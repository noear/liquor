/*
 * Copyright 2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.liquor.eval;

import org.noear.liquor.DynamicClassLoader;
import org.noear.liquor.DynamicCompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Liquor 评估器（线程安全）
 *
 * @author noear
 * @since 1.2
 * @since 1.4
 */
public class LiquorEvaluator implements Evaluator {
    //默认实例（也可定制自己的实例，增加全局导入）
    private static final Evaluator instance = new LiquorEvaluator(null);

    public static Evaluator getInstance() {
        return instance;
    }


    private boolean printable = false;

    private final DynamicCompiler compiler;
    private final DynamicClassLoader cachedClassLoader;
    private DynamicClassLoader tempClassLoader;
    private final AtomicLong tempCount = new AtomicLong();

    private final List<String> globalImports = new ArrayList<>();
    private final Map<CodeSpec, Execable> cachedMap = new ConcurrentHashMap<>();
    private final Map<CodeSpec, Long> nameMap = new ConcurrentHashMap<>();
    private final AtomicLong nameIdx = new AtomicLong(0L);
    private final ReentrantLock lock = new ReentrantLock();

    public LiquorEvaluator(ClassLoader parentClassLoader) {
        this.compiler = new DynamicCompiler(parentClassLoader);
        this.cachedClassLoader = compiler.getClassLoader();
        this.tempClassLoader = compiler.newClassLoader();
    }

    /**
     * 构建类
     */
    protected Class<?> build(CodeSpec codeSpec) {
        //1.分离导入代码

        Set<String> importBuilder = new TreeSet<>();
        StringBuilder codeBuilder = new StringBuilder();

        //全局导入
        for (String imp : globalImports) {
            importBuilder.add("import " + imp + ";\n");
        }

        //申明导入
        for (String imp : codeSpec.getImports()) {
            importBuilder.add("import " + imp + ";\n");
        }

        //代码导入
        if (codeSpec.getCode().contains("import ")) {
            BufferedReader reader = new BufferedReader(new StringReader(codeSpec.getCode()));

            try {
                String line;
                String lineTrim;
                while ((line = reader.readLine()) != null) {
                    lineTrim = line.trim();
                    if (lineTrim.startsWith("import ")) {
                        importBuilder.add(lineTrim + "\n");
                    } else {
                        codeBuilder.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            codeBuilder.append(codeSpec.getCode());
        }


        //2.构建代码申明

        String clazzName = "Execable$" + getKey(codeSpec);

        StringBuilder code = new StringBuilder();

        if (importBuilder.size() > 0) {
            for (String impCode : importBuilder) {
                code.append(impCode);
            }
            code.append("\n");
        }

        code.append("public class ").append(clazzName).append(" {\n");
        {
            code.append("  public static ");
            if (codeSpec.getReturnType() != null) {
                code.append(codeSpec.getReturnType().getCanonicalName());
            } else {
                code.append("void");
            }
            code.append(" _main$(");

            if (codeSpec.getParameters() != null && codeSpec.getParameters().length > 0) {
                for (int i = 0; i < codeSpec.getParameters().length; i++) {
                    Map.Entry<String, Class<?>> kv = codeSpec.getParameters()[i];
                    code.append(kv.getValue().getCanonicalName()).append(" ").append(kv.getKey()).append(",");
                }
                code.setLength(code.length() - 1);
            }
            code.append(")\n");
            code.append("  {\n");


            if (codeSpec.getCode().indexOf(';') < 0) {
                //没有 ";" 号（支持表达式）
                code.append("    return ").append(codeSpec.getCode()).append(";\n");
            } else {
                //有 ";" 号，说明是完整的语句
                code.append("    ").append(codeBuilder).append("\n");
            }


            code.append("  }\n");
        }
        code.append("}");

        if (printable) {
            System.out.println("-- Start(" + clazzName + ") --");
            System.out.println(code);
            System.out.println("-- End(" + clazzName + ") --");
        }

        //添加编译锁
        lock.tryLock();

        try {
            if (codeSpec.isCached()) {
                compiler.setClassLoader(cachedClassLoader);
            } else {
                if(tempCount.incrementAndGet() > 1000){
                    tempClassLoader = compiler.newClassLoader();
                    tempCount.set(0);
                }

                compiler.setClassLoader(tempClassLoader);
            }

            compiler.addSource(clazzName, code.toString()).build();

            return compiler.getClassLoader().loadClass(clazzName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 配置可打印的
     */
    public void printable(boolean printable) {
        this.printable = printable;
    }

    /**
     * 配置全局导入
     */
    public void globalImports(Class<?>... classes) {
        for (Class<?> clz : classes) {
            globalImports.add(clz.getCanonicalName());
        }
    }

    /**
     * 配置全局导入
     */
    public void globalImports(String... imports) {
        for (String imp : imports) {
            globalImports.add(imp);
        }
    }

    /**
     * 获取标记
     */
    protected Long getKey(CodeSpec codeSpec) {
        if (codeSpec.isCached() == false) {
            return nameIdx.incrementAndGet();
        }

        //中转一下，可避免有相同 hash 的情况
        return nameMap.computeIfAbsent(codeSpec, k -> nameIdx.incrementAndGet());
    }

    /**
     * 预编译
     *
     * @param codeSpec 代码申明
     */
    @Override
    public Execable compile(CodeSpec codeSpec) {
        assert codeSpec != null;

        if (codeSpec.isCached() == false) {
            return new ExecableImpl(build(codeSpec));
        }

        return cachedMap.computeIfAbsent(codeSpec, k -> new ExecableImpl(build(codeSpec)));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    @Override
    public Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException {
        assert codeSpec != null;

        try {
            return compile(codeSpec).exec(args);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }
}
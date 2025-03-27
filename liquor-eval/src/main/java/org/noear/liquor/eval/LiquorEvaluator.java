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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Liquor 评估器（线程安全）
 *
 * @author noear
 * @since 1.2
 * @since 1.3
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
    private int tempCount = 0;

    private final List<String> globalImports = new ArrayList<>();
    private final Map<CodeSpec, Execable> cachedMap;
    private final int cahceCapacity;
    private final Map<CodeSpec, Long> nameMap;
    private final AtomicLong nameIdx = new AtomicLong(0L);
    private final ReentrantLock lock = new ReentrantLock();

    public LiquorEvaluator(ClassLoader parentClassLoader) {
        this(parentClassLoader, 1000);
    }

    public LiquorEvaluator(ClassLoader parentClassLoader, int cahceCapacity) {
        this.compiler = new DynamicCompiler(parentClassLoader);
        this.cachedClassLoader = compiler.getClassLoader();
        this.tempClassLoader = compiler.newClassLoader();
        this.cahceCapacity = cahceCapacity;
        this.cachedMap = Collections.synchronizedMap(new LRUCache<>(cahceCapacity));
        this.nameMap = Collections.synchronizedMap(new LRUCache<>(cahceCapacity));

        globalImports.add(Map.class.getCanonicalName());
        globalImports.add(Execable.class.getCanonicalName());
        globalImports.add(ExecuteException.class.getCanonicalName());
    }

    /**
     * 构建类
     */
    protected Class<?> build(CodeSpec codeSpec) {
        boolean isCached = codeSpec.isCached();

        //添加编译锁
        lock.lock();

        try {
            if (isCached) {
                compiler.setClassLoader(cachedClassLoader);
            } else {
                if (tempCount++ > cahceCapacity) {
                    tempClassLoader = compiler.newClassLoader();
                    tempCount = 0;
                }

                compiler.setClassLoader(tempClassLoader);
            }

            String clazzName = this.addSource(codeSpec);
            compiler.build();

            return compiler.getClassLoader().loadClass(clazzName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 批量构建类
     *
     * @since 1.3.8
     */
    protected Map<CodeSpec, Class<?>> build(List<CodeSpec> codeSpecs) {
        boolean isCached = codeSpecs.get(0).isCached();

        //添加编译锁
        lock.lock();

        try {
            if (isCached) {
                compiler.setClassLoader(cachedClassLoader);
            } else {
                if (tempCount++ > cahceCapacity) {
                    tempClassLoader = compiler.newClassLoader();
                    tempCount = 0;
                }

                compiler.setClassLoader(tempClassLoader);
            }

            Map<CodeSpec, String> clazzNameMap = new HashMap<>();
            for (CodeSpec codeSpec : codeSpecs) {
                String clazzName = this.addSource(codeSpec);
                clazzNameMap.put(codeSpec, clazzName);
            }

            compiler.build();

            Map<CodeSpec, Class<?>> clazzMap = new HashMap<>();
            for (Map.Entry<CodeSpec, String> entry : clazzNameMap.entrySet()) {
                Class<?> clazz = compiler.getClassLoader().loadClass(entry.getValue());
                clazzMap.put(entry.getKey(), clazz);
            }
            return clazzMap;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 添加源码
     *
     * @since 1.3.8
     */
    protected String addSource(CodeSpec codeSpec) {
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

        code.append("public class ").append(clazzName).append(" implements Execable {\n");
        {
            code.append("  public Object exec(Map<String, Object> context) throws ExecuteException {\n");
            code.append("    try {\n");
            if (codeSpec.getReturnType() == null) {
                code.append("      execDo(context");
            } else {
                code.append("      return execDo(context");
            }

            code.append(");\n");
            code.append("    } catch(Throwable e) {\n");
            code.append("      throw new ExecuteException(e);\n");
            code.append("    }\n");

            if (codeSpec.getReturnType() == null) {
                code.append("    return null;\n");
            }

            code.append("  }\n\n");

            /// //////////////////////////////////////

            code.append("  public ");
            if (codeSpec.getReturnType() != null) {
                code.append(codeSpec.getReturnType().getCanonicalName());
            } else {
                code.append("void");
            }
            code.append(" execDo(Map<String, Object> _$CTX$) throws Throwable\n");
            code.append("  {\n");

            if (codeSpec.getParameters() != null && codeSpec.getParameters().size() > 0) {
                for (ParamSpec ps : codeSpec.getParameters()) {
                    code.append("    ").append(ps.getValue().getCanonicalName()).append(" ").append(ps.getName())
                            .append(" = ")
                            .append("(").append(ps.getValue().getCanonicalName()).append(")_$CTX$.get(\"").append(ps.getName()).append("\");")
                            .append("\n");
                }
            }


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
        compiler.addSource(clazzName, code.toString());

        return clazzName;
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
        if (codeSpec == null) {
            throw new IllegalArgumentException("The codeSpec parameter is null");
        }

        if (codeSpec.isCached() == false) {
            //不缓存
            return clazzToExecable(build(codeSpec));
        } else {
            //缓存
            return cachedMap.computeIfAbsent(codeSpec, k -> clazzToExecable(build(codeSpec)));
        }
    }

    private Execable clazzToExecable(Class<?> clazz) {
        try {
            return (Execable) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 批量预编译
     *
     * @param codeSpecs 代码申明集合
     * @since 1.3.8
     */
    @Override
    public Map<CodeSpec, Execable> compile(List<CodeSpec> codeSpecs) {
        if (codeSpecs == null || codeSpecs.size() == 0) {
            throw new IllegalArgumentException("The codeSpecs parameter is empty");
        }

        Map<CodeSpec, Execable> execableMap = new HashMap<>();

        if (codeSpecs.get(0).isCached() == false) {
            //不缓存
            for (Map.Entry<CodeSpec, Class<?>> entry : build(codeSpecs).entrySet()) {
                execableMap.put(entry.getKey(), clazzToExecable(entry.getValue()));
            }
        } else {
            //缓存

            //1.尝试先从缓存里取
            List<CodeSpec> codeSpecs2 = new ArrayList<>();
            for (CodeSpec codeSpec : codeSpecs) {
                Execable execable2 = cachedMap.get(codeSpec);
                if (execable2 == null) {
                    codeSpecs2.add(codeSpec);
                } else {
                    execableMap.put(codeSpec, execable2);
                }
            }

            //2.对无缓存的代码进行构建
            for (Map.Entry<CodeSpec, Class<?>> entry : build(codeSpecs).entrySet()) {
                Execable execable1 = clazzToExecable(entry.getValue());
                cachedMap.put(entry.getKey(), execable1);
                execableMap.put(entry.getKey(), execable1);
            }
        }

        return execableMap;
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param context  执行参数
     */
    @Override
    public Object eval(CodeSpec codeSpec, Map<String, Object> context) {
        if (codeSpec == null) {
            throw new IllegalArgumentException("The codeSpec parameter is null");
        }

        try {
            return compile(codeSpec).exec(context);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new ExecuteException(e);
        }
    }
}
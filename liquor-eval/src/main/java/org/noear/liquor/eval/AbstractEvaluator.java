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

import org.noear.liquor.DynamicCompiler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 虚拟评估器
 *
 * @author noear
 * @since 1.2
 */
public abstract class AbstractEvaluator implements IEvaluator {
    protected boolean cacheable = true;
    protected boolean printable = false;

    private ClassLoader parentClassLoader;
    private DynamicCompiler compiler;

    private final Map<CodeSpec, IExecutable> cachedMap = new ConcurrentHashMap<>();
    private final Map<CodeSpec, String> nameMap = new ConcurrentHashMap<>();
    private final AtomicLong nameCounter = new AtomicLong(0L);

    /**
     * 获取编译器
     */
    protected DynamicCompiler getCompiler() {
        if (compiler == null || cacheable == false) {
            compiler = new DynamicCompiler(parentClassLoader);
        }

        return compiler;
    }

    /**
     * 构建类
     */
    protected abstract Class<?> build(CodeSpec codeSpec);

    /**
     * 设置父类加载器
     */
    @Override
    public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    /**
     * 设置可缓存的（默认为 true）
     */
    @Override
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    /**
     * 获取标记
     */
    protected String getKey(CodeSpec codeSpec) {
        //中转一下，可避免有相同 hash 的情况
        return nameMap.computeIfAbsent(codeSpec, k -> String.valueOf(nameCounter.incrementAndGet()));
    }

    /**
     * 编译
     *
     * @param codeSpec 代码申明
     */
    @Override
    public IExecutable compile(CodeSpec codeSpec) {
        assert codeSpec != null;

        if (cacheable) {
            return cachedMap.computeIfAbsent(codeSpec, k -> new ExecutableImpl(build(codeSpec), codeSpec));
        } else {
            return new ExecutableImpl(build(codeSpec), codeSpec);
        }
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
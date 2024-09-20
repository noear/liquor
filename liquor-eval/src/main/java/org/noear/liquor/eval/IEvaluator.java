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

import java.lang.reflect.InvocationTargetException;

/**
 * 评估器
 *
 * @author noear
 * @since 1.2
 */
public interface IEvaluator {
    /**
     * 设置父类加载器
     */
    void setParentClassLoader(ClassLoader parentClassLoader);

    /**
     * 设置可缓存的（默认为 true）
     */
    void setCacheable(boolean cacheable);

    /**
     * 设置可打印的（默认为 false）
     */
    void setPrintable(boolean printable);

    /**
     * 获取类
     */
    default Class<?> getClazz(String code) {
        return getClazz(new CodeSpec(code));
    }

    /**
     * 获取类
     */
    Class<?> getClazz(CodeSpec codeSpec);

    /**
     * 评估
     *
     * @param code 代码
     */
    default Object eval(String code) throws InvocationTargetException {
        assert code != null;
        return eval(new CodeSpec(code));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException;
}
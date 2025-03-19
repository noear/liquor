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
import java.lang.reflect.Method;

/**
 * 可执行的
 *
 * @author noear
 * @since 1.2
 * @since 1.4
 */
public interface Execable {
    /**
     * 获取方法
     */
    Method getMethod();

    /**
     * 执行
     */
    Object exec(Object... args) throws EvaluationException;
}
/*
 * Copyright 2024 - 2025 noear.org and authors
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

import java.util.List;
import java.util.Map;

/**
 * 评估器
 *
 * @author noear
 * @since 1.2
 * @since 1.3
 */
public interface Evaluator {
    /**
     * 配置可打印的
     */
    void printable(boolean printable);

    /**
     * 预编译
     *
     * @param codeSpec 代码申明
     */
    Execable compile(CodeSpec codeSpec);

    /**
     * 批量预编译
     *
     * @param codeSpecs 代码申明集合
     * @since 1.3.8
     */
    Map<CodeSpec, Execable> compile(List<CodeSpec> codeSpecs);

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param context  执行参数
     */
    Object eval(CodeSpec codeSpec, Map<String, Object> context);
}
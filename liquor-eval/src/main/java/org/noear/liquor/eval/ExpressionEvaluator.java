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

/**
 * 表达式评估器（必须有估评结果）
 *
 * @author noear
 * @since 1.2
 */
public class ExpressionEvaluator extends AbstractEvaluator implements IEvaluator {
    private static final ExpressionEvaluator instance = new ExpressionEvaluator();

    /**
     * 获取快捷实例
     */
    public static ExpressionEvaluator getInstance() {
        return instance;
    }

    //////////////////////

    @Override
    protected Class<?> build(CodeSpec codeSpec) {
        //必须有估评结果
        if (codeSpec.getReturnType() == null) {
            codeSpec.returnType(Object.class);
        }

        return super.build(codeSpec);
    }
}
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
import java.util.Map;

/**
 * 表达式快捷工具类（必须要有返回）
 *
 * @author noear
 * @since 1.3
 */
public interface Exprs {
    /**
     * 编译
     *
     * @param code 代码
     */
    static Execable compile(String code) {
        return compile(new CodeSpec(code));
    }

    /**
     * 编译
     *
     * @param codeSpec 代码申明
     */
    static Execable compile(CodeSpec codeSpec) {
        //强制有估评结果
        if (codeSpec.getReturnType() == null) {
            codeSpec.returnType(Object.class);
        }

        return LiquorEvaluator.getInstance().compile(codeSpec);
    }


    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code) throws InvocationTargetException {
        return eval(new CodeSpec(code));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    static Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException {
        //强制有估评结果
        if (codeSpec.getReturnType() == null) {
            codeSpec.returnType(Object.class);
        }

        return LiquorEvaluator.getInstance().eval(codeSpec, args);
    }

    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code, Map<String, Object> context) throws InvocationTargetException {
        assert context != null;

        CodeSpec codeSpec = new CodeSpec(code);
        Object[] args = codeSpec.bind(context);

        return eval(codeSpec, args);
    }
}
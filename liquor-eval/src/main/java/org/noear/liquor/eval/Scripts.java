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
 * 脚本工具类
 *
 * @author noear
 * @since 1.3
 */
public interface Scripts {
    /**
     * 编译
     *
     * @param code 代码
     */
    static Execable compile(String code) {
        assert code != null;
        return compile(new CodeSpec(code));
    }

    /**
     * 编译
     *
     * @param codeSpec 代码申明
     */
    static Execable compile(CodeSpec codeSpec) {
        return LiquorEvaluator.getInstance().compile(codeSpec);
    }


    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code) throws InvocationTargetException {
        assert code != null;
        return eval(new CodeSpec(code));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    static Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException {
        return LiquorEvaluator.getInstance().eval(codeSpec, args);
    }
}
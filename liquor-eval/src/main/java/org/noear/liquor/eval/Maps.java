/*
 * Copyright 2025 noear.org and authors
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.5
 * @deprecated 1.5 {@link org.noear.liquor.Utils}
 */
@Deprecated
public class Maps extends LinkedHashMap<String,Object> implements Map<String,Object> {
    public static Maps of() {
        return new Maps();
    }

    public static Maps of(String key, Object value) {
        return new Maps().set(key, value);
    }

    public Maps set(String key, Object value) {
        put(key, value);
        return this;
    }
}
/*
 * Copyright 2018 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.leonardoxh.keystore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class InMemoryCipherStorage implements CipherStorage {
    private final Map<String, String> values = new HashMap<>();

    @Override
    public void encrypt(String alias, String value) {
        values.put(alias, value);
    }

    @Nullable
    @Override
    public String decrypt(String alias) {
        return values.get(alias);
    }

    @Override
    public boolean containsAlias(String alias) {
        return values.containsKey(alias);
    }

    @Override
    public void removeKey(String alias) {
        values.remove(alias);
    }

    @Override
    public void saveOrReplace(String alias, String value) {
        encrypt(alias, value);
    }
}

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
package com.github.leonardoxh.keystore.store;

import javax.annotation.Nullable;

public interface Storage {
    /**
     * Responsible for save the current value associated with this alias
     * @param alias something unique to retrieve the value from the system
     * @param content the actual content of the key (encrypted)
     */
    void saveKeyBytes(String alias, byte[] content);

    /**
     * Return the current encrypted value of the current alias
     * @param alias the unique alias to retrieve
     * @return the key bytes in success null otherwise
     */
    @Nullable byte[] getKeyBytes(String alias);

    /**
     * Check if the value for this alias exists
     * @param alias the unique alias to check
     * @return true if the value exists
     */
    boolean containsAlias(String alias);

    /**
     * Remove the current value associated with this alias
     * @param alias the unique alias to check
     */
    void remove(String alias);
}

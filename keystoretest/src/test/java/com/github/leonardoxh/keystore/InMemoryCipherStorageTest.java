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

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InMemoryCipherStorageTest {
    private final CipherStorage subject = CipherStorageFactory.newInstance(null);

    @Test
    public void encryptValues() {
        subject.encrypt("test", "test-value");
        assertThat(subject.containsAlias("test")).isTrue();
    }

    @Test
    public void decryptValues() {
        subject.encrypt("test", "test-value");
        assertThat(subject.decrypt("test")).isEqualTo("test-value");
    }

    @Test
    public void removeValues() {
        subject.encrypt("test", "test-alias");
        subject.removeKey("test");
        assertThat(subject.containsAlias("test")).isFalse();
    }

    @Test
    public void replaceValues() {
        subject.encrypt("test", "test");
        assertThat(subject.decrypt("test")).isEqualTo("test");

        subject.encrypt("test", "test-replaced");
        assertThat(subject.decrypt("test")).isEqualTo("test-replaced");
    }
}

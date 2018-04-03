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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

abstract class CipherBaseTest {
    private static final String ALIAS_FOR_TEST = "test_alias_api";
    private static final String VALUE_FOR_TEST = "test";

    private CipherStorage subject;

    @Before
    public void setUp() {
        subject = init();
        subject.encrypt(ALIAS_FOR_TEST, VALUE_FOR_TEST);

        assertThat(subject.containsAlias(ALIAS_FOR_TEST)).isTrue();
    }

    @After
    public void tearDown() {
        subject.removeKey(ALIAS_FOR_TEST);

        assertThat(subject.containsAlias(ALIAS_FOR_TEST)).isFalse();
    }

    @Test
    public void itDecryptKeyCorrectly() {
        String decrypted = subject.decrypt(ALIAS_FOR_TEST);
        assertThat(decrypted).isNotEmpty();
        assertThat(decrypted).isEqualTo(VALUE_FOR_TEST);
    }

    @Test
    public void itReplaceKeyCorrectly() {
        String valueToStore = "other-alias";
        subject.saveOrReplace(ALIAS_FOR_TEST, valueToStore);
        assertThat(subject.decrypt(ALIAS_FOR_TEST)).isEqualTo(valueToStore);
    }

    @Test
    public void itReturnNullWithoutAnyKey() {
        subject.removeKey(ALIAS_FOR_TEST);

        assertThat(subject.decrypt(ALIAS_FOR_TEST)).isNull();
    }

    abstract CipherStorage init();
}

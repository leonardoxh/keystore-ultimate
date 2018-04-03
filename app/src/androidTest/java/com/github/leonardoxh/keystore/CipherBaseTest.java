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

    abstract CipherStorage init();
}

package org.bitbucket.leorossetto.keystore;

import com.github.leonardoxh.keystore.CipherStorageFactory;
import com.github.leonardoxh.keystore.InMemoryCipherStorage;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CipherStorageFactoryTest {
    @Test
    public void returnCorrectImplementation() {
        assertThat(CipherStorageFactory.newInstance(null)).isInstanceOf(InMemoryCipherStorage.class);
    }
}
package com.github.leonardoxh.keystore;

import javax.annotation.Nullable;

public interface CipherStorage {
    void encrypt(String alias, String value);
    @Nullable String decrypt(String alias);
    boolean containsAlias(String alias);
    void removeKey(String alias);
    void saveOrReplace(String alias, String value);
}

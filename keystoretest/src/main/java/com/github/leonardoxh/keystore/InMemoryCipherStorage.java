package com.github.leonardoxh.keystore;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCipherStorage implements CipherStorage {
    private final Map<String, String> values = new HashMap<>();

    @Override
    public void encrypt(String alias, String value) {
        values.put(alias, value);
    }

    @Override
    public String decrypt(String alias) {
        return values.get(alias);
    }

    @Override
    public boolean containsAlias(String alias) {
        return values.containsValue(alias);
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

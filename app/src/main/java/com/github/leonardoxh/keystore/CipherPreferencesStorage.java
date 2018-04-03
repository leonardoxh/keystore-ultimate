package com.github.leonardoxh.keystore;

import android.content.Context;
import android.util.Base64;

import javax.annotation.Nullable;

final class CipherPreferencesStorage {
    private static final String SHARED_PREFERENCES_NAME = "security_storage";

    private CipherPreferencesStorage() {
        throw new AssertionError();
    }

    static void saveKeyString(Context context, String alias, String value) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(alias, value)
                .apply();
    }

    static void remove(Context context, String alias) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(alias)
                .apply();
    }

    static boolean containsAlias(Context context, String alias) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .contains(alias);
    }

    @Nullable
    static String getKeyString(Context context, String alias) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(alias, null);
    }

    @Nullable
    static byte[] getKeyBytes(Context context, String alias) {
        String value = getKeyString(context, alias);
        if (value != null) {
            return Base64.decode(value, Base64.DEFAULT);
        }
        return null;
    }

    static void saveKeyBytes(Context context, String alias, byte[] value) {
        saveKeyString(context, alias, Base64.encodeToString(value, Base64.DEFAULT));
    }
}

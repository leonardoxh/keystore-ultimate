package com.github.leonardoxh.keystore;

import android.content.Context;
import android.os.Build;

public final class CipherStorageFactory {
    private CipherStorageFactory() {
        throw new AssertionError();
    }

    public CipherStorage newInstance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new CipherStorageAndroidKeystore(context);
        }
        return new CipherStorageSharedPreferencesKeystore(context);
    }
}

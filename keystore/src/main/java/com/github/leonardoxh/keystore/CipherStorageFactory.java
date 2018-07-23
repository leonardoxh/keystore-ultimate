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

import android.content.Context;
import android.os.Build;

import com.github.leonardoxh.keystore.store.CipherPreferencesStorage;
import com.github.leonardoxh.keystore.store.Storage;

/**
 * Factory class for {@link CipherStorage} it'll decide what
 * is the best implementation based on the current api level
 *
 * @see #newInstance(Context, Storage)
 */
public final class CipherStorageFactory {
    private CipherStorageFactory() {
        throw new AssertionError();
    }

    /**
     * Create a new instance of the {@link CipherStorage} based on the
     * current api level, on API 22 and bellow it will use the {@link CipherStorageAndroidKeystore}
     * and on api 23 and above it will use the {@link CipherStorageSharedPreferencesKeystore}
     *
     * @param context used for api 22 and bellow to access the keystore and
     *                access the Android Shared preferences, on api 23 and above
     *                it's only used for Android Shared Preferences access
     *
     * @param storage abstraction for store the key and value bytes into the system
     *                you can implement your own version of the storage to fit your needs
     * @return a new {@link CipherStorage} based on the current api level
     */
    public static CipherStorage newInstance(Context context, Storage storage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new CipherStorageAndroidKeystore(context, storage);
        }
        return new CipherStorageSharedPreferencesKeystore(context, storage);
    }

    /**
     * @see #newInstance(Context, Storage)
     */
    public static CipherStorage newInstance(Context context) {
        return newInstance(context, new CipherPreferencesStorage(context));
    }
}

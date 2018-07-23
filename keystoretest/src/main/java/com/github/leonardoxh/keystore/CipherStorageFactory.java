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

import com.github.leonardoxh.keystore.store.Storage;

import javax.annotation.Nullable;

/**
 * Factory class for test
 * This will return a dummy but functional implementation
 * for test implementations
 */
public final class CipherStorageFactory {
    private CipherStorageFactory() {
        throw new AssertionError();
    }

    /**
     * Factory for test usage
     * @param context not used in tests
     * @return a {@link InMemoryCipherStorage} for test proposes
     */
    public static CipherStorage newInstance(@Nullable Context context) {
        return InMemoryCipherStorage.INSTANCE;
    }

    /**
     * Dummy factory
     * @see #newInstance(Context)
     */
    public static CipherStorage newInstance(@Nullable Context context, Storage storage) {
        return newInstance(context);
    }
}

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

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.github.leonardoxh.keystore.store.CipherPreferencesStorage;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CipherStorageApi22Test extends CipherBaseTest {
    @Override
    CipherStorage init() {
        return new CipherStorageSharedPreferencesKeystore(InstrumentationRegistry.getContext(),
                new CipherPreferencesStorage(InstrumentationRegistry.getContext()));
    }
}

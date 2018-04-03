package com.github.leonardoxh.keystore;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CipherStorageApi23Test extends CipherBaseTest {
    @Override
    CipherStorage init() {
        return new CipherStorageAndroidKeystore(InstrumentationRegistry.getContext());
    }
}

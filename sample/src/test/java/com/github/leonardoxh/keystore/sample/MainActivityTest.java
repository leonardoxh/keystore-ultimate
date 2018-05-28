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
package com.github.leonardoxh.keystore.sample;

import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        EditText key = mainActivity.findViewById(R.id.key);
        key.setText("test");
        EditText value = mainActivity.findViewById(R.id.value);
        value.setText("test-value");
        mainActivity.findViewById(R.id.save).callOnClick();
        value.setText("");
    }

    @Test
    public void decryptValue() {
        EditText value = mainActivity.findViewById(R.id.value);

        mainActivity.findViewById(R.id.retrieve).callOnClick();
        assertThat(value.getText().toString()).isEqualTo("test-value");
    }

    @Test
    public void eraseValue() {
        mainActivity.findViewById(R.id.retrieve).callOnClick();
        mainActivity.findViewById(R.id.erase).callOnClick();

        EditText value = mainActivity.findViewById(R.id.value);
        mainActivity.findViewById(R.id.retrieve).callOnClick();
        assertThat(value.getText().toString()).isEmpty();
    }
}

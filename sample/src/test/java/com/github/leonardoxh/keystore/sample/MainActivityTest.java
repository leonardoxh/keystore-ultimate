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
package com.github.leonardoxh.keystore.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.leonardoxh.keystore.CipherStorage;
import com.github.leonardoxh.keystore.CipherStorageFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText keyInput;
    private EditText valueInput;
    private CipherStorage cipherStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cipherStorage = CipherStorageFactory.newInstance(this);
        keyInput = findViewById(R.id.key);
        valueInput = findViewById(R.id.value);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.retrieve).setOnClickListener(this);
        findViewById(R.id.erase).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrieve:
                retrieveValue();
                break;
            case R.id.save:
                saveValue();
                break;
            case R.id.erase:
                erase();
                break;
        }
    }

    private void erase() {
        if (TextUtils.isEmpty(keyInput.getText())) {
            Toast.makeText(this, R.string.empty_hint_or_password, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        cipherStorage.removeKey(keyInput.getText().toString());
        keyInput.setText("");
        valueInput.setText("");
    }

    private void saveValue() {
        if (TextUtils.isEmpty(keyInput.getText()) || TextUtils.isEmpty(valueInput.getText())) {
            Toast.makeText(this, R.string.empty_hint_or_password, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        cipherStorage.encrypt(keyInput.getText().toString(), valueInput.getText().toString());
    }

    private void retrieveValue() {
        if (TextUtils.isEmpty(keyInput.getText())) {
            Toast.makeText(this, R.string.empty_hint_or_password, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String value = cipherStorage.decrypt(keyInput.getText().toString());
        valueInput.setText(value);
    }
}

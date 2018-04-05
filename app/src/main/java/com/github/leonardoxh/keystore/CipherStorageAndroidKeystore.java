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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.spec.AlgorithmParameterSpec;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

@TargetApi(Build.VERSION_CODES.M)
final class CipherStorageAndroidKeystore extends BaseCipherStorage {
    private static final String KEYSTORE_TYPE = "AndroidKeyStore";
    private static final String ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    private static final String ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    private static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String ENCRYPTION_TRANSFORMATION =
            ENCRYPTION_ALGORITHM + "/" +
            ENCRYPTION_BLOCK_MODE + "/" +
            ENCRYPTION_PADDING;
    private static final int ENCRYPTION_KEY_SIZE = 256;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    CipherStorageAndroidKeystore(Context context) {
        super(context);
    }

    @Override
    public void encrypt(String alias, String value) {
        try {
            KeyStore keyStore = getKeyStoreAndLoad();

            KeyGenerator generator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, KEYSTORE_TYPE);
            generator.init(generateParameterSpec(alias));
            generator.generateKey();

            Key key = keyStore.getKey(alias, null);
            byte[] encryptedData = encryptString(key, value);
            CipherPreferencesStorage.saveKeyBytes(context, alias, encryptedData);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException | UnrecoverableKeyException e) {
            throw new CryptoFailedException("Could not encrypt data", e);
        } catch (KeyStoreException | KeyStoreAccessException e) {
            throw new CryptoFailedException("Could not access Keystore", e);
        }
    }

    @Nullable
    @Override
    public String decrypt(String alias) {
        try {
            KeyStore keyStore = getKeyStoreAndLoad();
            Key key = keyStore.getKey(alias, null);
            byte[] storedData = CipherPreferencesStorage.getKeyBytes(context, alias);
            if (storedData == null) {
                return null;
            }
            return decryptBytes(key, storedData);
        } catch (KeyStoreException | UnrecoverableKeyException |
                NoSuchAlgorithmException | KeyStoreAccessException e) {
            return null;
        }
    }

    private static AlgorithmParameterSpec generateParameterSpec(String alias) {
        return new KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                .setBlockModes(ENCRYPTION_BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setRandomizedEncryptionRequired(true)
                .setKeySize(ENCRYPTION_KEY_SIZE)
                .build();
    }

    private static String decryptBytes(Key key, byte[] bytes) throws CryptoFailedException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            // read the initialization vector from the beginning of the stream
            IvParameterSpec ivParams = readIvFromStream(inputStream);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            // decrypt the bytes using a CipherInputStream
            CipherInputStream cipherInputStream = new CipherInputStream(
                    inputStream, cipher);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int n = cipherInputStream.read(buffer, 0, buffer.length);
                if (n <= 0) {
                    break;
                }
                output.write(buffer, 0, n);
            }
            return new String(output.toByteArray(), DEFAULT_CHARSET);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new CryptoFailedException("Could not decrypt bytes", e);
        }
    }

    private static IvParameterSpec readIvFromStream(ByteArrayInputStream inputStream) {
        byte[] iv = new byte[16];
        inputStream.read(iv, 0, iv.length);
        return new IvParameterSpec(iv);
    }

    private static byte[] encryptString(Key key, String value) throws CryptoFailedException {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // write initialization vector to the beginning of the stream
            byte[] iv = cipher.getIV();
            outputStream.write(iv, 0, iv.length);
            // encrypt the value using a CipherOutputStream
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            cipherOutputStream.write(value.getBytes(DEFAULT_CHARSET));
            cipherOutputStream.close();
            return outputStream.toByteArray();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new CryptoFailedException("Could not encrypt value", e);
        }
    }
}

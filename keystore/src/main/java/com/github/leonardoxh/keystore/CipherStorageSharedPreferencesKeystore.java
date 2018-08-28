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
import android.security.KeyPairGeneratorSpec;

import com.github.leonardoxh.keystore.store.Storage;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
final class CipherStorageSharedPreferencesKeystore extends BaseCipherStorage {
    private static final String KEY_ALGORITHM_RSA = "RSA";
    private static final String KEY_ALGORITHM_AES = "AES";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final int ENCRYPTION_KEY_SIZE = 128;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String AES_TAG_PREFIX = "aes!";
    private static final BigInteger KEY_SERIAL_NUMBER = BigInteger.valueOf(1338);

    CipherStorageSharedPreferencesKeystore(Context context, Storage storage) {
        super(context, storage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encrypt(String alias, String value) {
        KeyStore.Entry entry = getKeyStoreEntry(true, alias);
        if (entry == null) {
            throw new CryptoFailedException("Unable to generate key for alias " + alias);
        }

        KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry) entry;
        byte[] encryptedData = encryptData(alias, value, key.getCertificate().getPublicKey());

        storage.saveKeyBytes(alias, encryptedData);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String decrypt(String alias) {
        KeyStore.Entry entry = getKeyStoreEntry(false, alias);
        if (entry == null) {
            return null;
        }
        KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry) entry;
        return decryptData(alias, key.getPrivateKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAlias(String alias) {
        return super.containsAlias(alias)
                && storage.containsAlias(makeAesTagForAlias(alias));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeKey(String alias) {
        super.removeKey(alias);
        storage.remove(makeAesTagForAlias(alias));
    }

    @Nullable
    private String decryptData(String alias, PrivateKey privateKey) {
        byte[] encryptedData = storage.getKeyBytes(alias);
        byte[] secretData = storage.getKeyBytes(makeAesTagForAlias(alias));
        if (encryptedData == null || secretData == null) {
            return null;
        }

        byte[] decryptedData = cipherEncryption(TRANSFORMATION, Cipher.PRIVATE_KEY, privateKey, secretData);
        SecretKeySpec secretKey = new SecretKeySpec(decryptedData, 0, decryptedData.length, KEY_ALGORITHM_AES);
        byte[] finalData = cipherEncryption(KEY_ALGORITHM_AES, Cipher.DECRYPT_MODE, secretKey, encryptedData);
        return new String(finalData, DEFAULT_CHARSET);
    }

    private void generateKeyRsa(String alias) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
            keyPairGenerator.initialize(getParameterSpec(alias));
            keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new KeyStoreAccessException("Unable to access keystore", e);
        }
    }

    private AlgorithmParameterSpec getParameterSpec(String alias)  {
        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 5);

        return new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(KEY_SERIAL_NUMBER)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
    }

    @Nullable
    private KeyStore.Entry getKeyStoreEntry(boolean shouldGenerateKey, String alias) {
        try {
            KeyStore keyStore = getKeyStoreAndLoad();

            KeyStore.Entry entry = keyStore.getEntry(alias, null);
            if (entry == null) {
                if (shouldGenerateKey) {
                    generateKeyRsa(alias);
                    entry = keyStore.getEntry(alias, null);
                }
            }
            return entry;
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            throw new KeyStoreAccessException("Unable to access keystore", e);
        }
    }

    private byte[] encryptData(String alias, String value, PublicKey publicKey) {
        SecretKey secret = generateKeyAes(alias);
        byte[] rsaEncrypted = cipherEncryption(TRANSFORMATION, Cipher.PUBLIC_KEY, publicKey, secret.getEncoded());
        storage.saveKeyBytes(makeAesTagForAlias(alias), rsaEncrypted);
        return cipherEncryption(KEY_ALGORITHM_AES, Cipher.ENCRYPT_MODE, secret, value.getBytes(DEFAULT_CHARSET));
    }

    private SecretKey generateKeyAes(String alias) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
            generator.init(ENCRYPTION_KEY_SIZE);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoFailedException("Unable to generate key for alias " + alias, e);
        }
    }

    private static String makeAesTagForAlias(String alias) {
        return AES_TAG_PREFIX + alias;
    }

    private static byte[] cipherEncryption(String transformation, int mode, Key key,
                                           byte[] inputByteArray) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(mode, key);
            return cipher.doFinal(inputByteArray);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoFailedException(String.format(Locale.US,
                    "Unable to do cipher for transformation %s and mode %d", transformation, mode), e);
        }
    }
}

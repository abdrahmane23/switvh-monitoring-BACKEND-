package com.example.project.Services.Impls;

import com.example.project.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
@Service
public class EncryptionServiceImpl implements EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private final SecretKeySpec key;

    public EncryptionServiceImpl (
            @Value("${encryption.secret-key}") String secretKey) {

        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        if (keyBytes.length != 32) {
            throw new IllegalArgumentException(
                    "Encryption key must be exactly 32 bytes"
            );
        }

        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);

            GCMParameterSpec spec =
                    new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] ciphertext =
                    cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // IV + ciphertext + authentication tag
            byte[] result = new byte[IV_LENGTH + ciphertext.length];

            System.arraycopy(iv, 0, result, 0, IV_LENGTH);
            System.arraycopy(
                    ciphertext,
                    0,
                    result,
                    IV_LENGTH,
                    ciphertext.length
            );

            return Base64.getEncoder().encodeToString(result);

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] decoded =
                    Base64.getDecoder().decode(encryptedText);

            if (decoded.length <= IV_LENGTH) {
                throw new IllegalArgumentException(
                        "Invalid encrypted value"
                );
            }

            // Extract IV
            byte[] iv = Arrays.copyOfRange(
                    decoded,
                    0,
                    IV_LENGTH
            );

            // Extract ciphertext + authentication tag
            byte[] ciphertext = Arrays.copyOfRange(
                    decoded,
                    IV_LENGTH,
                    decoded.length
            );

            Cipher cipher = Cipher.getInstance(ALGORITHM);

            GCMParameterSpec spec =
                    new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] plaintext = cipher.doFinal(ciphertext);

            return new String(
                    plaintext,
                    StandardCharsets.UTF_8
            );

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
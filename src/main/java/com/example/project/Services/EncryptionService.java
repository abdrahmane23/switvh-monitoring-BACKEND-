package com.example.project.Services;

public interface EncryptionService {
    String encrypt(String password);
    String decrypt(String encryptedPassword);
}

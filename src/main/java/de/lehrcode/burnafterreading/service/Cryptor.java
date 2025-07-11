package de.lehrcode.burnafterreading.service;

import javax.crypto.SecretKey;

public interface Cryptor {
    SecretKey parseKey(byte[] keyBytes);
    SecretKey generateKey();
    byte[] encrypt(byte[] data, SecretKey key);
    byte[] decrypt(byte[] data, SecretKey key);
    byte[] hash(byte[] data);
}

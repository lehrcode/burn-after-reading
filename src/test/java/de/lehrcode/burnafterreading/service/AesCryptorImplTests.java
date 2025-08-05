package de.lehrcode.burnafterreading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AesCryptorImpl.class)
class AesCryptorImplTests {
    @Autowired
    Cryptor cryptor;

    @Test
    void hash() {
        var expectedHash = Base64.getDecoder().decode("12jMyCfF0Kq1pGAYXfvOL7TxRPYO2qPLZq+cCGsD2H4=");
        var hash = cryptor.hash("Geheim".getBytes(StandardCharsets.UTF_8));
        Assertions.assertArrayEquals(expectedHash, hash);
    }

    @Test
    void generateKey() {
        var key1 = cryptor.generateKey();
        var key2 = cryptor.generateKey();

        Assertions.assertNotNull(key1);
        Assertions.assertNotNull(key2);
        Assertions.assertNotEquals(key1, key2);
    }

    @Test
    void encrypt_and_decrypt() {
        var secret = "hallo";
        var key = cryptor.generateKey();
        var encrypted = cryptor.encrypt(secret.getBytes(), key);
        var decrypted = cryptor.decrypt(encrypted, key);

        assertEquals(secret, new String(decrypted));
    }
}

package com.geeker.marketing.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by Lubin.Xuan on 2017-11-03.
 * {desc}
 */
@Component
public class KeyUtils {

    @Value("${rsa.publicKey}")
    private String publicKey;
    @Value("${rsa.privateKey}")
    private String privateKey;

    private PrivateKey prk;
    private PublicKey plk;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        prk = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        plk = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    }


    public String encode(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, plk);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(Charset.forName("utf-8"))));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public String decode(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prk);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), "utf-8");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();
        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }
}

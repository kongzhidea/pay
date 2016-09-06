package com.kk.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    private static Log logger = LogFactory.getLog(RSA.class);

    private final static String CHARSET = "UTF-8";
    private final static String RSA = "RSA";
    private final static String DEFAULT_ALGORITHM = "SHA1WithRSA";

    public final static String encrypt(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory factory = KeyFactory.getInstance(RSA);
            PrivateKey priKey = factory.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(DEFAULT_ALGORITHM);

            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));

            byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static boolean verify(String sign, String content, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = java.security.Signature.getInstance(DEFAULT_ALGORITHM);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));

            return signature.verify(Base64.decodeBase64(sign));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
}

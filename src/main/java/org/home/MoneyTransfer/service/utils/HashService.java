package org.home.MoneyTransfer.service.utils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashService {

    /**
     * Getting md5 hash of string
     * @param code
     * @return md5 hash
     */
    public static String getMD5(String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(code.getBytes(Charset.forName("UTF-8")));
            byte[] digest = md.digest();
            return DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return code;
        }
    }
}

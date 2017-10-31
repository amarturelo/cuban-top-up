package com.wirelesskings.wkreload.mailmiddleware.crypto;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Alberto on 30/10/2017.
 */

public class Crypto {
    private static MessageDigest md;

    private static String hashPw(String pw, String salt, int rounds) throws Exception {
        byte[] bSalt;
        byte[] bPw;

        String appendedSalt = new StringBuilder().append(pw).append('{').append(salt).append('}').toString();

        try {
            bSalt = appendedSalt.getBytes("ISO-8859-1");
            bPw = pw.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding", e);
        }

        byte[] digest = run(bPw, bSalt);
        for (int i = 1; i < rounds; i++) {
            digest = run(digest, bSalt);
        }

        return Base64.encodeBytes(digest);
    }

    private static byte[] run(byte[] input, byte[] salt) {
        md.update(input);
        return md.digest(salt);
    }


    public static String hashPassword(String password, String salt) throws Exception {
        String result = password;
        String appendedSalt = new StringBuilder().append('{').append(salt).append('}').toString();
        String appendedSalt2 = new StringBuilder().append(password).append('{').append(salt).append('}').toString();

        if (password != null) {
            //Security.addProvider(new BouncyCastleProvider());

            MessageDigest mda = MessageDigest.getInstance("SHA-512");
            byte[] pwdBytes = password.getBytes("UTF-8");
            byte[] saltBytes = appendedSalt.getBytes("UTF-8");
            byte[] saltBytes2 = appendedSalt2.getBytes("UTF-8");
            byte[] digesta = encode(mda, pwdBytes, saltBytes);

            //result = new String(digesta);
            for (int i = 1; i < 5000; i++) {

                digesta = encode(mda, digesta, saltBytes2);
            }

            result = Base64.encodeBytes(digesta);
        }
        return result;
    }

    private static byte[] encode(MessageDigest mda, byte[] pwdBytes,
                                 byte[] saltBytes) {
        mda.update(pwdBytes);
        byte[] digesta = mda.digest(saltBytes);
        return digesta;
    }


    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}

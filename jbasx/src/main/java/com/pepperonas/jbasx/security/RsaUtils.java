package com.pepperonas.jbasx.security;

import com.pepperonas.jbasx.io.IoUtils;
import com.pepperonas.jbasx.log.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class RsaUtils {

    private static final String TAG = "RsaUtils";


    /**
     * Generate a public-/private-key-pair and save it.
     *
     * @param destDir The directory where the key-pair should be stored.
     * @return In the first field the public-key, in the second field the private-key.
     */
    public static File[] generateKeys(String destDir) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            File dest = new File(destDir);
            if (!dest.exists()) {
                if (dest.mkdirs()) {
                    Log.d(TAG, "Directory created: '" + dest.getPath() + "'");
                } else {
                    Log.d(TAG, "Directory can't be created: '" + dest.getPath() + "'");
                    return null;
                }
            }

            String publicKeyName = "public.key", privateKeyName = "private.key";

            saveKey(dest, publicKeyName, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
            saveKey(dest, privateKeyName, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());

            File[] keyFiles = new File[2];
            keyFiles[0] = new File(dest + File.separator + publicKeyName);
            keyFiles[1] = new File(dest + File.separator + privateKeyName);

            return keyFiles;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Encrypt text.
     *
     * @param publicKey the public key
     * @param data      the data
     * @return The encrypted bytes.
     */
    public static byte[] encryptText(File publicKey, String data) {
        try {
            PublicKey pubKey = readPublicKeyFromFile(publicKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            return cipher.doFinal(data.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Encrypt bytes.
     *
     * @param publicKey the public key
     * @param bytes     the bytes
     * @return The encrypted bytes.
     */
    public static byte[] encryptBytes(File publicKey, byte[] bytes) {
        try {
            PublicKey pubKey = readPublicKeyFromFile(publicKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            return cipher.doFinal(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Encrypt file.
     *
     * @param publicKey the public key
     * @param file      the file
     * @return The encrypted bytes.
     */
    public static byte[] encryptFile(File publicKey, File file) {
        try {
            PublicKey pubKey = readPublicKeyFromFile(publicKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] bytes = IoUtils.toByteArray(file);
            return cipher.doFinal(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Decrypt data.
     *
     * @param privateKey the private key
     * @param data       the data
     * @return The decrypted text.
     */
    public static String decryptText(File privateKey, byte[] data) {
        try {
            PrivateKey pk = readPrivateKeyFromFile(privateKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pk);

            byte[] decryptedData = cipher.doFinal(data);
            return new String(decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Decrypt data.
     *
     * @param privateKey the private key
     * @param data       the data
     * @return The decrypted bytes.
     */
    public static byte[] decryptBytes(File privateKey, byte[] data) {
        try {
            PrivateKey pk = readPrivateKeyFromFile(privateKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pk);

            return cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Save key boolean.
     *
     * @param destDir        the dest dir
     * @param fileName       the file name
     * @param modulus        the modulus
     * @param publicExponent the public exponent
     * @return the boolean
     */
    private static boolean saveKey(File destDir, String fileName, BigInteger modulus, BigInteger publicExponent) {
        try {
            FileOutputStream fos = new FileOutputStream(destDir + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));

            oos.writeObject(modulus);
            oos.writeObject(publicExponent);

            oos.close();
            fos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Read public-key from a file.
     *
     * @param publicKey the public key
     * @return The public key.
     */
    private static PublicKey readPublicKeyFromFile(File publicKey) {
        try {
            FileInputStream fis = new FileInputStream(publicKey);
            ObjectInputStream ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");

            ois.close();
            fis.close();

            return fact.generatePublic(rsaPublicKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Read the private-key from a file.
     *
     * @param privateKey the private key
     * @return The private key.
     */
    private static PrivateKey readPrivateKeyFromFile(File privateKey) {
        try {
            FileInputStream fis = new FileInputStream(privateKey);
            ObjectInputStream ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");

            ois.close();
            fis.close();

            return fact.generatePrivate(rsaPrivateKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

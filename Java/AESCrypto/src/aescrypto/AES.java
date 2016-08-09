package aescrypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author naveen-kumar
 */
public class AES {

    /** Cipher for encryption **/
    Cipher ecipher;
    /** Cipher for decryption **/
    Cipher dcipher;
    /** Buffer used to transport the bytes from one stream to another **/
    byte[] buf = new byte[1024];

    /**
     * Initialize AES class object and Cipher Object
     * @param strkey Secret key must be 8/16/32 long
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException 
     */
    public AES(String strkey) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        byte[] keyBytes = new byte[16];
        byte[] iv = new byte[16];

        keyBytes = strkey.getBytes("UTF-8");
        Key key = new SecretKeySpec(keyBytes, "AES");
        iv = strkey.getBytes("UTF-8");

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        /** CBC requires an initialization vector **/
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    }

    /**
     * Encrypt InputStream into OutputStream
     * @param in
     * @param out
     * @throws IOException 
     */
    private void encrypt(InputStream in, OutputStream out) throws IOException {
        buf = new byte[1024];
        out = new CipherOutputStream(out, ecipher);
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    }

    /**
     * Decrypt InputStream to OutputStream
     * @param in
     * @param out
     * @throws IOException 
     */
    private void decrypt(InputStream in, OutputStream out) throws IOException {
        buf = new byte[1024];
        in = new CipherInputStream(in, dcipher);
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    }

    /**
     * Encrypt File
     * @param inputFile input file path
     * @param outputFile output file path
     * @throws IOException 
     */
    public void encryptFile(String inputFile, String outputFile) throws IOException {
        encrypt(new FileInputStream(inputFile), new FileOutputStream(outputFile));
    }

    /**
     * Decrypt File
     * @param inputFile input file path
     * @param outputFile output file path
     * @throws IOException 
     */
    public void decryptFile(String inputFile, String outputFile) throws IOException {
        decrypt(new FileInputStream(inputFile), new FileOutputStream(outputFile));
    }

    /**
     * Encrypt text
     * @param input input text
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public String encryptText(String input) throws UnsupportedEncodingException, IOException {
        InputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CipherOutputStream cout = new CipherOutputStream(out, ecipher);
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            cout.write(buf, 0, numRead);
        }
        cout.close();
        in.close();
        return Base64.encodeBase64String(out.toByteArray());
    }

    /**
     * Decrypt Text
     * @param input Input text
     * @return
     * @throws IOException 
     */
    public String decryptText(String input) throws IOException {
        InputStream in = new ByteArrayInputStream(Base64.decodeBase64(input));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CipherOutputStream out1 = new CipherOutputStream(out, dcipher);
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out1.write(buf, 0, numRead);
        }
        out1.close();
        return out.toString();
    }
}

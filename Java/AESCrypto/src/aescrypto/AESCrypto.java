package aescrypto;

/**
 *
 * @author naveen-kumar
 */
public class AESCrypto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String KEY = "SecretKey1234567";
        String ENCRYPT_IN = "C:\\nav\\AP.tif";
        String ENCRYPT_OUT = "C:\\nav\\AP.PIN";

        String DECRYPT_IN = "C:\\nav\\AP.PIN";
        String DECRYPT_OUT = "C:\\nav\\AP1.tif";
        try {
            AES aes = new AES(KEY);
            String encrypted = aes.encryptText("Naveen");
            System.out.println("Encrypted Text:" + encrypted);

            String decrypted = aes.decryptText("qAROFJDyRaqiAUohujnQHA==");
            System.out.println("Decrypted Text:" + decrypted);

            aes.encryptFile(ENCRYPT_IN, ENCRYPT_OUT);
            System.out.println("File Encrypted..");

            aes.decryptFile(DECRYPT_IN, DECRYPT_OUT);
            System.out.println("File Decrypted..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AESCrypto
{
    class Program
    {
        static void Main(string[] args)
        {
            String KEY = "SecretKey1234567";
            String ENCRYPT_IN = "C:\\nav\\AP.tif";
            String ENCRYPT_OUT = "C:\\nav\\AP.PIN";

            String DECRYPT_IN = "C:\\nav\\AP.PIN";
            String DECRYPT_OUT = "C:\\nav\\AP1.tif";
            try
            {
                AES aes = new AES(KEY);
                String encrypted = aes.EncryptText("Naveen");
                Console.WriteLine("Encrypted Text:" + encrypted);

                String decrypted = aes.DecryptText("qAROFJDyRaqiAUohujnQHA==");
                Console.WriteLine("Decrypted Text:" + decrypted);

                aes.EncryptFile(ENCRYPT_IN, ENCRYPT_OUT);
                Console.WriteLine("File Encrypted..");

                aes.DecryptFile(DECRYPT_IN, DECRYPT_OUT);
                Console.WriteLine("File Decrypted..");
                Console.ReadLine();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}

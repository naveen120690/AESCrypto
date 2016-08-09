using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace AESCrypto
{
    class AES
    {
        ICryptoTransform encryptor;
        ICryptoTransform decryptor;

        /// <summary>
        /// Initialize AES class object and Cipher Object
        /// </summary>
        /// <param name="sKey">Secret key must be 8/16/32 long</param>
        public AES(string sKey)
        {
            RijndaelManaged aes = new RijndaelManaged();

            byte[] key = Encoding.UTF8.GetBytes(sKey);
            byte[] IV = Encoding.UTF8.GetBytes(sKey);

            aes.Padding = PaddingMode.PKCS7;
            aes.Mode = CipherMode.CBC;
            aes.KeySize = 128;
            aes.BlockSize = 128;
            decryptor = aes.CreateDecryptor(key, IV);
            encryptor = aes.CreateEncryptor(key, IV);
        }

        /// <summary>
        /// Encrypt File
        /// </summary>
        /// <param name="inputFile">Input file path</param>
        /// <param name="outputFile">Output file path</param>
        public void EncryptFile(string inputFile, string outputFile)
        {
            using (FileStream fsCrypt = new FileStream(outputFile, FileMode.Create))
            {

                using (CryptoStream cs = new CryptoStream(fsCrypt, encryptor, CryptoStreamMode.Write))
                {
                    using (FileStream fsIn = new FileStream(inputFile, FileMode.Open))
                    {
                        int data;
                        while ((data = fsIn.ReadByte()) != -1)
                        {
                            cs.WriteByte((byte)data);
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Decrypt File
        /// </summary>
        /// <param name="inputFile">Input file path</param>
        /// <param name="outputFile">Output File path</param>
        public void DecryptFile(string inputFile, string outputFile)
        {
            using (FileStream fsCrypt = new FileStream(inputFile, FileMode.Open))
            {
                using (FileStream fsOut = new FileStream(outputFile, FileMode.Create))
                {
                    using (CryptoStream cs = new CryptoStream(fsCrypt, decryptor, CryptoStreamMode.Read))
                    {
                        int data; while ((data = cs.ReadByte()) != -1)
                        { fsOut.WriteByte((byte)data); }
                    }
                }
            }
        }

        /// <summary>
        /// Encrypt text
        /// </summary>
        /// <param name="input">Input text</param>
        /// <returns></returns>
        public string EncryptText(string input)
        {
            string output = "";
            using (Stream fsCrypt = new MemoryStream())
            {
                using (CryptoStream cs = new CryptoStream(fsCrypt, encryptor, CryptoStreamMode.Write))
                {
                    var bytes = Encoding.UTF8.GetBytes(input);
                    cs.Write(bytes, 0, bytes.Length);
                    cs.FlushFinalBlock();
                    fsCrypt.Position = 0;
                    byte[] bytes1 = new byte[fsCrypt.Length];
                    fsCrypt.Read(bytes1, 0, (int)fsCrypt.Length);
                    output = Convert.ToBase64String(bytes1);
                }
            }
            return output;
        }

        /// <summary>
        /// Decrypt Text
        /// </summary>
        /// <param name="input">input text</param>
        /// <returns></returns>
        public string DecryptText(string input)
        {
            string output = "";
            using (Stream fsCrypt = new MemoryStream())
            {
                using (CryptoStream cs = new CryptoStream(fsCrypt, decryptor, CryptoStreamMode.Write))
                {
                    var bytes = Convert.FromBase64String(input);
                    cs.Write(bytes, 0, bytes.Length);
                    cs.FlushFinalBlock();

                    fsCrypt.Position = 0;
                    StreamReader reader = new StreamReader(fsCrypt);
                    output = reader.ReadToEnd();
                }
            }
            return output;
        }
    }
}

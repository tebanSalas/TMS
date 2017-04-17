/*
 * NewClass.java
 *
 * Created on 9 de marzo de 2009, 02:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.unify.webcenter.desencrypter;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

// CIPHER / GENERATORS
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
 
// KEY SPECIFICATIONS
import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
 
// EXCEPTIONS
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
/**
 *
 * @author MARCELA QUINTERO
 */
   public class DesEncrypter {
        Cipher ecipher;
        Cipher dcipher;
        
   
        public DesEncrypter(String passPhrase) {
            
               byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
        };
 
        // Iteration count
        int iterationCount = 19;
 
        try {
 
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWITHMD5ANDDES").generateSecret(keySpec);
 
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
 
            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
 
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
 
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
        /*    // Create an 8-byte initialization vector
            byte[] iv = new byte[]{
                (byte)0x8E, 0x12, 0x39, (byte)0x9C,
                0x07, 0x72, 0x6F, 0x5A
            };
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            try {
                ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    
                // CBC requires an initialization vector
                ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            } catch (java.security.InvalidAlgorithmParameterException e) {
            } catch (javax.crypto.NoSuchPaddingException e) {
            } catch (java.security.NoSuchAlgorithmException e) {
            } catch (java.security.InvalidKeyException e) {
            }*/
        }
    
        
        // Buffer used to transport the bytes from one stream to another
        byte[] buf = new byte[1024];
    
        public void encrypt(InputStream in, OutputStream out) {
            try {
                // Bytes written to out will be encrypted
                out = new CipherOutputStream(out, ecipher);
    
                // Read in the cleartext bytes and write to out to encrypt
                int numRead = 0;
                while ((numRead = in.read(buf)) >= 0) {
                    out.write(buf, 0, numRead);
                }
                out.close();
            } catch (java.io.IOException e) {
            }
        }
    
        public void decrypt(InputStream in, OutputStream out) {
            try {
                // Bytes read from in will be decrypted
                in = new CipherInputStream(in, dcipher);
    
                // Read in the decrypted bytes and write the cleartext to out
                int numRead = 0;
                while ((numRead = in.read(buf)) >= 0) {
                    out.write(buf, 0, numRead);
                }
                out.close();
            } catch (java.io.IOException e) {
            }
        }
    }

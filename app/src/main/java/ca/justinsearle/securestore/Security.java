package ca.justinsearle.securestore;

import android.content.Context;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by Justin on 11/22/2016.
 */

public class Security {

    public Security () {

    }

    public Security (Context context) {
        encrypt("");
    }

    protected static boolean verifyMasterPassword(String password) {
        boolean verified = true;

        //check if password is long enough
        if (password.length() < 8) {
            verified = false;
        }

        return verified;
    }

    public void encrypt(String msg) {

        try{
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
            SecretKey myDesKey = keygenerator.generateKey();

            Cipher desCipher;
            desCipher = Cipher.getInstance("AES");


            byte[] text = "No body can see me.".getBytes("UTF8");


            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            byte[] textEncrypted = desCipher.doFinal(text);

            String s = new String(textEncrypted);
            System.out.println(s);

            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);

            s = new String(textDecrypted);
            System.out.println(s);
        }catch(Exception e)
        {
            System.out.println("Exception");
        }
    }
}

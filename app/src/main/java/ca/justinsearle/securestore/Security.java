package ca.justinsearle.securestore;

import android.content.Context;

/**
 * Created by Justin on 11/22/2016.
 */

public class Security {

    public Security () {

    }

    public Security (Context context) {

    }

    protected static boolean verifyMasterPassword(String password) {
        boolean verified = true;

        //check if password is long enough
        if (password.length() < 8) {
            verified = false;
        }

        return verified;
    }
}

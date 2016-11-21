package ca.justinsearle.securestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * Created by Admin on 11/17/2016.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        SystemClock.sleep(5000);

        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

package ca.justinsearle.securestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Justin on 11/17/2016.
 * This class handles all the functionality for the splash screen of secure store
 */

public class Splash extends Activity {

    /** Duration of wait time for the splash screen **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    /**
     * On creation of the splash activity we want to do the following
     *  - initialize the program by verifying files, directories and data
     *  - read from the config file to decide what to do and where to go
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //start loading data
        Splash.this.initialize();

        /* New Handler to start the next activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent;
                Config config = new Config();
                config.read();

                /* Create an Intent that will start the next Activity. */
                if (config.getProperty("master_login") == "false") {
                    mainIntent = new Intent(Splash.this, Main2Activity.class);
                } else {
                    mainIntent = new Intent(Splash.this, MainActivity.class);
                }

                //start new activity and finish splash screen activity
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    /**
     * Verify files, directores and data along with settings up anything needed for the splash screen
     */
    private void initialize() {
        Message.info("Starting program.");

        //start a new file handler class instance
        FileHandler fh = new FileHandler(this);

        //verify files and directories exists as well as verifying data is not corrupt or missing information
        if (!fh.verify()) {
            //something went wrong verifying
            Message.error("Closing application.");
            Splash.this.destroy();
        }
    } //end of initialize()

    /**
     * Destroy the application and close properly
     */
    private void destroy() {
        //googles function to close applications properly
        Splash.this.finish();
    } //end of destroy()
}

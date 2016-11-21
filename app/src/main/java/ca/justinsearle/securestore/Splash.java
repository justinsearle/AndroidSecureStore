package ca.justinsearle.securestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Justin on 11/17/2016.
 * This class handles all the functionality for the splash screen of secure store
 */

public class Splash extends Activity {

    /** Duration of wait time for the splash screen **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private int mProgressStatus = 0;
    private boolean initialLoad = false;

    private ProgressBar mProgress;
    private TextView txtLoading;
    private Config config;

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

        //check if this is the first time the application is being loaded
        if (!this.initialLoad) {
            /* New Handler to start the next activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
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
        } else {
            this.txtLoading.setText("Welcome to secure store");
        }
    }

    /**
     * Verify files, directores and data along with settings up anything needed for the splash screen
     */
    private void initialize() {
        Message.info("Starting program.");

        //find loading elements
        this.mProgress = (ProgressBar) findViewById(R.id.progressBarLoading);
        this.txtLoading = (TextView) findViewById(R.id.txtLoading);

        //start a new file handler class instance
        FileHandler fh = new FileHandler(this);

        //verify files and directories exists
        this.txtLoading.setText("Verifying directories...");
        if (!fh.checkDirectories()) {
            this.txtLoading.setText("Could not verify directories.");
            this.destroy();
        }
        this.mProgressStatus = 25;
        this.mProgress.setProgress(this.mProgressStatus);

        //verify files exists
        this.txtLoading.setText("Verifying files...");
        if (!fh.checkFiles()) {

        }
        this.mProgressStatus = 50;
        this.mProgress.setProgress(this.mProgressStatus);

        //verify data is valid
        this.txtLoading.setText("Verifying data...");
        if (!fh.checkData()) {

        }
        this.mProgressStatus = 75;
        this.mProgress.setProgress(this.mProgressStatus);

        //read settings
        this.txtLoading.setText("Loading settings...");
        this.config = new Config();
        this.config.read();
        this.initialLoad = (config.getProperty("initial_load") == "true" ? true : false);
        this.mProgressStatus = 100;
        this.mProgress.setProgress(this.mProgressStatus);
    } //end of initialize()

    /**
     * Destroy the application and close properly
     */
    private void destroy() {
        //googles function to close applications properly
        Splash.this.finish();
    } //end of destroy()
}

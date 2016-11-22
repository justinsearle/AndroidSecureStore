package ca.justinsearle.securestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Justin on 11/17/2016.
 * This class handles all the functionality for the splash screen of secure store
 */

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private int mProgressStatus = 0;
    private boolean initialLoad = false;
    private boolean masterLogin = false;
    private ProgressBar mProgress;
    private TextView txtLoading;
    private TextView txtInformation;
    private Button btnNoMasterLogin;
    private Button btnYesMasterLogin;
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

        //find loading elements
        this.mProgress = (ProgressBar) findViewById(R.id.progressBarLoading);
        this.txtLoading = (TextView) findViewById(R.id.txtLoading);
        this.txtInformation = (TextView) findViewById(R.id.txtInformation);
        this.btnNoMasterLogin = (Button) findViewById(R.id.btnNoMasterLogin);
        this.btnYesMasterLogin = (Button) findViewById(R.id.btnYesMasterLogin);

        //start loading data
        Splash.this.initialize();

        //setup event listeners
        Splash.this.eventListeners();

        //decide path to be taken programmatically
        if (this.initialLoad) {
            //this is the initial load of the app
            this.mProgress.setVisibility(View.GONE);
            this.txtLoading.setText(getString(R.string.initial_load_header));
            this.btnNoMasterLogin.setVisibility(View.VISIBLE);
            this.btnYesMasterLogin.setVisibility(View.VISIBLE);
            this.txtInformation.setText(getString(R.string.initial_load_message));
            this.txtInformation.setVisibility(View.VISIBLE);
        } else if (this.masterLogin) {
            //master login screen
            Intent mainIntent = new Intent(Splash.this, Main2Activity.class);
            Splash.this.startActivity(mainIntent);
            this.destroy();
        } else {
            //right to main activity
            Intent mainIntent = new Intent(Splash.this, MainActivity.class);
            Splash.this.startActivity(mainIntent);
            this.destroy();
        }

//        /* New Handler to start the next activity
//         * and close this Splash-Screen after some seconds.*/
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, SPLASH_DISPLAY_LENGTH);
    }

    /**
     * Verify files, directores and data along with settings up anything needed for the splash screen
     */
    private void initialize() {
        Message.info("Starting program.");

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
            this.txtLoading.setText("Could not verify files.");
            this.destroy();
        }
        this.mProgressStatus = 50;
        this.mProgress.setProgress(this.mProgressStatus);

        //verify data is valid
        this.txtLoading.setText("Verifying data...");
        if (!fh.checkData()) {
            this.txtLoading.setText("Could not verify data.");
            this.destroy();
        }
        this.mProgressStatus = 75;
        this.mProgress.setProgress(this.mProgressStatus);

        //read settings
        this.txtLoading.setText("Loading settings...");
        this.config = new Config();
        this.config.read();
        this.initialLoad = (this.config.getProperty("initial_load").equals("true") ? true : false);
        this.masterLogin = (this.config.getProperty("master_login").equals("true") ? true : false);
        this.mProgressStatus = 100;
        this.mProgress.setProgress(this.mProgressStatus);
    } //end of initialize()

    /**
     *
     */
    public void eventListeners() {

        //check which event listeners to add
        if (this.initialLoad) {
            //initial load
//            this.mProgress.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //when play is clicked show stop button and hide play button
//                    playButton.setVisibility(View.GONE);
//                    stopButton.setVisibility(View.VISIBLE);
//                }
//            });
        }
    } //end of eventListeners

    /**
     * Destroy the application and close properly
     */
    private void destroy() {
        //googles function to close applications properly
        Splash.this.finish();
    } //end of destroy()
}

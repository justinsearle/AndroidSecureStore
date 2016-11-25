package ca.justinsearle.securestore;

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

    private final int SPLASH_DELAY_LENGTH = 2000;
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

        //find splash screen elements
        this.mProgress = (ProgressBar) findViewById(R.id.progressBarLoading);
        this.txtLoading = (TextView) findViewById(R.id.txtLoading);
        this.txtInformation = (TextView) findViewById(R.id.txtInformation);
        this.btnNoMasterLogin = (Button) findViewById(R.id.btnNoMasterLogin);
        this.btnYesMasterLogin = (Button) findViewById(R.id.btnYesMasterLogin);

        //set default visibilites
        this.txtInformation.setVisibility(View.GONE);
        this.btnNoMasterLogin.setVisibility(View.GONE);
        this.btnYesMasterLogin.setVisibility(View.GONE);

        //start loading bar
        Splash.this.setProgressBar(20);

        //start loading data after x amount of time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.initialize();
            }
        }, SPLASH_DELAY_LENGTH);
    }

    /**
     * Verify files, directores and data along with settings up anything needed for the splash screen
     */
    private void initialize() {
        Message.info("Starting program.");

        //start a new file handler class instance
        FileHandler fh = new FileHandler(this);
        fh.deleteAllFiles();

        //asking permission to store externally
        this.txtLoading.setText("Verifying permissions...");
        if (!fh.verifyStoragePermissions(this)) {
            this.txtLoading.setText("Could not verify directories.");
            this.destroy();
        }

        //verify files and directories exists
        this.txtLoading.setText("Verifying directories...");
        if (!fh.checkDirectories()) {
            this.txtLoading.setText("Could not verify directories.");
            this.destroy();
        }
        Splash.this.setProgressBar(40);

        //verify files exists
        this.txtLoading.setText("Verifying files...");
        if (!fh.checkFiles()) {
            this.txtLoading.setText("Could not verify files.");
            this.destroy();
        }
        Splash.this.setProgressBar(60);

        //verify data is valid
        this.txtLoading.setText("Verifying data...");
        if (!fh.checkData()) {
            this.txtLoading.setText("Could not verify data.");
            this.destroy();
        }
        Splash.this.setProgressBar(80);

        //read settings
        this.txtLoading.setText("Loading settings...");
        this.config = new Config(this);
        //this.config.build(true);
        this.config.read();
        config.viewProperties();
        this.initialLoad = (this.config.getProperty("initial_load").equals("true") ? true : false);
        this.masterLogin = (this.config.getProperty("master_login").equals("true") ? true : false);
        Splash.this.setProgressBar(100);

        //setup event listeners
        Splash.this.eventListeners();

        //decide path to be taken programmatically
        if (this.initialLoad) {
            //this is the initial load of the app
            this.txtLoading.setText(getString(R.string.initial_load_header));
            this.txtInformation.setText(getString(R.string.initial_load_message));
            this.btnNoMasterLogin.setVisibility(View.VISIBLE);
            this.btnYesMasterLogin.setVisibility(View.VISIBLE);
            this.txtInformation.setVisibility(View.VISIBLE);
        } else if (this.masterLogin) {
            //master login screen
            Intent mainIntent = new Intent(Splash.this, Login.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.destroy();
        } else {
            //right to main activity
            Intent mainIntent = new Intent(Splash.this, MainActivity.class);
            Splash.this.startActivity(mainIntent);
            this.destroy();
        }
    } //end of initialize()

    /**
     * Setup the splash screen event listeners
     */
    public void eventListeners() {
        //check which event listeners to add
        if (this.initialLoad) {
            /*
             * initial load event listeners
             */
            this.btnNoMasterLogin = (Button)findViewById(R.id.btnNoMasterLogin);
            this.btnNoMasterLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //user chose not setup a master account
                    Splash.this.config.setProperty("master_login", "false");
                    Splash.this.config.setProperty("initial_load", "false");
                    Splash.this.config.build(false);

                    //move to the main activity screen
                    Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.destroy();
                }
            });

            this.btnYesMasterLogin = (Button)findViewById(R.id.btnYesMasterLogin);
            this.btnYesMasterLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //user chose to setup a master password
                    Splash.this.config.setProperty("master_login", "true");
                    Splash.this.config.build(false);

                    //movie to the login screen
                    Intent mainIntent = new Intent(Splash.this, Login.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.destroy();
                }
            });
        }
    } //end of eventListeners

    /**
     * Update the progress par with a given number / 100
     * @param progressStatus
     */
    public void setProgressBar(int progressStatus) {
        this.mProgressStatus = progressStatus;
        this.mProgress.setProgress(this.mProgressStatus);
    } //end of setProgressBar()

    /**
     * Destroy the application and close properly
     */
    private void destroy() {
        Splash.this.finish();
    } //end of destroy()
}

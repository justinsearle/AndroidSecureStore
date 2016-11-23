package ca.justinsearle.securestore;

import android.content.Intent;
import android.sax.TextElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private boolean initialLoad = false;
    private Button btnLogin;
    private Config config;
    private EditText editLoginPass;
    private TextView txtLoginMessage;

    @Override
    /**
     * On creation of the login activity we will do the following:
     *  - Decide if this is the initial load and creating master account
     *  - or let the user attempt to login to the application
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //find all elements needed for the login activity
        this.txtLoginMessage = (TextView) findViewById(R.id.txtLoginMessage);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.editLoginPass = (EditText) findViewById(R.id.txtPass);

        //initialize the application
        this.initialize();
    } //end of onCreate()

    /**
     * initialize any needed aspects
     */
    private void initialize() {
        //read all settings needed for the login activity
        this.config = new Config();
        config.read();
        this.initialLoad = (config.getProperty("initial_load").equals("true") ? true : false);

        //if initial load,
        if (this.initialLoad) {
            this.txtLoginMessage.setText(getString(R.string.login_message_setup));
        } else {
            this.txtLoginMessage.setText(getString(R.string.login_message));
        }

        //setup event listeners
        this.eventListeners();
    } //end of initialize()

    /**
     * Setup and event listeners in the application
     */
    private void eventListeners() {
        /**
         * setup the login button
         */
        if (this.initialLoad) {
            //when initial load, we create the master account to user
            View.OnClickListener tryLogin = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EntryHandler entries = new EntryHandler();
                    String password = Login.this.editLoginPass.getText().toString();
                    Message.general("Password: " + password);

                    //verify that the given password is strong enough
                    if (!Security.verifyPassword(password)) {
                        Message.error("Could not verify password.");
                        Login.this.txtLoginMessage.setText(getString(R.string.login_password_not_strong_enough));
                    } else {
                        //setup main account
                        entries.setMaster(password);
                        Login.this.config.setProperty("initial_load", "false");
                        Login.this.config.build(false);

                        //move the entries activity
                        Intent mainIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(mainIntent);
                        Login.this.destroy();
                    }

                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            };
            this.btnLogin.setOnClickListener(tryLogin);
        } else {
            //otherwise here is where we let the user log in
            View.OnClickListener tryLogin = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onclick get the entered password
                    EntryHandler entries = new EntryHandler();
                    String password = Login.this.editLoginPass.getText().toString();
                    Message.general("Password: " + password);

                    entries.read();

                    if (entries.getMaster().equals(password)) {
                        Message.success("LOGGED IN");
                        //move the entries activity
                        Intent mainIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(mainIntent);
                        Login.this.destroy();
                    } else {
                        //failed to log in
                        Message.error("FAILED TO LOG IN");
                        Login.this.txtLoginMessage.setText(getString(R.string.login_password_wrong));
                    }
                }
            };
            this.btnLogin.setOnClickListener(tryLogin);
        }
    } //end of eventListeners()

    /**
     * Destroy the activity and close properly
     */
    private void destroy() {
        Login.this.finish();
    } //end of destroy()

} //end of Login class

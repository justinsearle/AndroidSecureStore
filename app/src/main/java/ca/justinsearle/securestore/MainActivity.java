package ca.justinsearle.securestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the application
        this.initialize();
    }

    /**
     * initialize any needed aspects
     */
    private void initialize() {
        Message.info("Starting program.");

        //start a new file handler class instance
        FileHandler fh = new FileHandler(this);

        //verify files and directories exists as well as verifying data is not corrupt or missing information
        if (!fh.verify()) {
            //something went wrong verifying
            Message.error("Closing application.");
            this.destroy();
        }

        //set up and event listeners needed for this activity
        this.eventListeners();

        //get configuration options
        Config config = new Config();
        config.read();
        config.viewProperties();
        //Message.success(config.getProperty("store_password_char_limit"));
    }

    /**
     * Setup and event listeners in the application
     */
    private void eventListeners() {
        /**
         * setup the login button
         */
        this.btnLogin = (Button)findViewById(R.id.btnLogin);
        View.OnClickListener tryLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onclick get the entered username and password
                EditText editUser = (EditText)findViewById(R.id.txtUser);
                EditText editPass = (EditText)findViewById(R.id.txtPass);

                String username = editUser.getText().toString();
                String password = editPass.getText().toString();

                Message.general("You clicked me " + username + ", " + password);

                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            }
        };
        this.btnLogin.setOnClickListener(tryLogin);

    } //end of eventListeners()

    /**
     * Destroy the application and close properly
     */
    private void destroy() {
        //googles function to close applications properly
        //super.finish();

    } //end of destroy()

} //end of MainActivity class

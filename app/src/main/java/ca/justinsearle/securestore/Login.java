package ca.justinsearle.securestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

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
        //set up and event listeners needed for this activity
        this.eventListeners();
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

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

} //end of Login class

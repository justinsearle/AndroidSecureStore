package ca.justinsearle.securestore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Admin on 11/30/2016.
 */

public class AddEntryActivity extends AppCompatActivity {
    @Override
    /**
     * On creation of the login activity we will do the following:
     *  - Decide if this is the initial load and creating master account
     *  - or let the user attempt to login to the application
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry);

        EntryHandler entryHandler = new EntryHandler(this);
        entryHandler.newEntry("test", "test", "Test");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AddEntryActivity.this.finish();
            }
        }, 3000);
    } //end of onCreate()
}

package ca.justinsearle.securestore;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EntryHandler entryHandler = new EntryHandler(this);
        entryHandler.read();
        //entryHandler.newEntry("Test account", "fakepass", "Fake test account");

        String[] cars = {"dodge", "ford", "nissan"};
        ListAdapter adapter = new EntryAdapter(this, entryHandler.getEntries());
        ListView listViewEntries = (ListView)findViewById(R.id.listViewEntries);
        listViewEntries.setAdapter(adapter);

        //onclick listener
        listViewEntries.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }
        );

        Security sec = new Security(this);
    }
}

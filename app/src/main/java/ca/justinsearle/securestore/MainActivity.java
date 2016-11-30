package ca.justinsearle.securestore;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public ListAdapter adapter;
    public EntryHandler entryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.entryHandler = new EntryHandler(this);
        this.entryHandler.read();
        //entryHandler.newEntry("Test account", "fakepass", "Fake test account");

        String[] cars = {"dodge", "ford", "nissan"};
        this.adapter = new EntryAdapter(this, this.entryHandler.getEntries());

        ListView listViewEntries = (ListView)findViewById(R.id.listViewEntries);
        listViewEntries.setAdapter(this.adapter);

        //onclick listener
//        listViewEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        //onclick listener for the fab button
        FloatingActionButton fabAddEntry = (FloatingActionButton)findViewById(R.id.fabAddEntry);
        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.success("clicked----");
                //Intent addEntryIntent = new Intent(MainActivity.this, AddEntryActivity.class);
                //startActivity(addEntryIntent);
                MainActivity.this.entryHandler.newEntry("Test account", "fakepass", "Fake test account");
                ((BaseAdapter)MainActivity.this.adapter).notifyDataSetChanged();
            }
        });

        Security sec = new Security(this);
    }
}

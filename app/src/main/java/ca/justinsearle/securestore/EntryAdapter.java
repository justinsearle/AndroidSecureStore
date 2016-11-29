package ca.justinsearle.securestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Justin on 11/23/2016.
 */

class EntryAdapter extends ArrayAdapter<Entry> {


    EntryAdapter(Context context, ArrayList entries) {
        super(context, R.layout.entry, entries);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listViewEntries = LayoutInflater.from(getContext());
        View customView = listViewEntries.inflate(R.layout.entry, parent, false);

        Entry entry = getItem(position);
        TextView txtName = (TextView) customView.findViewById(R.id.txtEntryName);
        TextView txtDescription = (TextView) customView.findViewById(R.id.txtEntryDescription);
        TextView txtID = (TextView) customView.findViewById(R.id.txtID);

        txtName.setText(entry.getName());
        txtDescription.setText(entry.getDescription());
        txtID.setText(Integer.toString(entry.getId()));

        return customView;
    }
}

package ca.justinsearle.securestore;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/15/2016.
 */

public class EntryHandler extends FileHandler {

    private int totalEntries = 0;
    private boolean hasRead = false;
    private ArrayList<Entry> entries = new ArrayList<Entry>();

    /**
     * Basic constructor
     */
    public EntryHandler() {

    } //end of constructor

    /**
     * Basic constructor
     * @param context
     */
    public EntryHandler(Context context) {
        super(context);
    } //end of constructor

    /**
     * Read from the entry file using file handler then process information
     * @return
     */
    public boolean read() {
        Message.debug("EntryHandler class attempting to load entry file.");
        boolean readOkay = false;

        this.entries = super.getEntryFile();

        //send status to console
        if (readOkay) Message.success("EntryHandler file loaded correctly!");
        else Message.warning("EntryHandler file may have not loaded correctly");
        return readOkay;
    } //end of read()

    /**
     * View all the entries
     */
    public void viewEntries() {
        Message.info("Reading " + this.entries.size() + " entries.");

        for (int i = 0; i < this.entries.size(); i++) {
            Message.general(this.entries.get(i).getName());
        }
    } //end of viewEntries()

    /**
     * Get all the entries
     */
    public ArrayList<Entry> getEntries() {
        return this.entries;
    } //end of viewEntries()

    public void newEntry (String entryName, String entryPassword, String entryDescription) {
        //create new entry
        Entry entry = new Entry();
        entry.setup(nextId(), entryName, entryPassword, entryDescription);

        //add entry to list and update file
        this.entries.add(entry);
        super.setEntryFile(this.entries);
    }

    public String getMasterPassword() {
        for (int i = 0; i < this.entries.size(); i++) {
            if (this.entries.get(i).getId() == Entry.getMasterId()) {
                return this.entries.get(i).getPassword();
            }
        }
        return "";
    }

    public int nextId() {
        int nextInt = 1;
        List idList = new ArrayList<Integer>();

        for (Entry entry : this.entries) {
            int tempId = entry.getId();
            idList.add(tempId);

            if (tempId == nextInt) {
                nextInt++;
            }
        }

        return nextInt;
    }

    /**
     * Set the master password
     * @param password
     */
    protected void setMaster(String password) {
        //setup master account
        Entry newEntry = new Entry();
        newEntry.setup(Entry.getMasterId(), "Master Account", password, "This is your login information for the application");

        //save file
        this.entries.add(newEntry);
        super.setEntryFile(this.entries);
    } //end of setMaster()

} //end of EntryHandler class

package ca.justinsearle.securestore;

import android.content.Context;

/**
 * Created by Admin on 11/15/2016.
 */

public class EntryHandler extends FileHandler {

    private int totalEntries;


    public EntryHandler() {

    } //end of constructor

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



        //send status to console
        if (readOkay) Message.success("EntryHandler file loaded correctly!");
        else Message.warning("EntryHandler file may have not loaded correctly");
        return readOkay;
    } //end of read()

} //end of EntryHandler class

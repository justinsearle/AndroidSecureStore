package ca.justinsearle.securestore;

import android.content.Context;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 * Created by Admin on 11/15/2016.
 */

public class FileHandler {

    private static Context context;
    private final String[] directories = {
            "backup/",
            "src/"
    };
    private final String[] files = {
            "src/entries.dat",
            "src/config.properties",
            "src/log.txt"
    };

    /**
     *
     */
    public FileHandler() {

    }

    /**
     * constructor override with context
     */
    public FileHandler(Context context) {
        this.context = context;
    } //end of constructor

    /**
     * Verify that all required information, files and directories exists
     * @return
     */
    protected boolean verify() {
        Message.info("File handler attempting to verify files, directories and data.");
        boolean verified = true;

        //check required directories and files exist as well as verifying data integrity
        if (!checkDirectories() && verified) {
            verified = false;
            Message.warning("Files handler could not verify directories.");
        }
        if (!checkFiles() && verified) {
            verified = false;
            Message.warning("Files handler could not verify files.");
        }
        if (!checkData() && verified) {
            verified = false;
            Message.warning("Files handler could not verify data.");
        }

        return verified;
    } //end of verify()

    /**
     * check that all needed directories exist
     */
    private boolean checkDirectories() {
        Message.debug("Attemtping to verify directories.");
        boolean verified = true;

        try {
            //loop through needed directories
            for (String dir : this.directories) {
                File directory = new File(this.context.getFilesDir(), dir);

                //check if directory exists
                if (!directory.exists()) {
                    //attempt to create directory
                    if (directory.mkdir()) {
                        Message.success("Created directory \"" + directory.getAbsolutePath() + "\".");
                    } else {
                        Message.error("Failed to create directory \"" + directory.getAbsolutePath() + "\".");
                        verified = false;
                    }
                }

            } //end of loop
        } catch (Exception e) {
            Message.exception(e.getMessage());
            e.printStackTrace();
        }

        //show status of method to console
        if (verified) Message.debug("Verified directories.");
        else Message.debug("Could not verify directories.");
        return verified;
    } //end of checkDirectories()

    /**
     * check that all required files exist
     */
    private boolean checkFiles() {
        Message.debug("Attemping to verify files.");
        boolean verified = true;

        try {
            //loop through needed files
            for (String filePath : this.files) {
                File file = new File(this.context.getFilesDir(), filePath);

                //check if the current file exists
                if (!file.exists()) {
                    //test to see if file was created successfully
                    if (file.createNewFile()) {
                        Message.success("File created: \"" + file.getAbsolutePath() +"\"");
                    } else {
                        Message.error("Failed to create file: \"" + file.getAbsolutePath() +"\"");
                        verified = false;
                    }
                }
            } //end of loop
        } catch (Exception e) {
            Message.exception(e.getMessage());
            e.printStackTrace();
        }

        //show status of method to console
        if (verified) Message.debug("Verified files.");
        else Message.debug("Could not verify files.");
        return verified;
    } //end of checkFiles()

    /**
     * check the data is intact and not corrupted
     */
    private boolean checkData() {
        Message.debug("Attemping to verify data.");
        boolean verified = true;

        //open config file
        Config config = new Config();

        //attempt to read values
        if (!config.read()) {
            //if we cannot read attempt to restore from a backup
//            if (!config.restore()) {
//                //cannot restore, rebuild config file
//                config.build(true);
//            }
        }

        //open entries file
        EntryHandler entryHandler = new EntryHandler();

        //attempt to read values
        if (!entryHandler.read()) {

        }

        //show status of method to console
        if (verified) Message.debug("Verified data.");
        else Message.debug("Could not verify data.");
        return verified;
    } //end of checkData()

    /**
     *
     * @param filename
     */
    protected void write(String filename, String output, boolean overwrite) {
        //check to overwrite or append data
        if (overwrite) {

        } else {

        }
    }

    /**
     * Set the configuration settings to a file
     * @param props
     * @param comment
     */
    protected boolean setConfig(Properties props, String comment) {
        Message.debug("File handler attempting to save the config file.");
        boolean saved = false;

        //reference the config file
        File config = new File(this.context.getFilesDir(), "src/config.properties");

        try {
            FileWriter writer = new FileWriter(config);
            props.store(writer, comment);
            writer.close();
            saved = true;
        } catch (Exception e) {
            //catch exception
            Message.exception(e.getMessage());
            e.printStackTrace();
        }

        //show status of method to console
        if (saved) Message.debug("File handler saved the config file.");
        else Message.debug("File handler could not save the config file.");
        return saved;
    } //end of setConfig()

    /**
     * Read the current configuration settings from a file
     * @return Properties
     */
    protected Properties getConfig() {
        Message.debug("Attempting to read from the config file.");

        //reference the config file
        File config = new File(this.context.getFilesDir(), "src/config.properties");
        Properties props = new Properties();

        try {
            //attempt to load properties
            FileReader reader = new FileReader(config);
            props.load(reader);
            reader.close();
            Message.success("File handler successfully read from the config file.");
        } catch (Exception e) {
            //catch exceptions
            Message.exception(e.getMessage());
            e.printStackTrace();
            Message.error("File handler failed to read from the config file.");
        }

        return props;
    } //end of getConfig()

    /**
     * Read the current configuration settings from a file
     * @return Properties
     */
    protected Properties getEntryFile() {
        Message.debug("Attempting to read from the config file.");

        //reference the entries file
        File config = new File(this.context.getFilesDir(), "src/config.properties");
        Properties props = new Properties();

        try {
            //attempt to load properties
            FileReader reader = new FileReader(config);
            props.load(reader);
            reader.close();
            Message.success("File handler successfully read from the config file.");
        } catch (Exception e) {
            //catch exceptions
            Message.exception(e.getMessage());
            e.printStackTrace();
            Message.error("File handler failed to read from the config file.");
        }

        return props;
    } //end of getEntryFile()

} //end of FileHandler class

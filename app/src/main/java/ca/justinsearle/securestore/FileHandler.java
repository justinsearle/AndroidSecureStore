package ca.justinsearle.securestore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Justin on 11/15/2016.
 *
 * This class handles all the input and output of any data that the
 * application needs or will need.
 */
class FileHandler {
    /**
     * if saveToPrivate = true, the application will save to private storage
     * else the application will save to sd card, mostly used for debugging
     */
    public static final boolean SAVE_TO_PRIVATE = false;

    /**
     * all the directories and files needed for the application,
     * just add to this array of strings and it will create on load of the app
     */
    private static final String[] directories = {
            "/SecureStore/",
            "/SecureStore/backup/",
            "/SecureStore/src/"
    };
    private static final String[] files = {
            "/SecureStore/src/entries.dat",
            "/SecureStore/src/config.properties",
            "/SecureStore/src/log.txt"
    };

    //store a reference of the context
    private Context context;

    /**
     * Constructor
     */
    public FileHandler() {}

    /**
     * Constructor override
     * @param context
     */
    public FileHandler(Context context) {
        this.context = context;
    }

    /**
     * check that all needed directories exist
     */
    protected boolean checkDirectories() {
        int directoriesExist = 0;

        Message.debug("Attempting to verify directories.");
        try {
            //loop through needed directories
            for (String dir : this.directories) {
                //create reference
                File directory;
                if (SAVE_TO_PRIVATE) {
                    directory = new File(this.context.getFilesDir(), dir);
                } else {
                    directory = new File(Environment.getExternalStorageDirectory(), dir);
                }

                //check if directory exists
                if (!directory.exists()) {
                    //attempt to create directory
                    if (directory.mkdirs()) {
                        Message.success("Created directory \"" + directory.getAbsolutePath() + "\".");
                        directoriesExist++;
                    } else {
                        Message.error("Failed to create directory \"" + directory.getAbsolutePath() + "\".");
                    }
                } else {
                    directoriesExist++;
                }

            } //end of loop
        } catch (Exception e) {
            Message.exception(e.getMessage());
            e.printStackTrace();
        }

        //show status of method to console
        if (directoriesExist == this.directories.length) {
            Message.debug("Verified directories.");
            return true;
        } else {
            Message.debug("Could not verify directories.");
            return false;
        }
    } //end of checkDirectories()

    /**
     * check that all required files exist
     */
    protected boolean checkFiles() {
        int filesExist = 0;

        Message.debug("Attempting to verify files.");
        try {
            //loop through needed files
            for (String filePath : this.files) {
                //create reference
                File file;
                if (SAVE_TO_PRIVATE) {
                    file = new File(this.context.getFilesDir(), filePath);
                } else {
                    file = new File(Environment.getExternalStorageDirectory(), filePath);
                }

                //check if the current file exists
                if (!file.exists()) {
                    //test to see if file was created successfully
                    if (file.createNewFile()) {
                        Message.success("File created: \"" + file.getAbsolutePath() +"\"");
                        filesExist++;
                    } else {
                        Message.error("Failed to create file: \"" + file.getAbsolutePath() +"\"");
                    }
                } else {
                    filesExist++;
                }
            } //end of loop
        } catch (Exception e) {
            Message.exception(e.getMessage());
            e.printStackTrace();
        }

        //show status of method to console
        if (filesExist == this.files.length) {
            Message.debug("Verified files.");
            return true;
        } else {
            Message.debug("Could not verify files.");
            return false;
        }
    } //end of checkFiles()

    /**
     * check the data is intact and not corrupted
     */
    protected boolean checkData() {
        Message.debug("Attemping to verify data.");
        boolean verified = true;

        //open config file
        Config config = new Config();

        //attempt to read values
        if (!config.read()) {
            //if we cannot read attempt to restore from a backup
            if (!config.restore()) {
                //cannot restore, rebuild config file
                config.build(true);
            }
        }

        //open entries file
        //EntryHandler entryHandler = new EntryHandler();

        //attempt to read values
        //if (!entryHandler.read()) {

        //}

        //show status of method to console
        if (verified) Message.debug("Verified data.");
        else Message.debug("Could not verify data.");
        return verified;
    } //end of checkData()

    /**
     * Set the configuration settings to a file
     * @param props
     * @param comment
     */
    protected boolean setConfig(Properties props, String comment) {
        boolean saved = false;
        final String configFilePath = "/SecureStore/src/config.properties";

        Message.debug("File handler attempting to save the config file.");
        try {
            //reference the config file
            File configFile;
            if (SAVE_TO_PRIVATE) {
                configFile = new File(this.context.getFilesDir(), configFilePath);
            } else {
                configFile = new File(Environment.getExternalStorageDirectory(), configFilePath);
            }

            FileWriter writer = new FileWriter(configFile);
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
        Properties props = new Properties();
        final String configFilePath = "/SecureStore/src/config.properties";

        Message.debug("Attempting to read from the config file.");
        try {
            //reference the config file
            File configFile;
            if (SAVE_TO_PRIVATE) {
                configFile = new File(this.context.getFilesDir(), configFilePath);
            } else {
                configFile = new File(Environment.getExternalStorageDirectory(), configFilePath);
            }

            //attempt to load properties
            FileReader reader = new FileReader(configFile);
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
    protected ArrayList<Entry> getEntryFile() {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        final String entryFilePath = "/SecureStore/src/entries.dat";

        Message.debug("Attempting to read from the config file.");
        try {
            //reference entries file
            File entryFile;
            if (SAVE_TO_PRIVATE) {
                entryFile = new File (this.context.getFilesDir(), entryFilePath);
            } else {
                entryFile = new File (Environment.getExternalStorageDirectory(), entryFilePath);
            }

            //attempt to load properties
            FileInputStream fis = new FileInputStream(entryFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            entries = (ArrayList<Entry>)ois.readObject();
            ois.close();
            fis.close();
            Message.success("File handler successfully read from the config file.");
        } catch (Exception e) {
            //catch exceptions
            Message.exception(e.getMessage());
            e.printStackTrace();
            Message.error("File handler failed to read from the config file.");
        }

        return entries;
    } //end of getEntryFile()

    protected static void writeToLog(String msg) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "/SecureStore/src/log.txt");
            FileWriter writer = new FileWriter(root);
            writer.append(msg);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the current configuration settings from a file
     * @return Properties
     */
    protected boolean setEntryFile(ArrayList<Entry> entries) {
        boolean saved = false;
        final String entryFilePath = "/SecureStore/src/entries.dat";

        Message.debug("Attempting to read from the config file.");
        try {
            //reference entries file
            File entryFile;
            if (SAVE_TO_PRIVATE) {
                entryFile = new File(this.context.getFilesDir(), entryFilePath);
            } else {
                entryFile = new File(Environment.getExternalStorageDirectory(), entryFilePath);
            }

            //attempt to load properties
            FileOutputStream fos = new FileOutputStream(entryFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entries);
            oos.close();
            fos.close();
            Message.success("File handler successfully read from the config file.");
            saved = true;
        } catch (Exception e) {
            //catch exceptions
            Message.exception(e.getMessage());
            e.printStackTrace();
            Message.error("File handler failed to read from the config file.");
            saved = false;
        }

        return saved;
    }

    /**
     * Deletes any files and directories created by the application
     */
    private void deleteAllFiles() {
        //loop through and delete all files
        for (String file : this.files) {
            Message.debug("Deleting file: " + file);

            //create reference to private storage
            File privateFile = new File(this.context.getFilesDir(), file);
            if (privateFile.exists()) privateFile.delete();

            //create reference to public storage
            File publicFile = new File(Environment.getExternalStorageDirectory(), file);
            if (publicFile.exists()) publicFile.delete();
        }

        //loop through and delete all directories
        for (String dir : this.directories) {
            Message.debug("Deleting directory: " + dir);

            //create reference to private storage
            File privateDirectory = new File(this.context.getFilesDir(), dir);
            if (privateDirectory.exists()) privateDirectory.delete();

            //create reference to public storage
            File publicDirectory = new File(Environment.getExternalStorageDirectory(), dir);
            if (publicDirectory.exists()) publicDirectory.delete();
        }
    }

} //end of FileHandler class

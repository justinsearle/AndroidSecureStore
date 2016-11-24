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
    private static final boolean saveToPrivate = false;

    /**
     * all the directories and files needed for the application,
     * just add to this array of strings and it will create on load of the app
     */
    private final String[] directories = {
            "backup/",
            "src/"
    };
    private final String[] files = {
            "src/entries.dat",
            "src/config.properties",
            "src/log.txt"
    };

    private Context context;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Constructors
     */
    public FileHandler() {
    }

    public FileHandler(Context context) {
        this.context = context;
    } //end of constructor

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * check that all needed directories exist
     */
    protected boolean checkDirectories() {
        boolean verified = true;

        Message.debug("Attempting to verify directories.");
        try {
            //loop through needed directories
            for (String dir : this.directories) {
                //create reference
                File directory;
                if (this.saveToPrivate) {
                    directory = new File(this.context.getFilesDir(), dir);
                } else {
                    directory = new File(Environment.getExternalStorageDirectory(), dir);
                }

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
            verified = false;
        }

        //show status of method to console
        if (verified) Message.debug("Verified directories.");
        else Message.debug("Could not verify directories.");
        return verified;
    } //end of checkDirectories()

    /**
     * check that all required files exist
     */
    protected boolean checkFiles() {
        boolean verified = true;

        Message.debug("Attemping to verify files.");
        try {
            //loop through needed files
            for (String filePath : this.files) {
                //create reference
                File file;
                if (this.saveToPrivate) {
                    file = new File(this.context.getFilesDir(), filePath);
                } else {
                    file = new File(Environment.getExternalStorageDirectory(), filePath);
                }

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
            verified = false;
        }

        //show status of method to console
        if (verified) Message.debug("Verified files.");
        else Message.debug("Could not verify files.");
        return verified;
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
//            if (!config.restore()) {
//                //cannot restore, rebuild config file
                config.build(true);
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
     * Set the configuration settings to a file
     * @param props
     * @param comment
     */
    protected boolean setConfig(Properties props, String comment) {
        boolean saved = false;

        Message.debug("File handler attempting to save the config file.");
        try {
            //reference the config file
            File config;
            if (this.saveToPrivate) {
                config = new File(this.context.getFilesDir(), "src/config.properties");
            } else {
                config = new File(Environment.getExternalStorageDirectory(), "src/config.properties");
            }

            FileWriter writer = new FileWriter(config);
            props.store(writer, comment);
            writer.close();
            saved = true;
        } catch (Exception e) {
            //catch exception
            Message.exception(e.getMessage());
            e.printStackTrace();
            saved = false;
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

        Message.debug("Attempting to read from the config file.");
        try {
            //reference the config file
            File config;
            if (this.saveToPrivate) {
                config = new File(this.context.getFilesDir(), "src/config.properties");
            } else {
                config = new File(Environment.getExternalStorageDirectory(), "src/config.properties");
            }

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
    protected ArrayList<Entry> getEntryFile() {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        Message.debug("Attempting to read from the config file.");
        try {
            //reference entries file
            File entryFile;
            if (this.saveToPrivate) {
                entryFile = new File (this.context.getFilesDir(), "src/entries.dat");
            } else {
                entryFile = new File (Environment.getExternalStorageDirectory(), "src/entries.dat");
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

    /**
     * Read the current configuration settings from a file
     * @return Properties
     */
    protected boolean setEntryFile(ArrayList<Entry> entries) {
        boolean saved = false;

        Message.debug("Attempting to read from the config file.");
        try {
            //reference entries file
            File entryFile;
            if (this.saveToPrivate) {
                entryFile = new File(this.context.getFilesDir(), "src/entries.dat");
            } else {
                entryFile = new File(Environment.getExternalStorageDirectory(), "src/entries.dat");
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
    } //end of getEntryFile()

} //end of FileHandler class

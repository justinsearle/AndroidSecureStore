package ca.justinsearle.securestore;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Admin on 11/15/2016.
 */

public class Config extends FileHandler {

    public Properties props = new Properties();
    private List<ArrayPair> defaultConfigurations;
    private boolean hasRead = false;

    /**
     * Basic constructor
     */
    public Config() {
        loadDefaults();
    } //end of Constructor

    /**
     * constructor with context
     * @param context
     */
    public Config(Context context) {
        //load parent with context if needed
        super(context);

        loadDefaults();
    } //end of constructor

    /**
     * Load the default settings
     */
    private void loadDefaults() {
        //load default configurations
        List<ArrayPair> configurations = new ArrayList<ArrayPair>();
        configurations.add(new ArrayPair("initial_load", "true", "boolean"));
        configurations.add(new ArrayPair("master_login", "false", "boolean"));
        configurations.add(new ArrayPair("login_attempts", "5", "int"));
        configurations.add(new ArrayPair("delete_once_locked", "false", "boolean"));
        configurations.add(new ArrayPair("store_passwords", "false", "boolean"));
        configurations.add(new ArrayPair("store_password_length", "false", "boolean"));
        configurations.add(new ArrayPair("email_me_on_failed_attempts", "false", "boolean"));

        this.defaultConfigurations = configurations;
    } //end of loadDefaults()

    /**
     * Get the properties from the config file and load into memory
     * @return
     */
    public boolean read() {
        Message.debug("Config class attempting to process properties.");
        this.hasRead = true;

        boolean readOkay = true;

        //get file configurations
        Properties fileProps = super.getConfig();

        //loop through file properties and verify with defaults
        for (int i = 0; i < this.defaultConfigurations.size(); i++) {
            //get current loop values
            String key = this.defaultConfigurations.get(i).key;
            String defaultValue = this.defaultConfigurations.get(i).svalue;
            String fileValue = fileProps.getProperty( key );
            //String type = this.defaultConfigurations.get(i).type;

            //test property to see if file contains default key and value
            if (fileValue != null) {
                //property found, verify that we have valid data
                this.props.setProperty( key, fileValue );
            } else {
                //property not found, load default property
                Message.warning("Property: " + key + " was not found in the loaded config file.");
                this.props.setProperty(key, defaultValue);
                readOkay = false;
            }
        } //end of loop

        if (readOkay) Message.debug("Config file loaded correctly!");
        else Message.debug("Config file may have not loaded correctly");
        return readOkay;
    } //end of read()

    /**
     * List all the properties in a given property object
     */
    public void viewProperties() {
        //send all the properties to the console
        Message.general("Listing all properties in the info file\n---------------------------------------------");
        this.props.list(System.out);
        Message.general("---------------------------------------------");
    } //end of viewProperties()

    /**
     * Restore the config file from a previous backup if at all possible
     * @return
     */
    public boolean restore() {
        Message.warning("Attempting to restore config file.");
        boolean restored = false;

        if (!restored) Message.error("Failed to restore config file.");
        return restored;
    } //end of restore()

    /**
     * Build or rebuild the apps configuration file
     * @param rebuild
     */
    protected void build(boolean rebuild) {
        String comment = "";

        if (rebuild) {
            comment = "Rebuilding the applications configuration file.";
            Message.warning(comment);

            //load default properties into temp property object
            Properties tempProps = new Properties();
            for (int i = 0; i < this.defaultConfigurations.size(); i++) {
                tempProps.setProperty( this.defaultConfigurations.get(i).key, this.defaultConfigurations.get(i).svalue );
            }

            //save file
            super.setConfig(tempProps, comment);
        } else {
            comment = "Building the applications configuration file.";
            Message.info(comment);

            //save file
            super.setConfig(this.props, comment);
        }
    } //end of build()

    /**
     * Return a property from a given key
     * @param key
     * @return
     */
    protected String getProperty(String key) {
        if (!this.hasRead) {
            Message.warning("You have to call read() before trying to read properties.");
        } else if (this.props.getProperty(key) != null) {
            return this.props.getProperty(key);
        } else {
            Message.warning("Property: \'" + key + "\' not found");
            return "Property: \'" + key + "\' not found";
        }
        return "";
    } //end of getProperty()

    /**
     * Set a property from a given key and value
     * @param key
     * @return
     */
    protected void setProperty(String key, String value) {

        this.props.setProperty(key, value);
    } //end of getProperty()
} //end of Config class

package ca.justinsearle.securestore;

/**
 * Created by Admin on 11/17/2016.
 */

public class Entry implements java.io.Serializable {

    //Constants to map to properties
    private static final int MASTER_ID = 999;
    private static final int ENTRY_ID = 1;
    private static final int ENTRY_NAME = 2;
    private static final int ENTRY_DESCRIPTION = 3;
    private static final int ENTRY_USERNAME = 4;
    private static final int ENTRY_PASSWORD = 5;
    private static final long serialVersionUID = -2167785672176537950L;

    //Entry main properties
    private int entryID;
    private int passwordLength;
    private String entryName;
    private String entryDescription;
    private String username;
    private String password;
    private String email;
    private String fName;
    private String lName;
    private String security1;
    private String security2;
    private String other1;
    private String other2;
    private String other3;
    private String other4;
    private boolean isMaster = false;
    private boolean storePassword;
    private boolean storePasswordLength;

    public Entry() {

    }

    protected String getProperty (int asd) {

        return "";
    }

    public String getName() {
        return this.entryName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setup(int id, String name, String password, String description) {
        if (id == this.MASTER_ID) {
            this.isMaster = true;
        }
        this.entryID = id;
        this.entryName = name;
        this.password = password;
        this.entryDescription = description;
    }

    public void getConstant(String master) {

    }

    public String getDescription() { return this.entryDescription; }

    public static int getMasterId() {
        return MASTER_ID;
    }

    public int getId() {
        return this.entryID;
    }
}

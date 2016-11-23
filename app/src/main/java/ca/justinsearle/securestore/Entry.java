package ca.justinsearle.securestore;

/**
 * Created by Admin on 11/17/2016.
 */

public class Entry implements java.io.Serializable {

    private static final int masterId = 999;
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

    public String getName() {
        return this.entryName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setup(int id, String name, String password, String description) {
        if (id == this.masterId) {
            this.isMaster = true;
            this.entryID = this.masterId;
        } else {
            this.entryID = id;
        }
        this.entryName = name;
        this.password = password;
        this.entryDescription = description;
    }

    public void getConstant(String master) {

    }

    public static int getMasterId() {
        return masterId;
    }

    public int getId() {
        return this.entryID;
    }
}

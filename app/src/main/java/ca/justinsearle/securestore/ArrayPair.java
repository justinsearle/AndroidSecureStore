package ca.justinsearle.securestore;

/**
 * Created by Admin on 11/15/2016.
 */

public class ArrayPair {

    public String key;
    public String type;
    public String svalue;
    public int ivalue;

    public ArrayPair(String key, String value, String type){
        this.key = key;
        this.svalue = value;
        this.type = type;
    }
}

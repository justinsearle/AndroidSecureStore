package ca.justinsearle.securestore;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Admin on 11/15/2016.
 */

class Message {

    private static boolean backlogMessages;
    private static boolean logMessages = true;
    private static boolean generalMessages = true;
    private static boolean infoMessages = true;
    private static boolean warningMessages = true;
    private static boolean successMessages = true;
    private static boolean errorMessages = true;
    private static boolean exceptionMessages = true;
    private static boolean debugMessages = true;
    private static boolean toastMessages = true;

    private Context context;

    /**
     * basic constructor
     */
    public Message() {

    } //end of constructor

    /**
     * constructor with context
     */
    public Message(Context context) {
        this.context = context;
    } //end of constructor

    /**
     * Set the objects backlog policy
     * If true, messages will be stored locally
     * If false, message will not be stored
     * @param backlog
     */
    protected void setBacklog (boolean backlog) {
        this.backlogMessages = backlog;
    } //end of setBacklog()

    /**
     * Set the objects backlog policy
     * If true, messages will be logged to a file
     * If false, message will not be logged
     * @param log
     */
    protected void setLog (boolean log) {
        this.logMessages = log;
    } //end of setLog

    /**
     * Log a message to the log file
     * @param msg
     */
    public void log (String msg) {
        if (this.logMessages) {

        }
    } //end of log()

    /**
     * Add a message to the backlog
     * @param msg
     */
    public void backlog (String msg) {
        if (this.backlogMessages) {

        }
    } //end of backlog()

    /**
     * Send a general system out message to the console
     * @param msg
     */
    public static void general(String msg) {
        if (generalMessages) {
            System.out.println(msg);
        }
    } //end of general()

    /**
     * Send a informational message to the console
     * @param msg
     */
    public static void info(String msg) {
        if (infoMessages) {
            System.out.println("--INFO-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of info()

    /**
     * Send a error message to the console
     * @param msg
     */
    public static void error(String msg) {
        if (errorMessages) {
            System.out.println("--ERROR-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of error()

    /**
     * Send a success message to the console
     * @param msg
     */
    public static void success(String msg) {
        if (successMessages) {
            System.out.println("--SUCCESS-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of success()

    /**
     * Send a warning message to the console
     * @param msg
     */
    public static void warning(String msg) {
        if (warningMessages) {
            System.out.println("--WARNING-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of warning()

    /**
     * Send a exception message to the console
     * @param msg
     */
    public static void exception(String msg) {
        if (exceptionMessages) {
            System.out.println("--CAUGHT EXCEPTION-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of warning()

    /**
     * Send a exception message to the console
     * @param msg
     */
    public static void debug(String msg) {
        if (debugMessages) {
            System.out.println("--DEBUG-- "+ msg);
            if (logMessages) {
                FileHandler.writeToLog(msg);
            }
        }
    } //end of warning()

    /**
     * Send a exception message to the console
     * @param msg
     */
    public void toast(String msg) {
        if (toastMessages) {
            System.out.println("--TOAST-- "+ msg);
            Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show();
        }
    } //end of warning()

} //end of Message class

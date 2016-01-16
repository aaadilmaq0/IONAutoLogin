package login.kunal.com.loginautomatically.Functions;

import android.content.Context;
import android.util.Log;
import android.widget.TextClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MAHE on 11/10/2015.
 */
public class Logger {
    private static Logger instance = null;
    public static List<String> logs;
    int size = 0;

    protected Logger() {
        logs = new ArrayList<String>();
        size++;
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }


    public int getSize() {
        return size;
    }

    public void addToLog(String text) {

        Log.d("added", text);

        logs.add(getCurrentTime() + ": " + text);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM hh-mm-ss aa");
        return sdf.format(new Date());

    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (String s : logs) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}

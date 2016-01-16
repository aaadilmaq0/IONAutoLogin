package login.kunal.com.loginautomatically.Functions;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kunall17 on 12/24/15.
 */
public class SharedPreferencesClass {
    String defaultID;
    private static SharedPreferencesClass spc;
    public static final String SP_PATH = "settings_sp";
    public static final String SP_DEFAULT = "default_id";
    public static final String SP_MINUTES = "minutes";
    public static final String SP_autocheck = "autocheck_boolean";


    private static SharedPreferences sp;
    Logger log;

    private SharedPreferencesClass(Context context) {
        sp = context.getSharedPreferences(SP_PATH, context.MODE_PRIVATE);
        log = Logger.getInstance();

        defaultID = sp.getString(SP_DEFAULT, "");
    }

    public Boolean getAutoCheckBoolean() {
        return sp.getBoolean(SP_autocheck, false);
    }

    public static SharedPreferencesClass getInstance(Context context) {
        if (spc == null) {
            spc = new SharedPreferencesClass(context);
        }
        return spc;
    }

    public void saveDefaultID(String defaultUsername) {
        SharedPreferences.Editor spe = sp.edit();
        log.addToLog("DEFAULT ID Saved");
        spe.putString(SP_DEFAULT, defaultUsername);
        spe.commit();
        spe.apply();
    }

    public String getDefaultID() {
        return sp.getString(SP_DEFAULT, "");
    }

    public Boolean ifExists(String keyname) {
        return sp.contains(keyname);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor spe = sp.edit();
        log.addToLog(key + " Saved");
        spe.putString(key, value);
        spe.commit();
        spe.apply();

    }

    public void saveIntegerToSharedPreferences(String key, int value) {
        SharedPreferences.Editor spe = sp.edit();
        log.addToLog(key + " Saved");
        spe.putInt(key, value);
        spe.commit();
        spe.apply();

    }

    public String getValue(String keyname) {
        return sp.getString(keyname, null);
    }


    public int getDefaultMinutes() {
        return sp.getInt(SP_MINUTES, 5);
    }
}

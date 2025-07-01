package wifi.key.show.master.show.all.wifi.password.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreference {
    public static final String MY_PREF = "MyPreferences";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        this.sharedPreferences = context.getSharedPreferences(MY_PREF, 0);
        this.editor = this.sharedPreferences.edit();
    }


    public int getint(String key) {
        return this.sharedPreferences.getInt(key, 0);
    }

    public void set(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.apply();
    }


    public void setint(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.apply();
    }


    public int get(String key) {
        return this.sharedPreferences.getInt(key, 0);
    }

    public void setboolean(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.apply();
    }


    public boolean getboolean(String key) {
        return this.sharedPreferences.getBoolean(key, false);
    }


}
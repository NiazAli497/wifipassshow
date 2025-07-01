package wifi.key.show.master.show.all.wifi.password.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {
    private Context context;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static final String PREFNAME = "MyPref";
    public static final String BOOLEANKEY = "ads";

    public MyPreference(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public  void setBoolean(boolean value) {
        editor.putBoolean(BOOLEANKEY, value); // Storing boolean - true/false
        editor.commit(); // commit changes
    }

    public  boolean getBoolean() {
        return pref.getBoolean(BOOLEANKEY, false); // getting boolean
    }
}
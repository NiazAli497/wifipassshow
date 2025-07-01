package wifi.key.show.master.show.all.wifi.password

import android.content.Context
import android.content.SharedPreferences

class MyPrefHelper(mContext: Context) {
    companion object {
        const val APP_PREFERENCES = "App_Preferences"
        const val LANGUAGE_PREF = "language_pref"
    }

    var sharedPreferences: SharedPreferences =
        mContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun setLanguage(language: String) {
        editor.putString(LANGUAGE_PREF, language).apply()
    }

    fun getLanguage(): String? {
        return sharedPreferences.getString(LANGUAGE_PREF,null)
    }
    fun setBoolean(key: String?, value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaultValue)
    }
    fun setConsentValue(name: String?, value: Boolean) {
        editor!!.putBoolean(name, value)
        editor!!.commit()
        editor!!.apply()
    }
    fun getConsentValue(): Boolean {
        return sharedPreferences!!.getBoolean("consentAllowed", true)
    }

}
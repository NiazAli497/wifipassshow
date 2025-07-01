package wifi.key.show.master.show.all.wifi.password.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import wifi.key.show.master.show.all.wifi.password.model.WifiModel;

import java.util.ArrayList;


public class DataBaseWifiHistory extends SQLiteOpenHelper {


    private static final String MY_FAV_HISTORY = "DB_FAVOURITES";
    private static final String MySSidsaver = "MySSidsaver";


    private static final String MY_HISTORY_ID = "history_id";
    private static final String SSID_ID = "SSID_ID";
    private static final String SSIDNAME = "SSIDNAME";
    private static final String MY_HISTORY_URL = "history_title";

    private static final String MY_HISTORY_DATE = "history_date";
    private static final String STRENGTH_SIGNAL = "STRENGTH_SIGNAL";
    private static final String SPEEDWIFI = "SPEEDWIFI";
    private static final String INTERNET_TYPE = "INTERNET_TYPE";
    private static final String SIGNALS = "SIGNALS";


    public DataBaseWifiHistory(Context context) {
        super(context, "DUAL_BROWSER", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_FAV_TABLE = "CREATE TABLE " + MY_FAV_HISTORY +
                " (" + MY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MY_HISTORY_URL + " TEXT,"
                + STRENGTH_SIGNAL + " TEXT," +
                SPEEDWIFI + " TEXT," +
                INTERNET_TYPE + " TEXT," +
                SIGNALS + " TEXT,"
                + MY_HISTORY_DATE + " TEXT)";


        String CREATE_SSIDTABLE = "CREATE TABLE " + MySSidsaver +
                " (" + SSID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                + SSIDNAME + " TEXT)";

        db.execSQL(CREATE_SSIDTABLE);

        db.execSQL(CREATE_FAV_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MY_FAV_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + MySSidsaver);
        onCreate(db);
    }


    public long savehistory(String id, String url_address, String date, String a, String b, String c, String d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MY_HISTORY_URL, url_address);
        contentValues.put(MY_HISTORY_DATE, date);
        contentValues.put(STRENGTH_SIGNAL, a);
        contentValues.put(SPEEDWIFI, b);
        contentValues.put(INTERNET_TYPE, c);
        contentValues.put(SIGNALS, d);


        long result = db.insertOrThrow(MY_FAV_HISTORY, null, contentValues);
        db.close();
        Log.d("dualsqlitehelper", "" + result);
        return result;
    }

    public long myssidsaver(String url_address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SSIDNAME, url_address);


        long result = db.insertOrThrow(MySSidsaver, null, contentValues);
        db.close();
        Log.d("dualsqlitehelper", "" + result);
        return result;
    }


    public ArrayList<String> getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + MY_FAV_HISTORY;
        Cursor cursor = db.rawQuery(query, new String[]{});
        ArrayList<String> messages = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String msg = cursor.getString(0) + "\n" + cursor.getString(1) + "\n" + cursor.getString(2) + "\n" + cursor.getString(3) + "\n" + cursor.getString(4) + "\n" + cursor.getString(5) + "\n" + cursor.getString(6);
                messages.add(msg);
            }
            if (!cursor.isClosed())
                cursor.close();
        }
        db.close();
        return messages;
    }

    public ArrayList<WifiModel> getSsid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + MySSidsaver;
        Cursor cursor = db.rawQuery(query, new String[]{});
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<WifiModel> wifiModelArrayList = new ArrayList<>();
        WifiModel wifiModel = new WifiModel();
        if (cursor != null) {
            while (cursor.moveToNext()) {
//                String msg = cursor.getString(0) + "\n" + cursor.getString(1);
                //temp commented
//                String name = cursor.getString(cursor.getColumnIndex(SSIDNAME));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SSIDNAME));

                wifiModel.setSsid(name);
                wifiModelArrayList.add(wifiModel);
            }
            if (!cursor.isClosed())
                cursor.close();
        }
        db.close();
        return wifiModelArrayList;


    }



}

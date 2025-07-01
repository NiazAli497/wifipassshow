package wifi.key.show.master.show.all.wifi.password.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Show_password";
     public static final String COL2 = "name";
    public static final String COL3 = "password";
     public static final String TABLE_NAME1 = "Show_SSID";
     public static final String COLName = "name";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT)";
        db.execSQL(createTable);
        String createTable1 = "CREATE TABLE " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLName + " TEXT)";
        db.execSQL(createTable1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }



    public boolean addData(String item, String item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues2 = new ContentValues();

        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.insert(TABLE_NAME, null, contentValues2);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }





    //TODO: check pwd if exist


    //todo: get pass number
    public String getSSId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "select * from  " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            try {
                String cmdname = cursor.getString(cursor.getColumnIndex(COL2));
                stringBuffer.append(cmdname);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
}

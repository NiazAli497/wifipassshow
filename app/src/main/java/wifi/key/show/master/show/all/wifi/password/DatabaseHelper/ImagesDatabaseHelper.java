package wifi.key.show.master.show.all.wifi.password.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ImagesDatabaseHelper extends SQLiteOpenHelper {


    private Context ctx;
    private static final int version = 1;
    private static final String DB_NAME = "PATHDATABASE";
    private static final String tablename = "path";


    private static final String IMAGE_ID = "id";
    private static final String IMAGE_URL = "pathurl";

    private static final String CREATE_PATH_TABLE = "CREATE TABLE " + tablename +
            " (" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IMAGE_URL + " VARCHAR);";


    public ImagesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PATH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

            db.execSQL("DROP TABLE IF EXISTS " + tablename);
            onCreate(db);
        } catch (Exception e) {
        }

    }

/*
    public ArrayList<String> getallimagespath() {

        ArrayList<String> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from path", null);
        res.moveToFirst();

        if (res != null) {
            while (res.isAfterLast() == false) {
                arrayList.add(res.getString(res.getColumnIndex(IMAGE_URL)));

                res.moveToNext();
            }
        }
        return arrayList;


    }

    public void InsertPathData(String paths) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pathurl", paths);
        db.insert(tablename, null, cv);
        db.close();
    }

    public void deleteimage(String path) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tablename, IMAGE_URL + " = ?", new String[]{String.valueOf(path)});
        db.close();

    }

    public void allimagesdelete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tablename);
        db.close();
    }*/


}

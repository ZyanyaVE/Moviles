package itesm.mx.golpedecalor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marcelo on 22/03/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "golpedecalor.db";
    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME  = "fname";
    public static final String COLUMN_LNAME  = "lname";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_SEX = "sex";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_FNAME + " TEXT," +
                COLUMN_LNAME + " TEXT," +
                COLUMN_BIRTHDAY + " TEXT," +
                COLUMN_SEX + " TEXT" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


}

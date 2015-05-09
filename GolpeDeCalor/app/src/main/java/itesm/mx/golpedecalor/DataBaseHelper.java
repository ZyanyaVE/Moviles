package itesm.mx.golpedecalor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marcelo on 22/03/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper sInstance;

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "golpedecalor.db";

    //Tabla para los usuarios
    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME  = "fname";
    public static final String COLUMN_LNAME  = "lname";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_GROUPID = "groupid";

    //Tabla para los grupos
    public static final String TABLE_GROUPS = "grupos";
    public static final String COLUMN_NAME = "name";

    public static synchronized DataBaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_FNAME + " TEXT," +
                COLUMN_LNAME + " TEXT," +
                COLUMN_BIRTHDAY + " TEXT," +
                COLUMN_SEX + " TEXT," +
                COLUMN_GROUPID + " INTEGER" +
                ")";

        String CREATE_GROUPS_TABLE = "CREATE TABLE " +
                TABLE_GROUPS +
                "(" +
                COLUMN_GROUPID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT" +
                ")";



        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_GROUPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        onCreate(db);
    }


}

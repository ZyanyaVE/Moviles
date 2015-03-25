package itesm.mx.golpedecalor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Marcelo on 22/03/2015.
 */
public class DataBaseOperations {
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME  = "fname";
    public static final String COLUMN_LNAME  = "lname";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_SEX = "sex";

    public DataBaseOperations(Context context){
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public Usuario findAccount (int id){

        Usuario usuario;

        String query = "Select * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_ID + " = " + id;

        System.out.println(query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            usuario = new Usuario(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        else{
            usuario = null;
        }

        return usuario;

    }

    public long registerUser (Usuario user){
        System.out.println(user.getFechaNacimiento());
        ContentValues values = new ContentValues();
        values.put(COLUMN_FNAME, user.getNombre());
        values.put(COLUMN_LNAME, user.getApellidos());
        values.put(COLUMN_BIRTHDAY, user.getFechaNacimiento());
        values.put(COLUMN_SEX, user.getSexo());

        return db.insert(TABLE_USERS, null, values);
    }
}

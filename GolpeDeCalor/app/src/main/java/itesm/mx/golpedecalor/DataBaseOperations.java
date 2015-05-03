package itesm.mx.golpedecalor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public static final String COLUMN_GROUPID = "groupid";

    //Tabla para los grupos
    public static final String TABLE_GROUPS = "grupos";
    public static final String COLUMN_NAME = "name";

    public DataBaseOperations(Context context){
        dbHelper = DataBaseHelper.getInstance(context);
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
        ContentValues values = new ContentValues();
        values.put(COLUMN_FNAME, user.getNombre());
        values.put(COLUMN_LNAME, user.getApellidos());
        values.put(COLUMN_BIRTHDAY, user.getFechaNacimiento());
        values.put(COLUMN_SEX, user.getSexo());
        values.putNull(COLUMN_GROUPID);

        return db.insert(TABLE_USERS, null, values);
    }



    public long addGroup (Grupo grupo, ArrayList<Usuario> integrantes){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, grupo.getNombre());
        long id = db.insert(TABLE_GROUPS, null, values);
        grupo.setId(id);

        for (int i = 0; i < integrantes.size(); i++){
            Usuario aAgregar = integrantes.get(i);
            ContentValues values2 = new ContentValues();
            values2.put(COLUMN_GROUPID, id);

            String where = COLUMN_ID + " = " + aAgregar.getId();

            db.update(TABLE_USERS, values2, where, null);
        }

        return id;
    }

    public void addPersonToGroup(Grupo grupo, Usuario aAgregar){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUPID, grupo.getId());
        String where = COLUMN_ID + " = " + aAgregar.getId();
        db.update(TABLE_USERS, values, where, null);
    }

    public ArrayList<Grupo> getAllGroups(){
        ArrayList<Grupo> grupos = new ArrayList<Grupo>();

        String selectQuery = "SELECT * FROM " + TABLE_GROUPS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                Grupo grupo = new Grupo(cursor.getLong(0), cursor.getString(1));
                grupos.add(grupo);
            } while (cursor.moveToNext());
        }

        cursor.close();


        return grupos;
    }

    public Grupo getGroup(long id){
        String selectQuery = "SELECT * FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUPID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Grupo grupo;
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            grupo = new Grupo(cursor.getLong(0), cursor.getString(1));
            cursor.close();
        }
        else{
            grupo = null;
        }

        return grupo;

    }

    public ArrayList<Usuario> getAllUsersFromGroup(Grupo grupo){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_GROUPID + " = " + grupo.getId();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Usuario usuario = new Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return usuarios;

    }

    public boolean userInGroup(Grupo gpo, Usuario user){
        boolean encontrado = false;
        ArrayList<Usuario> usuarios = getAllUsersFromGroup(gpo);
        for (Usuario u : usuarios){
            if (u.getId() == user.getId()){
                encontrado = true;
                break;
            }
        }

        return encontrado;

    }
    public boolean deleteMember(long id)
    {
        ContentValues values = new ContentValues();
        values.putNull(COLUMN_GROUPID);
        //return db.delete(TABLE_GROUPS, COLUMN_ID + "=" + id + " and " + COLUMN_GROUPID + "=" + idgpo, null) > 0;
        return db.update(TABLE_USERS, values, COLUMN_ID  + "=" + id, null) > 0;
    }

    public boolean deleteGroup(long id)
    {
        ContentValues values = new ContentValues();
        values.putNull(COLUMN_GROUPID);
        //return db.delete(TABLE_GROUPS, COLUMN_ID + "=" + id + " and " + COLUMN_GROUPID + "=" + idgpo, null) > 0;
        db.delete(TABLE_GROUPS, COLUMN_GROUPID + "=" + id, null);
        return db.update(TABLE_USERS, values, COLUMN_GROUPID  + "=" + id, null) > 0;

    }

    public ArrayList<Usuario> getAllUsers(){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Usuario usuario = new Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return usuarios;

    }



}

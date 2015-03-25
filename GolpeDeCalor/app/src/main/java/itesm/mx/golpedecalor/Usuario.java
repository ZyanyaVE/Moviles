package itesm.mx.golpedecalor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZyanyaVE on 3/19/15.
 */
public class Usuario implements Parcelable{
    private long id;
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String sexo;

    public Usuario(long id, String nombre, String apellidos, String fechaNacimiento, String sexo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
    }

    public Usuario(long id, String nombre, String apellidos, Integer dia, Integer mes, Integer año, String sexo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = "" + año + "-" + mes + "-" + dia;
        this.sexo = sexo;
    }

    private Usuario (Parcel in){
        id = in.readLong();
        nombre = in.readString();
        apellidos = in.readString();
        fechaNacimiento = in.readString();
        sexo = in.readString();
    }





    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(nombre);
        out.writeString(apellidos);
        out.writeString(fechaNacimiento);
        out.writeString(sexo);
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };


}

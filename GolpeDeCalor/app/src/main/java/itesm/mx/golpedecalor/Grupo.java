package itesm.mx.golpedecalor;

import java.util.ArrayList;

/**
 * Created by Marcelo on 14/04/2015.
 */
public class Grupo {
    private long id;
    private String nombre;
    private ArrayList<Usuario> integrantes;

    public Grupo(long id, String nombre){
        this.id = id;
        this.nombre = nombre;
        this.integrantes = new ArrayList<Usuario>();
    }


    public Grupo(long id, String nombre, ArrayList<Usuario> integrantes) {
        this.id = id;
        this.nombre = nombre;
        this.integrantes = integrantes;
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

    public ArrayList<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(ArrayList<Usuario> integrantes) {
        this.integrantes = integrantes;
    }
}

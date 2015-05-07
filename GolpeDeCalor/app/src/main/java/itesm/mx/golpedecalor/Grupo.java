/*
    Golpe de Calor

    Copyright (C) 2015
    Marcelo Alberto Cantú Quiroga
    Zyanya Valdés Esquivel
    Hugo León Garza

    Última Modificación: 14 de Abril del 2015
    Nombre del Archivo: Grupo.java
    Convención de nombres: "CamelCase"
    Versión 1.0

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package itesm.mx.golpedecalor;

import java.util.ArrayList;

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

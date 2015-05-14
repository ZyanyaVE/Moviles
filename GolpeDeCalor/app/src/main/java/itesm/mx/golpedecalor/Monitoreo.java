/*
    Golpe de Calor

    Copyright (C) 2015
    Marcelo Alberto Cantú Quiroga
    Zyanya Valdés Esquivel
    Hugo León Garza

    Última Modificación: 28 de Abril del 2015
    Nombre del Archivo: Monitoreo.java
    Convención de nombres: "CamelCase"
    Versión 3.0

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

import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase helper que sirve para relaizar el monitoreo de los usuarios
 */

public class Monitoreo {
    Grupo grupo;
    MonitoringActivity interfaz;
    Timer timer;

    //Mapeo de alertas
    ArrayList<Usuario> usuariosAlerta;
    ArrayList<String> valores;
    ArrayList<String> causas;



    public Monitoreo(Grupo grupo, MonitoringActivity interfaz) {
        this.grupo = grupo;
        this.interfaz = interfaz;

        usuariosAlerta = new ArrayList<>();
        valores = new ArrayList<>();
        causas = new ArrayList<>();
    }

    /**
     * Método utilizado para obtener el valor de temperatura del usuario
     * @param user Es el usuario al cual se le quiere obtener su temperatura
     * @return Regresa el valor de la temperatura
     */
    private float getTemp(Usuario user){
        Random rand = new Random();
        float temp;
        if (rand.nextDouble() < .8){
            temp = rand.nextFloat() * (38.0f - 37.0f) + 37.0f;
        }
        else{
            temp = rand.nextFloat() * (40.0f - 38.0f) + 38.0f;
        }

        return temp;
    }

    /**
     * Método utilizado para obtener el valor del ritmo cardiaco del usuario
     * @param user Es el usuario al cual se le quiere obtener su ritmo cardiaco
     * @return Regresa el valor del ritmo cardiaco
     */
    private int getRitmoCardiaco(Usuario user) {
        Random rand = new Random();
        int rc;
        if (rand.nextDouble() < .8) {
            rc = rand.nextInt((100 - 60) + 1) + 60;
        } else {
            rc = rand.nextInt((120 - 100) + 1) + 100;
        }
        return rc;
    }

    /**
     * Método utilizado para obtener el valor de radiación del usuario
     * @param user Es el usuario al cual se le quiere obtener su radiación
     * @return Regresa el valor de la radiación
     */
    private int getRadiacion(Usuario user) {
        Random rand = new Random();
        int rad;
        if (rand.nextDouble() < .8) {
            rad = rand.nextInt((100 - 50) + 1) + 50;
        } else {
            rad = rand.nextInt((150 - 100) + 1) + 100;
        }
        return rad;
    }

    /**
     * Este método lo que hace es empezar el monitoreo, lo hace creando un timer
     * el cual corrrerá cada cierto tiempo y obtendrá el valor de los parámetros,
     * después de esto checará si supera el valor deseado.
     */
    public void empezarMonitoreo(){
        timer = new Timer();
        final Handler handler = new Handler();
        TimerTask monitorear = new TimerTask() {
            @Override
            public void run() {
                int i = 0;
                for (final Usuario u : grupo.getIntegrantes()){
                    final float temp = getTemp(u);
                    final int rc = getRitmoCardiaco(u);
                    final int rad = getRadiacion(u);
                    final int ind = i;
                    handler.post(new Runnable() {
                        public void run() {
                            interfaz.updateValues(temp, rc, rad, ind);
                            if (temp > 39.5){
                                usuariosAlerta.add(u);
                                valores.add(String.valueOf(temp));
                                causas.add("Temp");
                                interfaz.newNotification("¡Alerta! Temperatura eleveda", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", usuariosAlerta.size() - 1);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "Temp", String.valueOf(temp), true);

                            }
                            if (rc > 115){
                                usuariosAlerta.add(u);
                                valores.add(String.valueOf(rc));
                                causas.add("RC");
                                interfaz.newNotification("¡Alerta! Ritmo Cardiaco elevado", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", usuariosAlerta.size() - 1);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "RC", String.valueOf(rc), true);

                            }
                            if (rad > 140){
                                usuariosAlerta.add(u);
                                valores.add(String.valueOf(rad));
                                causas.add("Rad");
                                interfaz.newNotification("¡Alerta! Radiación solar elevada", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", usuariosAlerta.size() - 1);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "Rad", String.valueOf(rad), true);

                            }


                        }

                    });
                    i++;
                }
            }
        };
        timer.schedule(monitorear, 01, 1000*5);
    }

    public void terminarMonitoreo(){
        timer.cancel();
    }

    public void terminarAlerta(){

        usuariosAlerta.remove(usuariosAlerta.size() - 1);
        valores.remove(valores.size() - 1);
        causas.remove(causas.size() - 1);
        interfaz.terminarAlerta(usuariosAlerta.isEmpty(), usuariosAlerta.size());

        if (!usuariosAlerta.isEmpty()){
            interfaz.alerta(usuariosAlerta.get(usuariosAlerta.size() - 1).getNombre() + " " + usuariosAlerta.get(usuariosAlerta.size() - 1).getApellidos(), causas.get(causas.size() - 1), valores.get(valores.size() - 1), false);
        }

    }

}

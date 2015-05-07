package itesm.mx.golpedecalor;

import android.os.Handler;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Marcelo on 15/04/2015.
 */
public class Monitoreo {
    Grupo grupo;
    MonitoringActivity interfaz;
    Timer timer;

    public Monitoreo(Grupo grupo, MonitoringActivity interfaz) {
        this.grupo = grupo;
        this.interfaz = interfaz;
    }

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
                                interfaz.newNotification("¡Alerta! Temperatura eleveda", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", ind);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "Temp", String.valueOf(temp));
                            }
                            if (rc > 115){
                                interfaz.newNotification("¡Alerta! Ritmo Cardiaco elevado", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", ind);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "RC", String.valueOf(rc));
                            }
                            if (rad > 140){
                                interfaz.newNotification("¡Alerta! Radiación solar elevada", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", ind);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "Rad", String.valueOf(rad));
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

}

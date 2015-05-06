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

    private int getRitmoCardiaco(Usuario user){
        Random rand = new Random();
        int rc;
        if (rand.nextDouble() < .8){
            rc = rand.nextInt((100 - 60) + 1) + 60;
        }
        else{
            rc = rand.nextInt((120 - 100) + 1) + 100;
        }


        return rc;
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
                    final int ind = i;
                    handler.post(new Runnable() {
                        public void run() {
                            interfaz.updateValues(temp, rc, 0, ind);
                            if (temp > 39.5){
                                interfaz.newNotification("¡Alerta! Temperatura eleveda", "¡Alerta! Trabajador en riesgo", u.getNombre() + " " + u.getApellidos()+ " está en riesgo.", ind);
                                interfaz.alerta(u.getNombre() + " " + u.getApellidos(), "Temp", String.valueOf(temp));

                            }

                        }

                    });
                    i++;
                }
            }
        };
        timer.schedule(monitorear, 01, 1000*2);
    }

    public void terminarMonitoreo(){
        timer.cancel();
    }

}

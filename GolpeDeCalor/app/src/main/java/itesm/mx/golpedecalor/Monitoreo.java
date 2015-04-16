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
        float temp = rand.nextFloat() * (40.0f - 37.0f) + 37.0f;
        return temp;
    }

    private int getRitmoCardiaco(Usuario user){
        Random rand = new Random();
        int rc = rand.nextInt((200 - 100) + 1) + 100;
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

                            if (temp > 39){
                                interfaz.newNotification("Temperatura alta", u.getNombre(), u.getNombre()+ "Cuidado temp alta");
                            }



                        }

                    });
                    i++;
                    //Toast.makeText(interfaz, "LALALALALA", Toast.LENGTH_SHORT).show();
                }
            }
        };
        timer.schedule(monitorear, 01, 1000*2);
    }

    public void terminarMonitoreo(){
        timer.cancel();
    }

}

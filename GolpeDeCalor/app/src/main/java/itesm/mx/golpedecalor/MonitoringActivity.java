package itesm.mx.golpedecalor;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.sql.SQLException;
import java.util.ArrayList;


public class MonitoringActivity extends ActionBarActivity {

    // Declaración de Variables
    Grupo grupo;
    long groupId;
    DataBaseOperations dbo;
    TableLayout tablaTL;
    Monitoreo monitoreoHelper;

    private Intent notificationIntent;
    private PendingIntent pendingIntent;
    NotificationManager notificationManager;

    private ArrayList<TextView> rc;
    private ArrayList<TextView> temp;
    private ArrayList<TextView> rad;

    private boolean alerta;

    TextView nombreTV, causaTV, parametroTV;
    ViewSwitcher switcherVS;

    DialogInterface.OnClickListener dialogClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        alerta = false;

        dbo = new DataBaseOperations(getApplicationContext());
        groupId = getIntent().getLongExtra("id", 0);

        // Referencias a objetos de interfacae
        tablaTL = (TableLayout) findViewById(R.id.tablaTL);
        nombreTV = (TextView) findViewById(R.id.nombreTV);
        causaTV = (TextView) findViewById(R.id.causaTV);
        parametroTV = (TextView) findViewById(R.id.parametroTV);
        switcherVS = (ViewSwitcher) findViewById(R.id.switcherVS);

        // Inicialización
        rc = new ArrayList<TextView>();
        temp = new ArrayList<TextView>();
        rad = new ArrayList<TextView>();

        try {
            dbo.open();
        }
        catch (SQLException ex){
            Log.e("", ex.toString());
        }

        grupo = dbo.getGroup(groupId);
        grupo.setIntegrantes(dbo.getAllUsersFromGroup(grupo));
        monitoreoHelper = new Monitoreo(grupo, this);

        for(Usuario u : grupo.getIntegrantes()){
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tvAux = new TextView(this);
            tvAux.setText(u.getNombre() + " " + u.getApellidos());

            TextView rcAux = new TextView(this);
            rcAux.setText("0");

            TextView tempAux = new TextView(this);
            tempAux.setText("0");

            TextView radAux = new TextView(this);
            radAux.setText("0");

            rc.add(rcAux);
            temp.add(tempAux);
            rad.add(radAux);

            tr.addView(tvAux);
            tr.addView(rcAux);
            tr.addView(tempAux);
            tr.addView(radAux);
            tablaTL.addView(tr);
        }
        monitoreoHelper.empezarMonitoreo();

        notificationIntent = new Intent(getApplicationContext(), MonitoringActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        switcherVS.showNext();
                        alerta = false;
                        notificationManager.cancelAll();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

    }

    @Override
    protected void onResume(){
        try {
            dbo.open();
        }
        catch (SQLException ex){
            Log.e("", ex.toString());
        }
        super.onResume();
    }

    @Override
    protected void onPause(){
        dbo.close();
        super.onPause();
    }

    // Se cancela el monitoreo u la recepción de datos al presionar back
    @Override
    public void onBackPressed(){
        //agregar un dialog box para preguntar si esta seguro que quiere
        monitoreoHelper.terminarMonitoreo();
        Toast.makeText(getApplicationContext(), "Monitoreo terminado", Toast.LENGTH_SHORT).show();
        finish();
    }

    //
    public void updateValues(float tempa, int rcard, int radia, int ind){
        temp.get(ind).setText(String.format("%.1f", tempa));
        rc.get(ind).setText(String.valueOf(rcard));
        rad.get(ind).setText(String.valueOf(radia));
    }

    public void newNotification(String titulo, String descripCorta, String contenido, int notifID){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        notificationBuilder.setContentTitle(titulo);
        notificationBuilder.setTicker(descripCorta);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        notificationBuilder.setContentText(contenido);
        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifID, notificationBuilder.build());

    }

    public void alerta(String nombre, String causa, String parametro){
        if (!alerta){
            switcherVS.showNext();
            alerta = true;
        }

        nombreTV.setText(nombre);
        if (causa == "Temp"){
            causaTV.setText("Se le presentó una temperatura muy elevada");
            parametroTV.setText("Temperatura: " + parametro + "°C");
        }
        else if (causa == "RC"){
            causaTV.setText("Se le presentó un ritmo cardiaco muy elevado");
            parametroTV.setText("Ritmo cardiáco: " + parametro + " ppm");
        }
        else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickEnterado(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Se atendio al trabajador?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void onClickTerminarMonitoreo(View v){
        monitoreoHelper.terminarMonitoreo();
        Toast.makeText(getApplicationContext(), "Monitoreo terminado", Toast.LENGTH_SHORT).show();
        finish();
    }
}

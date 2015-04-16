package itesm.mx.golpedecalor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;


public class MonitoringActivity extends ActionBarActivity {

    Grupo grupo;
    long groupId;
    DataBaseOperations dbo;
    TableLayout tablaTL;
    Monitoreo monitoreoHelper;

    private ArrayList<TextView> rc;
    private ArrayList<TextView> temp;
    private ArrayList<TextView> rad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        dbo = new DataBaseOperations(getApplicationContext());
        groupId = getIntent().getLongExtra("id", 0);

        tablaTL = (TableLayout) findViewById(R.id.tablaTL);

        rc = new ArrayList<TextView>();
        temp = new ArrayList<TextView>();
        rad = new ArrayList<TextView>();


    }

    public void toast(){
        Toast.makeText(getApplicationContext(), "LALALA", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
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
        super.onResume();
    }

    @Override
    protected void onPause(){
        dbo.close();
        super.onPause();
    }

    public void updateValues(float tempa, int rcard, int radia, int ind){
        temp.get(ind).setText(String.format("%.1f", tempa));
        rc.get(ind).setText(String.valueOf(rcard));
        rad.get(ind).setText(String.valueOf(radia));

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
}

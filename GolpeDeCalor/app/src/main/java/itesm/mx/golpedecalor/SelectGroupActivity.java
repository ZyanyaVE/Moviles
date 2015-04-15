package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;


public class SelectGroupActivity extends ActionBarActivity {

    // Declaración de Variables
    TextView nombreTV;
    ListView miembrosLV;
    Usuario usuarioPrincipal;
    DataBaseOperations dbo;
    ArrayList<Grupo> grupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        dbo = new DataBaseOperations(getApplicationContext());


        // Referencias a objetos de interface
        nombreTV = (TextView) findViewById(R.id.nombreTV);
        miembrosLV = (ListView) findViewById(R.id.miembrosLV);

        usuarioPrincipal = (Usuario) getIntent().getParcelableExtra("Usuario");
        nombreTV.setText("Hola, " + usuarioPrincipal.getNombre());

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectGroupActivity.this, CreateViewGroupActivity.class);
                intent.putExtra("groupid", grupos.get(position).getId());
                intent.putExtra("existente?", true);
                startActivity(intent);
            }
        };

        miembrosLV.setOnItemClickListener(itemListener);


    }

    @Override
    protected void onResume(){
        try {
            dbo.open();
        }
        catch (SQLException ex){
            Log.e("", ex.toString());
        }

        grupos = new ArrayList(dbo.getAllGroups());

        ArrayList<String> nombres = new ArrayList<>();

        for (Grupo g : grupos){
            nombres.add(g.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_row, R.id.rowTV, nombres);
        miembrosLV.setAdapter(adapter);

        super.onResume();



    }

    @Override
    protected void onPause(){
        dbo.close();
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public void onClickNuevoGrupo(View v){
        Intent intent = new Intent(SelectGroupActivity.this, CreateViewGroupActivity.class);
        intent.putExtra("existente?", false);
        startActivity(intent);
    }
}

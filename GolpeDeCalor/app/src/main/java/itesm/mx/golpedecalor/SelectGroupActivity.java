package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;


public class SelectGroupActivity extends ActionBarActivity {

    // Declaración de Variables
    TextView nombreTV;
    ListView miembrosLV;
    Usuario usuarioPrincipal;
    DataBaseOperations dbo;
    ArrayList<Grupo> grupos;
    ArrayAdapter<String> adapter;
    ArrayList<String> nombres;

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

        // Si se presiona un grupo se va al intent de verGrupos
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectGroupActivity.this, CreateViewGroupActivity.class);
                intent.putExtra("Usuario", usuarioPrincipal);
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

        nombres = new ArrayList<>();

        for (Grupo g : grupos){
            nombres.add(g.getNombre());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_row, R.id.rowTV, nombres);
        miembrosLV.setAdapter(adapter);
        registerForContextMenu(miembrosLV);
        registerForContextMenu(miembrosLV);


        super.onResume();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(), "No se puede hacia atrás", Toast.LENGTH_SHORT);
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

    // Se crea un nuevo grupo y se inicia la creación de grupos
    public void onClickNuevoGrupo(View v){
        Intent intent = new Intent(SelectGroupActivity.this, CreateViewGroupActivity.class);
        intent.putExtra("existente?", false);
        intent.putExtra("Usuario", usuarioPrincipal);
        startActivity(intent);
    }

    // Accesa al layout de menu menu_context
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // Despliega las opciones del menú cuando se realiza un long click
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();

        // Si se elimina se llama a la función de eliminar en DBO
        if (id == R.id.delete){
            //(grupos.get(info.position)).getId()

            Toast.makeText(getApplicationContext(), "DELETE " + nombres.get(info.position), Toast.LENGTH_LONG).show();
            boolean deleted = dbo.deleteGroup((grupos.get(info.position)).getId());

            nombres.remove(info.position);
            adapter.notifyDataSetChanged();

            return true;
        }
        return super.onContextItemSelected(item);
    }
}

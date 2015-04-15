package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.sql.SQLException;
import java.util.ArrayList;


public class CreateViewGroupActivity extends ActionBarActivity {

    boolean existente;

    EditText groupNameET;
    TextView groupNameTV;
    ListView groupMembersLV;
    ArrayList<String> nombres;

    ArrayAdapter<String> adapter;

    ViewSwitcher viewSwitcher;

    DataBaseOperations dbo;

    Grupo grupo;

    ArrayList<Usuario> miembrosGpo;

    final static int PICK_CONTACT_REQUEST = 1994;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_view_group);

        dbo = new DataBaseOperations(getApplicationContext());

        groupNameET = (EditText) findViewById(R.id.groupNameET);
        groupNameTV = (TextView) findViewById(R.id.groupNameTV);
        groupMembersLV = (ListView) findViewById(R.id.groupMembersLV);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.cambioVS);

        nombres = new ArrayList<>();

        existente = getIntent().getBooleanExtra("existente?", false);

        try {
            dbo.open();
        }
        catch (SQLException ex){
            Log.e("", ex.toString());
        }

        if (existente) {
            long id = getIntent().getLongExtra("groupid", 0);
            grupo = dbo.getGroup(id);
            miembrosGpo = dbo.getAllUsersFromGroup(grupo);
            viewSwitcher.showNext();
            groupNameTV.setText(grupo.getNombre());

            for (Usuario u : miembrosGpo){
                nombres.add(u.getNombre() + " " + u.getApellidos());
            }

        } else {
            miembrosGpo = new ArrayList<Usuario>();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_row, R.id.rowTV, nombres);
        groupMembersLV.setAdapter(adapter);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_view_group, menu);
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

    public void onClickEmpezarMonitoreo(View v){
        if (existente){

        }
        else{
            if (!groupNameET.getText().toString().equals("")){
                if (!miembrosGpo.isEmpty()){
                    Grupo gpo = new Grupo(0, groupNameET.getText().toString());
                    long idnumber = dbo.addGroup(gpo, miembrosGpo);
                    gpo.setId(idnumber);
                    Toast.makeText(getApplicationContext(), "Grupo agregado correctamente", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Favor de agregar al menos una persona", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Favor de llenar el nombre del grupo", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onClickAgregarPersona(View v){
        Intent intent = new Intent(CreateViewGroupActivity.this, AddUserActivity.class);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
               Bundle extras = data.getExtras();
               long id = extras.getLong("id");
                try {
                    dbo.open();
                }
                catch (SQLException ex){

                }
               Usuario user = dbo.findAccount((int)id);
               miembrosGpo.add(user);
               nombres.add(user.getNombre() + " " + user.getApellidos());


                 adapter.notifyDataSetChanged();
                System.out.println("Entra!");
                if (existente){
                    dbo.addPersonToGroup(grupo, user);
                }

                System.out.println("lala");


            }

        }
    }

}

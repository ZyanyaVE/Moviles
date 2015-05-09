/*
    Golpe de Calor

    Copyright (C) 2015
    Marcelo Alberto Cantú Quiroga
    Zyanya Valdés Esquivel
    Hugo León Garza

    Última Modificación: 20 de Abril del 2015
    Nombre del Archivo: SelectGroupActivity.java
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

import android.content.Intent;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

            //Toast.makeText(getApplicationContext(), "DELETE " + nombres.get(info.position), Toast.LENGTH_LONG).show();
            boolean deleted = dbo.deleteGroup((grupos.get(info.position)).getId());

            nombres.remove(info.position);
            adapter.notifyDataSetChanged();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onClickSincronizar(View v){
        new RequestTask().execute("http://golpedecalor.comoj.com/dbHandler.php");
        Toast.makeText(getApplicationContext(), "Sincronizacion Exitosa", Toast.LENGTH_SHORT).show();
    }

    /**
     * Clase que sirve para manejar la sincronización con el servidor de
     * manera asincrónica
     */
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                ArrayList<Grupo> grupos = dbo.getAllGroups();
                ArrayList<Usuario> usuarios = dbo.getAllUsers();
                int i = 0;

                for (Grupo g : grupos){
                    nameValuePairs.add(new BasicNameValuePair("grupo-"+ i, g.getNombre() + "-" + g.getId()));
                    i++;
                }
                nameValuePairs.add(new BasicNameValuePair("del", "--"));
                i = 0;
                for (Usuario u : usuarios){
                    nameValuePairs.add(new BasicNameValuePair("usuario-"+ i, u.getId() + "*" + u.getNombre() + "*" + u.getApellidos() + "*" + u.getFechaNacimiento() + "*" + u.getSexo()));
                    i++;
                }



                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httpPost);
                return EntityUtils.toString(response.getEntity());


            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}

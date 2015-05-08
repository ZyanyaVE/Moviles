/*
    Golpe de Calor

    Copyright (C) 2015
    Marcelo Alberto Cantú Quiroga
    Zyanya Valdés Esquivel
    Hugo León Garza

    Última Modificación: 28 de Abril del 2015
    Nombre del Archivo: AddUserActivity.java
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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;


public class AddUserActivity extends ActionBarActivity {

    // Declaración de variables
    final static int PICK_CONTACT_REQUEST = 1995;
    EditText idET;
    DataBaseOperations dbo;
    ArrayList<Usuario> miembrosGpo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Referencias a objetos de interface
        idET = (EditText) findViewById(R.id.idET);
        dbo = new DataBaseOperations(getApplicationContext());

        miembrosGpo = getIntent().getParcelableArrayListExtra("usuarios");


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
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
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

    /**
     * Método que sirve como handler para el botón de agregar usuario
     * @param v Es la vista del botón
     */
      public void onClickAgregarUsuario(View v){
        if (!idET.getText().toString().equals("")){  // Verificación de variables
            Usuario user = dbo.findAccount(Integer.parseInt(idET.getText().toString()));
            if (user != null){
                boolean encontrado = false;
                for (Usuario u : miembrosGpo){
                    if (u.getId() == user.getId()){
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    Intent intent = new Intent();
                    intent.putExtra("id", user.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Este usuario ya pertenece al grupo.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "El número de id ingresado no existe", Toast.LENGTH_SHORT).show();
            }
        }
            else{
            Toast.makeText(getApplicationContext(), "Favor ingresa un número de id", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que sirve como handler para el botón de registrarse
     * @param v Es la vista del botón
     */
    public void onClickRegistrarUsuario(View v){
        Intent intent = new Intent(AddUserActivity.this, SignUpActivity.class);
        intent.putExtra("registroinicial", false);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    // Respuesta al intent de CreateViewGroupActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                long id = extras.getLong("id");
                Intent intent = new Intent();
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }



}

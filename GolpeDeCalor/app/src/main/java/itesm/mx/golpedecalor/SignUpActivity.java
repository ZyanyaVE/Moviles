package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class SignUpActivity extends ActionBarActivity {

        // Declaracion de variables
        Spinner sexosSP;
        EditText nombreET, apellidosET;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            sexosSP = (Spinner) findViewById(R.id.sexosSP);
            nombreET = (EditText) findViewById(R.id.nombreET);
            apellidosET = (EditText) findViewById(R.id.apellidosET);
            //fechaNacimientoET = (EditText) findViewById(R.id.fechaNacimientoET);

            // Adaptador utilizado para el arreglo de sexos
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexos, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            sexosSP.setAdapter(adapter);

        }

        // Cambio de Activity para terminar registro e iniciar Home Activity
        public void onClickRegistrarse(View v){
            Intent registrarseIntent = new Intent (SignUpActivity.this, HomeActivity.class);
            registrarseIntent.putExtra("nombre", nombreET.getText().toString());
            registrarseIntent.putExtra("apellidos", apellidosET.getText().toString());
            //registrarseIntent.putExtra("fecha", fechaNacimientoET.getText().toString());

            startActivity(registrarseIntent);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_login, menu);
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

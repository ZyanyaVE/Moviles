package itesm.mx.golpedecalor;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class SignUpActivity extends ActionBarActivity {

        // Declaracion de variables
        Spinner sexosSP;
        EditText nombreET, apellidosET, diaET, mesET, añoET;

        boolean registroInicial;

        DataBaseOperations dbo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            // Referencias a objetos de interface
            sexosSP = (Spinner) findViewById(R.id.sexosSP);
            nombreET = (EditText) findViewById(R.id.nombreET);
            apellidosET = (EditText) findViewById(R.id.apellidosET);
            diaET = (EditText) findViewById(R.id.diaET);
            mesET = (EditText) findViewById(R.id.mesET);
            añoET = (EditText) findViewById(R.id.anoET);

            registroInicial = getIntent().getExtras().getBoolean("registroinicial");

            // Adaptador utilizado para el arreglo de sexos
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexos, R.layout.spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            sexosSP.setAdapter(adapter);

            dbo = new DataBaseOperations(getApplicationContext());
        }


        @Override
        protected void onResume()   {
            try {
                dbo.open();
            }
            catch (Exception e){
                System.out.println("lala");
            }
            super.onResume();
        }

    @Override
    protected void onPause(){
        dbo.close();
        super.onPause();
    }

        // Cambio de Activity para terminar registro e iniciar Home Activity
        public void onClickRegistrarse(View v){
            String primerNombre = nombreET.getText().toString();
            String apellidos = apellidosET.getText().toString();
            String dia = diaET.getText().toString();
            String mes = mesET.getText().toString();
            String año = añoET.getText().toString();
            String sexo = sexosSP.getSelectedItem().toString();

            //Se checa que los campos no estén vacios
            if (!primerNombre.equals("") && !apellidos.equals("") && !dia.equals("") && !mes.equals("") && !año.equals("")){
                Integer diaInt = Integer.parseInt(dia);
                Integer mesInt = Integer.parseInt(mes);
                Integer añoInt = Integer.parseInt(año);

                //Se checa que el mes y el año esten en un rango aceptable
                if (mesInt > 0 && mesInt < 13 && añoInt > 1915 && añoInt < 2015){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, añoInt);
                    calendar.set(Calendar.MONTH, mesInt - 1);
                    int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    //Se checa si el dia cae dentro del mes correcto
                    if (diaInt <= numDays){

                        Usuario user = new Usuario(0, primerNombre, apellidos, diaInt, mesInt, añoInt, sexo);
                        long id = dbo.registerUser(user);
                        user.setId(id);


                        if (registroInicial) {
                            Intent mostrarIDIntent = new Intent(SignUpActivity.this, ShowIDActivity.class);
                            mostrarIDIntent.putExtra("Usuario", user);
                            Toast.makeText(getApplicationContext(), "Usuario creado correctamente", Toast.LENGTH_LONG).show();
                            startActivity(mostrarIDIntent);
                        }
                        else{
                            Intent intent = new Intent();
                            intent.putExtra("id", id);
                            setResult(RESULT_OK, intent);
                            Toast.makeText(getApplicationContext(), "Usuario creado correctamente, su ID es: " + id, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Fecha Inválida", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fecha Inválida", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(getApplicationContext(), "Faltó llenar algún campo", Toast.LENGTH_SHORT).show();
            }
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

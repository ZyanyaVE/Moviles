package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {

    // Declaracion de Variables
    DataBaseOperations dbo;

    EditText idET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a objetos de interface
        idET = (EditText) findViewById(R.id.IdET);

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

    // Se revisa la base de datos para saber si existe el usuario ingresado
    public void onClickIngresar(View v){
        Integer id = Integer.parseInt(idET.getText().toString());
        Usuario user = dbo.findAccount(id);
        if (user != null){
            Intent homeIntent = new Intent (MainActivity.this, SelectGroupActivity.class);
            homeIntent.putExtra("Usuario", user);

            startActivity(homeIntent);
        }
        else{
            Toast.makeText(getApplicationContext(), "El ID que ingresaste no existe", Toast.LENGTH_SHORT).show();
        }
    }

    // Va a la activity de SignUpActivity
    public void onClickRegistrarse(View v){
        Intent registrarseIntent = new Intent(MainActivity.this, SignUpActivity.class);
        registrarseIntent.putExtra("registroinicial", true);
        startActivity(registrarseIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

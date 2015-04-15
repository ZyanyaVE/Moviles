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


public class AddUserActivity extends ActionBarActivity {

    final static int PICK_CONTACT_REQUEST = 1995;
    EditText idET;
    DataBaseOperations dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        idET = (EditText) findViewById(R.id.idET);
        dbo = new DataBaseOperations(getApplicationContext());

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

      public void onClickAgregarUsuario(View v){
        if (!idET.getText().toString().equals("")){
            Usuario user = dbo.findAccount(Integer.parseInt(idET.getText().toString()));
            if (user != null){
                Intent intent = new Intent();
                intent.putExtra("id", user.getId());
                setResult(RESULT_OK, intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "El número de id ingresado no existe", Toast.LENGTH_SHORT).show();
            }
        }
            else{
            Toast.makeText(getApplicationContext(), "Favor ingresa un número de id", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRegistrarUsuario(View v){
        Intent intent = new Intent(AddUserActivity.this, SignUpActivity.class);
        intent.putExtra("registroinicial", false);
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
                Intent intent = new Intent();
                intent.putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }



}

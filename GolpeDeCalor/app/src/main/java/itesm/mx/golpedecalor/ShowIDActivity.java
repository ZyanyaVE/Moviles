package itesm.mx.golpedecalor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ShowIDActivity extends ActionBarActivity {

    // Declaración de variables
    private TextView idTV;
    Usuario usuarioPrincipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_id);

        // Referencias a objetos de interface
        idTV = (TextView) findViewById(R.id.idTV);

        usuarioPrincipal = (Usuario) getIntent().getParcelableExtra("Usuario");

        idTV.setText(String.valueOf(usuarioPrincipal.getId()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_id, menu);
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

    // Continuar en HomeActivity
    public void onClickContinuar(View v){
        Intent homeIntent = new Intent (ShowIDActivity.this, SelectGroupActivity.class);
        homeIntent.putExtra("Usuario", usuarioPrincipal);
        startActivity(homeIntent);

    }
}

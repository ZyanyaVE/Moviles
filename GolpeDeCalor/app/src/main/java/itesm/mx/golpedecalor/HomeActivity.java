package itesm.mx.golpedecalor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class HomeActivity extends ActionBarActivity {

    // Declaración de Variables
    TextView nombreTV, apellidosTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nombreTV = (TextView) findViewById(R.id.nombreTV);
        apellidosTV = (TextView) findViewById(R.id.apellidosTV);

        Bundle extras = getIntent().getExtras();
        Usuario user = new Usuario(extras.getString("nombre"), extras.getString("apellidos"),
                extras.getString("fecha"));

        nombreTV.setText(user.getNombre());
        apellidosTV.setText(user.getApellidos());
        //fechaNacimientoTV.setText(user.getFechaNacimiento());
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
}

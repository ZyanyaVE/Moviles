/*
    Golpe de Calor

    Copyright (C) 2015
    Marcelo Alberto Cantú Quiroga
    Zyanya Valdés Esquivel
    Hugo León Garza

    Última Modificación: 20 de Marzo del 2015
    Nombre del Archivo: ShowIDActivity.java
    Convención de nombres: "CamelCase"
    Versión 2.0

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

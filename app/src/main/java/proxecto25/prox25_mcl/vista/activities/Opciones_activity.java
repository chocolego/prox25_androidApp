package proxecto25.prox25_mcl.vista.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.modelo.Prefs;

public class Opciones_activity extends AppCompatActivity {

    SQLiteDatabase db;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //recuperamos opciones
        try {
            prefsHelper helper = new prefsHelper(getApplicationContext());
            db = helper.getWritableDatabase();
            prefsDAO prefsdao = new prefsDAO();
            prefs = prefsdao.getPreferences(db,getApplicationContext());

        }catch(Exception e){
            Toast.makeText(this, "Error recuperando preferencias.", Toast.LENGTH_SHORT).show();
        }finally{
            if (db != null && db.isOpen()) {
                try {
                    db.close();
                } catch (Exception e) {
                    Log.e("KO", "error cerrando la BD");
                }
            }
        }

        EditText et_ip = findViewById(R.id.et_ip);
        et_ip.setText(prefs.getIp());
        EditText et_port = findViewById(R.id.et_puerto);
        et_port.setText(prefs.getPort());

        //spinner opciones y eventos

        Spinner spinnerIdioma = findViewById(R.id.spin_idioma);
        String[] spinnerOptions = {"Galego", "Castellano", "English"};
        ArrayAdapter<String> adapter_idioma = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerOptions);
        spinnerIdioma.setAdapter(adapter_idioma);

        if(prefs.getLanguage().toUpperCase().equals("GA")){
            spinnerIdioma.setSelection(0);
        } else if(prefs.getLanguage().toUpperCase().equals("ES")){
            spinnerIdioma.setSelection(1);
        } else if(prefs.getLanguage().toUpperCase().equals("EN")){
            spinnerIdioma.setSelection(2);
        }

        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = spinnerOptions[position];
                if(selectedOption.equals("Castellano")){
                    prefs.setLanguage("ES");
                }else if(selectedOption.equals("Galego")){
                    prefs.setLanguage("GA");
                }else if(selectedOption.equals("English")){
                    prefs.setLanguage("EN");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_aceptar = findViewById(R.id.btn_aceptarop);
        Button btn_cancelar = findViewById(R.id.btn_atrasop);
        Button btn_resetPrefs = findViewById(R.id.btn_defecto);


        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    prefsHelper helper = new prefsHelper(getApplicationContext());
                    db = helper.getWritableDatabase();
                    prefs.setIp(et_ip.getText().toString());
                    prefs.setPort(et_port.getText().toString());
                    prefsDAO prefsdao = new prefsDAO();
                    prefsdao.setPreferences(db,getApplicationContext(),prefs);
                    //finish();

                    // Cambiar el idioma
                    if(prefs.getLanguage().toUpperCase().equals("ES")){
                        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("es-ES");
                        AppCompatDelegate.setApplicationLocales(appLocale);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else if(prefs.getLanguage().toUpperCase().equals("GA")){
                        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("gl-ES");
                        AppCompatDelegate.setApplicationLocales(appLocale);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else if(prefs.getLanguage().toUpperCase().equals("EN")){
                        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("en-EN");
                        AppCompatDelegate.setApplicationLocales(appLocale);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(Opciones_activity.this, R.string.error_reinicioprefs, Toast.LENGTH_SHORT).show();
                }finally{
                    if (db != null && db.isOpen()) {
                        try {
                            db.close();
                        } catch (Exception e) {
                            Log.e("KO", "error cerrando la BD");
                        }
                    }
                    finish();
                }
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_resetPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    prefsHelper helper = new prefsHelper(getApplicationContext());
                    db = helper.getWritableDatabase();
                    prefsDAO prefsdao = new prefsDAO();
                    prefsdao.defaultPreferencesparaVacio(db, getApplicationContext());
                }catch(Exception e){
                    Toast.makeText(Opciones_activity.this, "Error reseteando preferencias.", Toast.LENGTH_SHORT).show();
                }finally{
                    if (db != null && db.isOpen()) {
                        try {
                            db.close();
                        } catch (Exception e) {
                            Log.e("KO", "error cerrando la BD");
                        }
                    }
                    finish();
                }
            }
        });
    }
}
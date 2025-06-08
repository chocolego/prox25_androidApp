package proxecto25.prox25_mcl.vista.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.util.UtilController;
import proxecto25.prox25_mcl.modelo.Prefs;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        //0. TABLET LAYOUT
//        // Comprobar si tablet o telefono
//        boolean isTablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;
//        if (isTablet) {
//            Log.d("DeviceCheck", " tablet");
//        } else {
//            Log.d("DeviceCheck", "telefono");
//        }
//        setContentView(R.layout.activity_main);

        //1.cargar imagenes y ajustes por defecto en BD
        try{

            prefsHelper helper = new prefsHelper(getApplicationContext());
            db = helper.getWritableDatabase();
            prefsDAO prefDAO = new prefsDAO();
            prefDAO.defaultPreferencesparaVacio(db, getApplicationContext());

        }catch(Exception e){
            Toast.makeText(this, R.string.error_reinicioprefs, Toast.LENGTH_SHORT).show();
        }finally{
        }

        //test
//        try {
//            prefsHelper helper = new prefsHelper(getApplicationContext());
//            db = helper.getWritableDatabase();
//            prefsDAO prefsDAO = new prefsDAO();
//            Prefs prefs = prefsDAO.getPreferences(db, getApplicationContext());
////            Toast.makeText(this,prefs.getName() , Toast.LENGTH_SHORT).show();
////            Toast.makeText(this,prefs.getPass() , Toast.LENGTH_SHORT).show();
////            Toast.makeText(this,prefs.getIp() , Toast.LENGTH_SHORT).show();
////            Toast.makeText(this,prefs.getPort() , Toast.LENGTH_SHORT).show();
////            Toast.makeText(this,prefs.getLanguage() , Toast.LENGTH_SHORT).show();
//            //Toast.makeText(this,"1imagen"+prefs.getImagenprof() , Toast.LENGTH_SHORT).show();
//        }catch(Exception e){
//            Toast.makeText(this, "Error cargando prefs", Toast.LENGTH_SHORT).show();
//        }finally{
//            db.close();
//        }




        //2.si hay idioma guardado, si no local de dispositivo


        //3. boton opciones

        Button btn_opciones = findViewById(R.id.btn_opcion);
        btn_opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Opciones_activity.class);
                startActivity(intent);
            }
        });

        Button btn_entrar = findViewById(R.id.btn_login);
        EditText et_user = findViewById(R.id.et_user);
        EditText et_pw = findViewById(R.id.et_pass);
        //ProgressBar progressBar = findViewById(R.id.progressBar);

        //01. LOGIN

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //comprueba valores introducidos antes
                String nombre = et_user.getText().toString().trim();
                String password = et_pw.getText().toString().trim();

                if (nombre.length() < 3 || nombre.length() > 20) {
                    Toast.makeText(MainActivity.this, "El nombre debe tener entre 3 y 20 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 3 || password.length() > 20) {
                    Toast.makeText(MainActivity.this, "La contraseña debe tener entre 3 y 20 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressBar progressBar = findViewById(R.id.progressBar);

                progressBar.setVisibility(View.VISIBLE);

                try{

                    prefsHelper helper = new prefsHelper(getApplicationContext());
                    db = helper.getWritableDatabase();
                    prefsDAO prefsDAO = new prefsDAO();
                    Prefs pre = prefsDAO.getPreferences(db, getApplicationContext());

//                    String ip = "10.0.2.2";         // your server IP (localhost for emulator)
//                    int port = 8080;                // your server port
//                    String apiMethod = "usuarios/login";  // your API endpoint
//
//                    String url = "http://" + ip + ":" + port + "/" + apiMethod;

                    //OkHttpClient client = new OkHttpClient();

                    //gestionar tiempo de espera
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(5, TimeUnit.SECONDS) // connexion a servidor
                            .readTimeout(5, TimeUnit.SECONDS)    // respuesta
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .build();


                    MediaType JSON = MediaType.get("application/json; charset=utf-8");

                    String json = "{"
                            + "\"user\":\"" + et_user.getText().toString() + "\","
                            + "\"pass\":\"" + et_pw.getText().toString() + "\""
                            + "}";

//                    RequestBody body = RequestBody.create(json, JSON);
//                    Request request = new Request.Builder()
//                            .url(url)
//                            .post(body)
//                            .build();

                    /////

//                    OkHttpClient client = new OkHttpClient();
//
//                    MediaType JSON = MediaType.get("application/json; charset=utf-8");
//                    String json = "{"
//                            + "\"user\":\"" + et_mail.getText().toString() + "\","
//                            + "\"pass\":\"" + et_pw.getText().toString() + "\""
//                            + "}";
//
                    RequestBody body = RequestBody.create(json, JSON);
                    Request request = new Request.Builder()
                            .url(UtilController.getBaseConnectionString(getApplicationContext(),db, "usuarios/login"))
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            // TIMEOUT
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);

                                if (e instanceof java.net.SocketTimeoutException) {
                                    Toast.makeText(MainActivity.this, "Tiempo de espera agotado. El servidor no respondió.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Log.e("ERROR", "No se pudo conectar." + e.toString());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            String responseData = "";
                            if (response.body() != null) {
                                responseData = response.body().string();
                            }

                            final String finalResponseData = responseData;

                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);});

                            if(response.isSuccessful()){
                                //responseData = response.body().string();
                                //DEBUG
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Login correcto!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //LOGIN OK, PASA A PERFILUSUARIO
                                Log.d("OK", "Logeado con exito!");
                                Intent intent = new Intent(MainActivity.this, Perfil_activity.class);

                                // Almacenar ID usuario
                                UtilController.setUserIdPreferences(getBaseContext(),responseData);
                                startActivity(intent);

                            }else{
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if ("inactivo.".equalsIgnoreCase(finalResponseData.trim())) {
                                            Toast.makeText(MainActivity.this, "Tu usuario no está activo. Contacta con soporte.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Login incorrecto.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Log.e("KO", "Conectado pero no logeado");
                            }
                        }
                    });

                }catch(Exception e){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "Error de login", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN_ERROR", "Error durante login", e);
                    Toast.makeText(MainActivity.this, "Error de login: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
//                finally{
//                    if (db != null && db.isOpen()) {
//                        try {
//                            db.close();
//                        } catch (Exception e) {
//                            Log.e("KO", "error cerrando la BD");
//                        }
//                    }
//                }


            }
        });




    }

    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.e("KO", "error cerrando la BD");
            }
        }
        super.onDestroy();
    }

    //menu

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu01, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item clicks
        int id = item.getItemId();
        if (id == R.id.mop01) {
            //opcion ir a opciones conex/idioma
            return true;
        }
        if (id == R.id.mop02) {
            //opcion sincro
            return true;
        }
        if (id == R.id.mop03) {
            //opcion salir
//            Intent i = new Intent(MainActivity.this, ListadoActoresActivity.class);
//            startActivity(i);
//            finish();
            return true;
        }
        return onOptionsItemSelected(item);
    }

    public String getUserPreferredLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("preferencias", MODE_PRIVATE);
        String idiomaprefs = prefs.getString("idioma", "");
        if (idiomaprefs.isEmpty()) {
            idiomaprefs = Locale.getDefault().getLanguage(); // idioma de dispositivo
        }
        return idiomaprefs;
    }

}

package proxecto25.prox25_mcl.vista.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.modelo.Dispositivos;
import proxecto25.prox25_mcl.modelo.Usuarios;
import proxecto25.prox25_mcl.util.UtilController;
import proxecto25.prox25_mcl.vista.adaptadores.DispositivoAdapter;
import proxecto25.prox25_mcl.vista.fragments.listaDispositivos_fragment;
import proxecto25.prox25_mcl.vista.fragments.dispositivoDetalles_fragment;

public class Perfil_activity extends AppCompatActivity {

    SQLiteDatabase db;
    private prefsDAO prefsdao;
    //Usuarios usuario;
    Button btn_editar;
    Button btn_atras;
    Button btn_gestion;
    TextView tv_datos;
    ImageView imgperf;
    ArrayList<Dispositivos> dispositivosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_editar = findViewById(R.id.btn_editar);
        btn_atras = findViewById(R.id.btn_atrasperf);
        btn_gestion = findViewById(R.id.btn_gestiontargets);
        tv_datos = findViewById(R.id.tv_datos);
        imgperf = findViewById(R.id.imagenperfil);
        //ListView listadisp = findViewById(R.id.perfil_list_disp);

        //cargar datos usuario Sharedpreferences

        try {
            prefsHelper helper = new prefsHelper(Perfil_activity.this);
            db = helper.getWritableDatabase();
            prefsdao = new prefsDAO();


            String id = UtilController.getUserIdFromPreferences(Perfil_activity.this);

            String url = UtilController.getBaseConnectionString(Perfil_activity.this, db, "usuarios/"+id);
            Integer idusuario = Integer.parseInt(id);

            ///cargar imaxe

            String stringimg = prefsdao.getPreferences(db, this).getImagenprof();
            Bitmap bitmap = stringToBitmap(stringimg);

            if (bitmap != null) {
                imgperf.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "NO hay imagen de perfil guardada.", Toast.LENGTH_SHORT).show();
            }

            //cargar datos usuario
            cargarUsuario(Perfil_activity.this, url);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID de usuario inválido", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Error en la base de datos", Toast.LENGTH_SHORT).show();
            Log.e("Perfil_activity", "SQLiteException", e);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Error inesperado: dato nulo", Toast.LENGTH_SHORT).show();
            Log.e("Perfil_activity", "NullPointerException", e);
        } catch (Exception e) {
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show();
            Log.e("Perfil_activity", "Exception", e);
        }



            //cargar dispositivos (solos activos) en fragment (listadispositivos)
            listaDispositivos_fragment fragment = listaDispositivos_fragment.newInstance("param1", "param2");
//
//
//// Use FragmentManager to start a transaction
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_perfil, fragment)
                    .addToBackStack(null)  // Optional: add to backstack so user can navigate back
                    .commit();

            //Cargar dispositivos (solos activos) en listview
         //cargarDispositivos(Perfil_activity.this, url2);
//        DispositivoAdapter adapter = new DispositivoAdapter(Perfil_activity.this, dispositivosList);
//        listadisp.setAdapter(adapter);
//
//            // Build dynamic endpoint string
//        try {
//            prefsHelper helper = new prefsHelper(Perfil_activity.this);
//            db = helper.getWritableDatabase();
//            prefsdao = new prefsDAO();
//
//
//            String id = UtilController.getUserIdFromPreferences(Perfil_activity.this);
//            //String endpoint = "usuarios/" + id + "/dispositivos/activos";
//            String endpoint = "dispositivos/usuarios/" + id + "/dispositivos/activos";
//
//
//// Build full URL
//            String url2 = UtilController.getBaseConnectionString(Perfil_activity.this, db, endpoint);
//
//           // cargarDispositivos(Perfil_activity.this, url2);
//            //DispositivoAdapter adapter = new DispositivoAdapter(Perfil_activity.this, dispositivosList);
//            //listadisp.setAdapter(adapter);
//
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(url2)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    Log.e("ERROR", "No se pudo conectar: " + e.getMessage());
//                    runOnUiThread(() -> Toast.makeText(Perfil_activity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    String responseData="";
//                    try {
//                        if (response.isSuccessful() && response.body() != null) {
//                            responseData = response.body().string();
//
//                            Gson gson = new Gson();
//                            Type listType = new TypeToken<List<Dispositivos>>(){}.getType();
//                            dispositivosList = gson.fromJson(responseData, listType);
//
//                            runOnUiThread(() -> {
//                                //ListView listView = findViewById(R.id.perfil_list_disp);
//
//                                DispositivoAdapter adapter = new DispositivoAdapter(Perfil_activity.this, dispositivosList);
//                                listadisp.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//
//
//                            });
//
//                        } else {
//                            runOnUiThread(() -> Toast.makeText(Perfil_activity.this, "Error respuesta" + "Error: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show());
//                            Log.e("ERROR", "Respuesta no exitosa: " + response.code());
//                        }
//                    } catch (Exception e) {
//                        runOnUiThread(() -> Toast.makeText(Perfil_activity.this, "Error eparse json", Toast.LENGTH_SHORT).show());
//
//                        Log.e("ERROR", "Error parseando JSON", e);
//                    }
//                }
//            });}
//             catch (NumberFormatException e) {
//                Toast.makeText(this, "ID de usuario inválido", Toast.LENGTH_SHORT).show();
//            } catch (SQLiteException e) {
//                Toast.makeText(this, "Error en la base de datos", Toast.LENGTH_SHORT).show();
//                Log.e("Perfil_activity", "SQLiteException", e);
//            } catch (NullPointerException e) {
//                Toast.makeText(this, "Error inesperado: dato nulo", Toast.LENGTH_SHORT).show();
//                Log.e("Perfil_activity", "NullPointerException", e);
//            } catch (Exception e) {
//                Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show();
//                Log.e("Perfil_activity", "Exception", e);
//            }
//
//        listadisp.setOnItemClickListener((parent, view, position, id) -> {
//            Dispositivos selectedDispositivo = dispositivosList.get(position);
//
//            Intent intent = new Intent(Perfil_activity.this, dispositivoDetalle_activity.class);
//            intent.putExtra("dispositivo_id", selectedDispositivo.getId());
//            startActivity(intent);
//        });






        //boton editar
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil_activity.this, UsuarioDatos_activity.class);
                startActivity(intent);
            }
        });

        //boton volver
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //boton gestionar targets (asignar target libre a dispositivo libre)

        btn_gestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil_activity.this, GestionTargets_activity.class);
                startActivity(intent);
            }
        });




    }

    public Bitmap stringToBitmap(String imgstring) {
        try {
            byte[] decodedBytes = Base64.decode(imgstring, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("Base64ToBitmap", "Error convirtiendo Base64 string", e);
            return null;
        }
    }



    private void cargarUsuario(Context context, String url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                      Log.e("ERROR", "No se pudo conectar: " + e.getMessage());
                      runOnUiThread(() -> Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                 try {
                     if (response.isSuccessful() && response.body() != null) {
                         String responseData = response.body().string();
                         Gson gson = new Gson();
                         Usuarios usuario = gson.fromJson(responseData, Usuarios.class);

                         //JSONObject json = new JSONObject(jsonData);

                         //se non coincide e da problemas
//
//                         int id = json.getInt("id");
//                         String nombreUsuario = json.optString("nombreUsuario", "");
//                         String nombreDisplay = json.optString("nombreDisplay", "");
//                         String email = json.optString("email", "");
//                         String telefono = json.optString("telefono", "");
//
//                         // Only include the fields you care about
//                         Usuario usu = new Usuario();
//                         usu.setId(id);
//                         usu.setNombreUsuario(nombreUsuario);
//                         usu.setNombreDisplay(nombreDisplay);
//                         usu.setEmail(email);
//                         usu.setTelefono(telefono);

                         runOnUiThread(() -> {
                             // refrescar UI
                             //cargar datos tv
                             String datoscargar = "";
                             StringBuilder sb = new StringBuilder();
                             sb.append("Nombre: ").append(usuario.getNombreUsuario()).append("\n");
                             sb.append("Display: ").append(usuario.getNombreDisplay()).append("\n");
                             sb.append("Email: ").append(usuario.getEmail()).append("\n");
                             sb.append("Teléfono: ").append(usuario.getTelefono());
                             datoscargar = sb.toString();

                             TextView tv_datos = findViewById(R.id.tv_datos);
                             tv_datos.setText(datoscargar + "");

                         });

                     } else {
                         Log.e("ERROR", "Respuesta no exitosa: " + response.code());
                     }
                 } catch (Exception e) {
                     Log.e("ERROR", "Error parseando JSON", e);
                 }
            }
        });
    }

//    private void cargarDispositivos(Context context, String url) {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("ERROR", "No se pudo conectar: " + e.getMessage());
//                runOnUiThread(() -> Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String responseData = response.body().string();
//
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Dispositivos>>(){}.getType();
//                        List<Dispositivos> dispositivosList = gson.fromJson(responseData, listType);
//
//                        runOnUiThread(() -> {
//                            ListView listView = findViewById(R.id.perfil_list_disp);
//
//                            DispositivoAdapter adapter = new DispositivoAdapter(Perfil_activity.this, dispositivosList);
//                            listView.setAdapter(adapter);
//
//                            listView.setOnItemClickListener((parent, view, position, id) -> {
//                                Dispositivos selectedDispositivo = dispositivosList.get(position);
//
//                                Intent intent = new Intent(Perfil_activity.this, dispositivoDetalle_activity.class);
//                                intent.putExtra("dispositivo_id", selectedDispositivo.getId());
//                                startActivity(intent);
//                            });
//                        });
//
//                    } else {
//                        Log.e("ERROR", "Respuesta no exitosa: " + response.code());
//                    }
//                } catch (Exception e) {
//                    Log.e("ERROR", "Error parseando JSON", e);
//                }
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.e("KO", "error cerrando BD.");
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar datos usuario
        String id = UtilController.getUserIdFromPreferences(Perfil_activity.this);

        String url = UtilController.getBaseConnectionString(Perfil_activity.this, db, "usuarios/"+id);
        cargarUsuario(Perfil_activity.this, url);
        cargarImagenPerfil();
    }

    private void cargarImagenPerfil() {
        try {
            String stringimg = prefsdao.getPreferences(db, this).getImagenprof();
            Bitmap bitmap = stringToBitmap(stringimg);
            if (bitmap != null) {
                imgperf.setImageBitmap(bitmap);
            } else {
                Log.d("Perfil_activity", "Imagen de perfil vacía.");
            }
        } catch (Exception e) {
            Log.e("Perfil_activity", "Error cargando imagen de perfil en onResume", e);
        }
    }
}





package proxecto25.prox25_mcl.vista.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.modelo.Dispositivos;
import proxecto25.prox25_mcl.modelo.Prefs;
import proxecto25.prox25_mcl.modelo.Usuarios;
import proxecto25.prox25_mcl.util.UtilController;
import proxecto25.prox25_mcl.vista.adaptadores.ImaxenAdapter;

public class UsuarioDatos_activity extends AppCompatActivity {

    SQLiteDatabase db;
    private prefsDAO prefsdao;
    ImaxenAdapter adapter;
    Usuarios usuarioold;
    Button btn_guardar;
    Button btn_atras;
    ImageView imgperf;
    String imaxeSeleccionada ="";
    Prefs prefs;
    Usuarios updatedUsuario;
    Usuarios retornoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_datos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btn_editar = findViewById(R.id.btn_editar);
        Button btn_atras= findViewById(R.id.btn_atrasusu);
        ImageView imgperf = findViewById(R.id.imagenperfil);
        EditText et_nombre = findViewById(R.id.et_nombre);
        EditText et_mail = findViewById(R.id.et_user);
        EditText et_pass = findViewById(R.id.et_pass);
        EditText et_tel = findViewById(R.id.et_tel);
        ListView listaimagenes = findViewById(R.id.list_images);

        //01. Cargar datos usuario

        try {
            prefsHelper helper = new prefsHelper(UsuarioDatos_activity.this);
            db = helper.getWritableDatabase();
            prefsdao = new prefsDAO();


            String id = UtilController.getUserIdFromPreferences(UsuarioDatos_activity.this);

            String url = UtilController.getBaseConnectionString(UsuarioDatos_activity.this, db, "usuarios/"+id);
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
            cargarUsuario(UsuarioDatos_activity.this, url);


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


        //02.cargar imagenes /assets usando streams
        try {

            prefsdao = new prefsDAO();

            List<String> robots = getlistaImaxesRobotsAssets(UsuarioDatos_activity.this);

            for (String name : robots) {
                Log.d("ASSETS", "Found: " + name);
            }

            List<Bitmap> bitmaps = cargarImaxesfromAssets(UsuarioDatos_activity.this, robots); //streams
            adapter = new ImaxenAdapter(UsuarioDatos_activity.this, bitmaps);
            listaimagenes.setAdapter(adapter);

            Log.d("DEBUG", "Bitmaps loaded: " + bitmaps.size());

            listaimagenes.setOnItemClickListener((parent, view, position, id) -> {
                Bitmap selected = bitmaps.get(position);
                String filename = robots.get(position);
                imgperf.setImageBitmap(selected);
                imaxeSeleccionada = bitmapToString(selected); // para gardar despois
            });

        } catch (Exception e) {
            Toast.makeText(UsuarioDatos_activity.this, "Error inesperado cargando imágenes", Toast.LENGTH_SHORT).show();
            Log.e("GeneralError", "Excepción en carga de imágenes", e);
        }

        //03. Guardar cambios datos ususario (service)  + imagen perfil (sqlite)
            btn_editar.setOnClickListener(v -> {

                //comprobar formato datos
                String nombrenovo = et_nombre.getText().toString().trim();
                String emailnovo = et_mail.getText().toString().trim();
                String telnovo = et_tel.getText().toString().trim();
                String passnovo = et_pass.getText().toString().trim();

                if (nombrenovo.length() < 3 || nombrenovo.length() > 20) {
                    Toast.makeText(this, "El nombre debe tener entre 3 y 20 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailnovo.length() < 9 || emailnovo.length() > 50 || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailnovo).matches()) {
                    Toast.makeText(this, "Introduce un email válido entre 9 y 50 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!telnovo.isEmpty() || !telnovo.isBlank()) {

                if (telnovo.length() < 6 || telnovo.length() > 20 || !telnovo.matches("\\+?[0-9]+")) {
                    Toast.makeText(this, "El teléfono debe tener entre 6 y 20 dígitos y solo números", Toast.LENGTH_SHORT).show();
                    return;
                }}

                if (!passnovo.isEmpty() || !passnovo.isBlank()) {
                    if (passnovo.length() < 6 || passnovo.length() > 25 ||
                            !passnovo.matches(".*[a-z].*") ||      // minúscula
                            !passnovo.matches(".*[A-Z].*") ||      // mayúscula
                            !passnovo.matches(".*\\d.*") ||        // número
                            !passnovo.matches(".*[^a-zA-Z0-9].*")) // carácter especial
                    {
                        Toast.makeText(this, "La contraseña debe tener entre 6 y 25 caracteres y contener mayúscula, minúscula, número y símbolo", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                try {
                    prefs = prefsdao.getPreferences(db, UsuarioDatos_activity.this); // prefs que ya existen
                    prefs.setImagenprof(imaxeSeleccionada);
                    prefsdao.setPreferences(db, UsuarioDatos_activity.this, prefs);  //imagen guardada en prefs update

                    Toast.makeText(UsuarioDatos_activity.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(UsuarioDatos_activity.this, "Error guardando imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("GuardarImagen", "Error al guardar imagen", e);
                }

                //Recuperar usuario completo por id y guardar cambios en bd service
                updatedUsuario = new Usuarios(usuarioold.getId(),
                        et_nombre.getText().toString().trim(),
                        usuarioold.getNombreDisplay(),
                        et_mail.getText().toString().trim(),
                        et_pass.getText().toString().trim(),
                        et_tel.getText().toString().trim(),
                        usuarioold.getDispostivosAsignados());

                String id = UtilController.getUserIdFromPreferences(UsuarioDatos_activity.this);

                String urlupdate = UtilController.getBaseConnectionString(UsuarioDatos_activity.this, db, "usuarios/" + id);
                Integer idusuario = Integer.parseInt(id);


                actualizarUsuarioREST(urlupdate, UsuarioDatos_activity.this);

            });

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



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
                        usuarioold = gson.fromJson(responseData, Usuarios.class);

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
                            //cargar datos usuario
                            EditText et_nombre = findViewById(R.id.et_nombre);
                            EditText et_mail = findViewById(R.id.et_user);
                            EditText et_pass = findViewById(R.id.et_pass);
                            EditText et_tel = findViewById(R.id.et_tel);

                            //cargar ets
                            et_nombre.setText(usuarioold.getNombreUsuario() != null ? usuarioold.getNombreUsuario() : "");
                            et_mail.setText(usuarioold.getEmail() != null ? usuarioold.getEmail() : "");
                            et_tel.setText(usuarioold.getTelefono() != null ? usuarioold.getTelefono() : "");


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

    private void actualizarUsuarioREST(String url, Context context) {

        if (updatedUsuario == null) {
            Toast.makeText(this, "Usuario no cargado aún", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        String jsonUsuario = gson.toJson(updatedUsuario);

        RequestBody body = RequestBody.create(jsonUsuario, okhttp3.MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(context, "Error actualizando usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    retornoUsuario = gson.fromJson(responseBody, Usuarios.class);

                    runOnUiThread(() -> {
                        Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();

                        // update UI with updatedUsuario data:
//                        EditText et_nombre = findViewById(R.id.et_nombre);
//                        EditText et_mail = findViewById(R.id.et_user);
//                        EditText et_tel = findViewById(R.id.et_tel);
//
//                        et_nombre.setText(retornoUsuario.getNombreUsuario() != null ? retornoUsuario.getNombreUsuario() : "");
//                        et_mail.setText(retornoUsuario.getEmail() != null ? retornoUsuario.getEmail() : "");
//                        et_tel.setText(retornoUsuario.getTelefono() != null ? retornoUsuario.getTelefono() : "");
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(context, "Error actualizando usuario. Código: " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }


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

    private List<String> getlistaImaxesRobotsAssets(Context context) {
        List<String> robotImages = new ArrayList<>();
        try {
            String[] files = context.getAssets().list("");
            for (String file : files) {
                if (file.startsWith("robot") && file.endsWith(".png")) {
                    robotImages.add(file);
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error cargando imagenes", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return robotImages;
    }

    private List<Bitmap> cargarImaxesfromAssets(Context context, List<String> ficheros) {
        List<Bitmap> bitmaps = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        for (String filename : ficheros) {
            try (InputStream is = assetManager.open(filename)) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bitmaps.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UsuarioDatos_activity.this, "Error leyendo imagenes.", Toast.LENGTH_SHORT).show();
            }
        }
        return bitmaps;
    }

    void saveUserImageProfileToPrefs(String imageName) {
        try {
            prefsHelper helper = new prefsHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            prefsDAO prefsDAO = new prefsDAO();
            Prefs prefs = prefsDAO.getPreferences(db, getApplicationContext());
            prefs.setImagenprof(imageName);  // or your field name for profile image
            prefsDAO.setPreferences(db, UsuarioDatos_activity.this, prefs); // You must implement this update method
            db.close();
            Toast.makeText(this, "Profile image saved locally", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving image to prefs: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}
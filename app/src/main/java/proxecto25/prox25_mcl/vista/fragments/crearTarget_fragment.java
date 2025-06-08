package proxecto25.prox25_mcl.vista.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
import proxecto25.prox25_mcl.modelo.Targets;
import proxecto25.prox25_mcl.util.UtilController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crearTarget_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crearTarget_fragment extends Fragment {

    Button btn_crear;
    private EditText et_nombre, et_desc;
    private Spinner spinnerTipo;
    Context context;
    SQLiteDatabase db;
    private prefsDAO prefsdao;

    public crearTarget_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment crearTarget_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static crearTarget_fragment newInstance() {
        crearTarget_fragment fragment = new crearTarget_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_target, container, false);
        context = requireContext();

        et_nombre = view.findViewById(R.id.et_nombretarget);
        et_desc = view.findViewById(R.id.et_desc);
        spinnerTipo = view.findViewById(R.id.spinner_tipo);
        btn_crear = view.findViewById(R.id.btn_crear);

        // carga tipos en spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Tipo", "animal", "vehiculo", "otro"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        btn_crear.setOnClickListener(v -> {
            String nombre = et_nombre.getText().toString().trim();
            String descripcion = et_desc.getText().toString().trim();
            String tipo = spinnerTipo.getSelectedItem().toString();

            // ValidaCions
            if (nombre.length() < 5 || nombre.length() > 20) {
                Toast.makeText(getContext(), "Nombre debe tener entre 5 y 20 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (descripcion.length() > 100) {
                Toast.makeText(getContext(), "Descripcion hasta 100 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spinnerTipo.getSelectedItemPosition() == 0) {
                Toast.makeText(getContext(), "Seleccione un tipo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // novo target
            Targets nuevoTarget = new Targets(nombre, descripcion, tipo);

            // Prepare JSON manually
//            JSONObject targetJson = new JSONObject();
//            try {
//                targetJson.put("nombre", nombre);
//                targetJson.put("descripcion", descripcion);
//                targetJson.put("tipo", tipo);
//            } catch (JSONException e) {
//                Toast.makeText(getContext(), "Error al construir JSON", Toast.LENGTH_SHORT).show();
//                return;
//            }

            // 01. Comprobar que el nombre no esta en uso

            prefsHelper helper = new prefsHelper(requireActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            String getUrl = UtilController.getBaseConnectionString(getContext(), db, "targets/nombre/" + nombre);

            OkHttpClient client = new OkHttpClient();
            Request getRequest = new Request.Builder()
                    .url(getUrl)
                    .build();

            client.newCall(getRequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al verificar nombre: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    requireActivity().runOnUiThread(() -> {
                        try {
                            if (response.isSuccessful()) {
                                // Si ya existe, no se continúa
                                String responseBody = response.body().string();
                                if (responseBody != null && !responseBody.isEmpty() && !responseBody.equals("null")) {
                                    Toast.makeText(getContext(), "El nombre ya está en uso", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Name is not in use, proceed with POST
                                insertarNuevoTarget(nombre, descripcion, tipo, db);

                            } else {
                                Toast.makeText(getContext(), "Error al verificar nombre: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Error al leer la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        return view;
    }

    private void insertarNuevoTarget(String nombre, String descripcion, String tipo, SQLiteDatabase db) {
            String postUrl = UtilController.getBaseConnectionString(getContext(), db, "targets");

            JSONObject jsonTarget = new JSONObject();
            try {
                jsonTarget.put("nombre", nombre);
                jsonTarget.put("descripcion", descripcion);
                jsonTarget.put("tipoTarget", tipo);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(
                    jsonTarget.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request postRequest = new Request.Builder()
                    .url(postUrl)
                    .post(body)
                    .build();

            client.newCall(postRequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al crear target: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    requireActivity().runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Target creado correctamente", Toast.LENGTH_SHORT).show();
                            et_nombre.setText("");
                            et_desc.setText("");
                            spinnerTipo.setSelection(0);
                        } else {
                            Toast.makeText(getContext(), "Error al crear target: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    @Override
    public void onDestroy() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.e("KO", "error cerrando BD.");
            }
        }
        super.onDestroy();
    }

    }
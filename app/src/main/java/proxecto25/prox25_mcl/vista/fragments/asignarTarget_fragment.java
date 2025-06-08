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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.modelo.Dispositivos;
import proxecto25.prox25_mcl.modelo.Targets;
import proxecto25.prox25_mcl.util.UtilController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link asignarTarget_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class asignarTarget_fragment extends Fragment {

    Button btn_asignado;
    Context context;
    SQLiteDatabase db;
    private prefsDAO prefsdao;
    private Spinner spinDisp;
    private AutoCompleteTextView autoDisp;
    private Spinner spinTar;
    private AutoCompleteTextView autoTar;
    Dispositivos seleccionDisp;
    Targets seleccionTar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public asignarTarget_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment asignarTarget_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static asignarTarget_fragment newInstance(String param1, String param2) {
        asignarTarget_fragment fragment = new asignarTarget_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignar_target_fragment, container, false);
        context = requireContext();

        spinDisp = view.findViewById(R.id.spin_disp);
        autoDisp = view.findViewById(R.id.autoctv_dis);
        spinTar = view.findViewById(R.id.spin_tar);
        autoTar = view.findViewById(R.id.autoctv_tar);

        cargarDispositivosLibres();

        cargarTargetsLibres();

        btn_asignado = view.findViewById(R.id.btn_asignado);
        btn_asignado.setOnClickListener(v -> {
            if (seleccionTar != null && seleccionTar != null) {
                asignacion(seleccionTar.getId(), seleccionDisp.getId() );
            } else {
                Toast.makeText(context, "Seleccione un Target y un Dispositivo", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void cargarDispositivosLibres() {
        try {
            prefsHelper helper = new prefsHelper(context);
            db = helper.getWritableDatabase();


            String idUsuario = UtilController.getUserIdFromPreferences(context);
            String endpoint = "dispositivos/usuarios/" + idUsuario + "/dispositivos/libres";
            String url = UtilController.getBaseConnectionString(context, db, endpoint);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Error cargando dispositvos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();

                        Type listType = new TypeToken<List<Dispositivos>>() {
                        }.getType();
                        List<Dispositivos> dispositivosLibres = new Gson().fromJson(responseData, listType);

                        requireActivity().runOnUiThread(() -> {
                            if (dispositivosLibres != null && !dispositivosLibres.isEmpty()) {

                                //spinner
                                ArrayAdapter<Dispositivos> adapter = new ArrayAdapter<>(
                                        context,
                                        android.R.layout.simple_spinner_item,
                                        dispositivosLibres
                                );
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinDisp.setAdapter(adapter);

                                spinDisp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        seleccionDisp = dispositivosLibres.get(position);

                                        Toast.makeText(context, "Seleccionado: " + seleccionDisp.getNombreDispositivo(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                                //autocomplete
                                // AutoCompleteTextView adapter
                                ArrayAdapter<Dispositivos> autoDispAdapter = new ArrayAdapter<>(
                                        context,
                                        android.R.layout.simple_dropdown_item_1line,
                                        dispositivosLibres
                                );
                                autoDisp.setAdapter(autoDispAdapter);

                                autoDisp.setOnItemClickListener((parent, view, position, id) -> {
                                    seleccionDisp = (Dispositivos) parent.getItemAtPosition(position);
                                    Toast.makeText(context, "Seleccionado: " + seleccionDisp.getNombreDispositivo(), Toast.LENGTH_SHORT).show();
                                });


                            } else {
                                requireActivity().runOnUiThread(() ->
                                        Toast.makeText(context, "No hay dispositivos libres", Toast.LENGTH_SHORT).show());
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(context, "Error en respuesta: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Log.e("AsignarFrag", "Error cargando libres", e);
            Toast.makeText(context, "Error interno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarTargetsLibres() {
        try {
            prefsHelper helper = new prefsHelper(context);
            db = helper.getWritableDatabase();

            String endpoint = "targets/freeTargets";
            String url = UtilController.getBaseConnectionString(context, db, endpoint);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Error cargando targets: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();

                        Type listType = new TypeToken<List<Targets>>() {}.getType();
                        List<Targets> targetsLibres = new Gson().fromJson(responseData, listType);

                        requireActivity().runOnUiThread(() -> {
                            if (targetsLibres != null && !targetsLibres.isEmpty()) {
                                // Spinner
                                ArrayAdapter<Targets> spinnerAdapter = new ArrayAdapter<>(
                                        context,
                                        android.R.layout.simple_spinner_item,
                                        targetsLibres
                                );
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinTar.setAdapter(spinnerAdapter);

                                spinTar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        seleccionTar = targetsLibres.get(position);
                                        Toast.makeText(context, "Target seleccionado: " + seleccionTar.getNombre(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });

                                // AutoCompleteTextView
                                ArrayAdapter<Targets> autoAdapter = new ArrayAdapter<>(
                                        context,
                                        android.R.layout.simple_dropdown_item_1line,
                                        targetsLibres
                                );

                                autoTar.setAdapter(autoAdapter);

                                autoTar.setOnItemClickListener((parent, view, position, id) -> {
                                    seleccionTar = (Targets) parent.getItemAtPosition(position);
                                    Toast.makeText(context, "Seleccionado: " + seleccionTar.getNombre(), Toast.LENGTH_SHORT).show();
                                });

                            } else {
                                Toast.makeText(context, "No hay targets libres", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(context, "Error en respuesta: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Log.e("AsignarFrag", "Error cargando targets", e);
            Toast.makeText(context, "Error interno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void asignacion(int targetId, int dispositivoId) {
        try {
            prefsHelper helper = new prefsHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();

            String endpoint = "targets/" + targetId + "/asignar/" + dispositivoId;
            String url = UtilController.getBaseConnectionString(context, db, endpoint);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null)) // Empty body
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Error al asignar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    requireActivity().runOnUiThread(() -> {
                        if (response.isSuccessful()) {

                            //recargar despues de insertar (dispositivo y target ya noe stan libres)
                            Toast.makeText(context, "Asignación exitosa", Toast.LENGTH_SHORT).show();

                            // RELOAD both lists
                            cargarDispositivosLibres();
                            cargarTargetsLibres();

                        } else {
                            Toast.makeText(context, "Error en la asignación: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("AsignarTarget", "Error interno", e);
            Toast.makeText(context, "Error interno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroyView() {
        if (db != null && db.isOpen()) db.close();
        super.onDestroyView();
    }
}
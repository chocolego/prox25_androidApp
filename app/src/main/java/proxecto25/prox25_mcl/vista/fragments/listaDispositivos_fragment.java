package proxecto25.prox25_mcl.vista.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.bbdd.prefsHelper;
import proxecto25.prox25_mcl.modelo.Dispositivos;
import proxecto25.prox25_mcl.modelo.Targets;
import proxecto25.prox25_mcl.util.UtilController;
import proxecto25.prox25_mcl.vista.activities.Perfil_activity;
import proxecto25.prox25_mcl.vista.activities.dispositivoDetalle_activity;
import proxecto25.prox25_mcl.vista.adaptadores.DispositivoAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link listaDispositivos_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class listaDispositivos_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView;
    private List<Dispositivos> dispositivosList = new ArrayList<>();
    private List<Dispositivos> listarecuperada = new ArrayList<>();
    private DispositivoAdapter adapter;
    private Context context;
    SQLiteDatabase db;
    private prefsDAO prefsdao;

    //private DispositivosListener dispositivosListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public listaDispositivos_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment listaDispositivos_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static listaDispositivos_fragment newInstance(String param1, String param2) {
        listaDispositivos_fragment fragment = new listaDispositivos_fragment();
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
        View view = inflater.inflate(R.layout.fragment_lista_dispositivos_fragment, container, false);
        listView = view.findViewById(R.id.list_dispositivos_frag);
        context = requireContext();

        cargarDispositivos();

        return view;
    }

    private void cargarDispositivos() {
        try {
            prefsHelper helper = new prefsHelper(context);
            db = helper.getWritableDatabase();
            prefsdao = new prefsDAO();

            String idUsuario = UtilController.getUserIdFromPreferences(context);
            String endpoint = "dispositivos/usuarios/" + idUsuario + "/dispositivos/activos";
            String url = UtilController.getBaseConnectionString(context, db, endpoint);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Dispositivos>>(){}.getType();
                        dispositivosList = gson.fromJson(responseData, listType);

                        requireActivity().runOnUiThread(() -> {
                            adapter = new DispositivoAdapter(context, dispositivosList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            listView.setOnItemClickListener((parent, view, position, id) -> {
//                                Dispositivos selected = dispositivosList.get(position);
//                                Intent intent = new Intent(context, dispositivoDetalle_activity.class);
//                                intent.putExtra("dispositivo_id", selected.getId());
//                                startActivity(intent);

                                Dispositivos seleccionado = dispositivosList.get(position);
                                dispositivoDetalles_fragment detalleDispo = dispositivoDetalles_fragment.newInstance(String.valueOf(seleccionado.getId()));

                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_perfil, detalleDispo) // your fragment container ID here
                                        .addToBackStack(null)
                                        .commit();


                            });
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(context, "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Fragment", "Excepción al cargar dispositivos", e);
        }
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
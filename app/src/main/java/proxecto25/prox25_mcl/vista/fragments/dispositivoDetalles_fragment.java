package proxecto25.prox25_mcl.vista.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
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
import proxecto25.prox25_mcl.vista.activities.UsuarioDatos_activity;
import proxecto25.prox25_mcl.vista.activities.dispositivoDetalle_activity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dispositivoDetalles_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dispositivoDetalles_fragment extends Fragment {

    private static final String ARG_DISPOSITIVO_ID = "dispositivo_id";

    private String dispositivoId;
    TextView tv_detalles;

    private Context context;
    SQLiteDatabase db;
    private prefsDAO prefsdao;

    public dispositivoDetalles_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dispositivo_id Parameter 1.
     * @return A new instance of fragment dispositivoDetalles_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dispositivoDetalles_fragment newInstance(String dispositivo_id) {
        dispositivoDetalles_fragment fragment = new dispositivoDetalles_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_DISPOSITIVO_ID, dispositivo_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dispositivoId = getArguments().getString(ARG_DISPOSITIVO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispositivo_detalles_fragment, container, false);
        context = requireContext();
        tv_detalles = view.findViewById(R.id.tv_datos_detalledisp);
        Integer idDis = Integer.parseInt(dispositivoId);

        cargaDetalles(idDis);

        Button btn_maps = view.findViewById(R.id.btn_maps);
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), dispositivoDetalle_activity.class);
                intent.putExtra("device_id", idDis);
                startActivity(intent);
            }
        });

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        tv_detalles = view.findViewById(R.id.tv_datos_detalledisp);
//
//        if (dispositivoId != null) {
//            loadDispositivoDetails(dispositivoId);
//        }
//    }



    private void cargaDetalles(Integer id) {
        // Build URL: base + /dispositivos/{id}
        prefsHelper helper = new prefsHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String idUsuario = String.valueOf(id);
        String endpoint = "dispositivos/" + idUsuario;
        String url = UtilController.getBaseConnectionString(requireContext(), db, endpoint);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error cargando dispositivo: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                    Dispositivos dispositivo = gson.fromJson(responseData, Dispositivos.class);

                    requireActivity().runOnUiThread(() -> {
                        if (dispositivo != null) {
                            StringBuilder details = new StringBuilder();

                            details.append("Nombre: ").append(dispositivo.getNombreDispositivo()).append("\n")
                                    .append("Modelo: ").append(dispositivo.getModelo()).append("\n")
                                    .append("Número de Serie: ").append(dispositivo.getNumeroSerie()).append("\n")
                                    .append("Fecha de Creación: ").append(dispositivo.getFechaCreacion()).append("\n\n");

                            List<Targets> targets = dispositivo.getTargets();
                            if (targets != null && !targets.isEmpty()) {
                                details.append("Targets:\n");
                                for (Targets target : targets) {
                                    details.append("• ID: ").append(target.getId()).append("\n")
                                            .append("  Nombre: ").append(target.getNombre()).append("\n")
                                            .append("  Tipo: ").append(target.getTipoTarget()).append("\n\n");
                                }
                            } else {
                                details.append("Sin targets asociados.");
                            }

                            tv_detalles.setText(details.toString());
                            
                        } else {
                            tv_detalles.setText("Dispositivo no encontrado.");
                        }
                    });
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Error loading dispositivo: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}
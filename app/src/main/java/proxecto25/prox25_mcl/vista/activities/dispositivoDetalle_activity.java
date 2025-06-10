package proxecto25.prox25_mcl.vista.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.modelo.Posiciones;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class dispositivoDetalle_activity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    private List<Posiciones> posicionesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivo_detalle);



        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // carga mapa asincrona

        // Get ID from Intent
        int deviceId = getIntent().getIntExtra("device_id", -1);
        if (deviceId != -1) {



            fetchPosiciones(deviceId);
        } else {
            Toast.makeText(this, "ID del dispositivo no recibido", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchPosiciones(int deviceId) {
        String BASE_URL = "http://10.0.2.2:8080/posiciones/device/";
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + deviceId + "/all";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("ERROR", "No se pudo conectar: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(dispositivoDetalle_activity.this, "Error de conexión",
                        Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    posicionesList = gson.fromJson(responseData, new TypeToken<List<Posiciones>>() {}.getType());

                    runOnUiThread(() -> updateMap());
                } else {
                    Log.e("ERROR", "Respuesta no exitosa: " + response.code());
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMap(); // Load markers when map is ready
    }

    private void updateMap() {
        if (mMap == null || posicionesList == null || posicionesList.isEmpty()) return;

        mMap.clear(); // Remove previous markers
        LatLng lastLocation = null;

        for (Posiciones pos : posicionesList) {
            LatLng location = new LatLng(pos.getLocation().get(1), pos.getLocation().get(0)); // [lat, lon]
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Dispositivo " + pos.getIdDispositivo())
                    .snippet("Tiempo: " + pos.getTimestamp()));

            lastLocation = location;
        }

        if (lastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12));
        }
    }

    // Lifecycle methods for MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}


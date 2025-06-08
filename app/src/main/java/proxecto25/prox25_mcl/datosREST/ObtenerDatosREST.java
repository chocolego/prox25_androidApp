package proxecto25.prox25_mcl.datosREST;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObtenerDatosREST {

//    private static ArrayList<Dispositivo> listapelis;
//    private ArrayList<Pelicula> listaRecuperadaPelis;
//
//    private static ArrayList<Actor> listaactores;
//    private ArrayList<Actor> listaRecuperadaActores;

//    private OkHttpClient cliente;
//
//
//    //01. LISTA PELICULAS
//    public void obtenerPelisFromREST(Context context, OnPelisCargadasListener restListener) {
//
//        listapelis = new ArrayList<>();
//        listaRecuperadaPelis = new ArrayList<>();
//        cliente = new OkHttpClient();
//
//        //OkHttpClient cliente = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://10.0.2.2:8080/listarPeliculas") //non recoñece localhost
//                .build();
//
//        cliente.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("Error", "Error al realizar la solicitud", e);
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful() && response.body() != null) {
//                    listapelis.clear();
//
//                    String responseDatos = response.body().string();
//                    Gson gson = new Gson();
//                    listaRecuperadaPelis = gson.fromJson(responseDatos, new TypeToken<ArrayList<Pelicula>>() {
//                    }.getType());
//
//                    //lista.addAll(listaRecuperada);
//                    if (restListener != null) {
//                        restListener.OnListaCargada(listaRecuperadaPelis);
//                    }
//                    Log.d("OK", "Respuesta correcta: " + response.code());
//
//                } else {
//                    Log.e("KO", "Fallo en la respuesta: " + response.code());
//                }
//            }
//        });
//    }
//
//    //02.LISTA ACTORES
//    public void obtenerActoresFromREST(Context context, OnActoresCargadosListener ActorestListener) {
//
//        listaactores = new ArrayList<>();
//        listaRecuperadaActores = new ArrayList<>();
//        cliente = new OkHttpClient();
//
//        //OkHttpClient cliente = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://10.0.2.2:8080/listarActores") //non recoñece localhost
//                .build();
//
//        cliente.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("Error", "Error al realizar la solicitud", e);
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful() && response.body() != null) {
//                    //listaactores.clear();
//
//                    String responseDatos = response.body().string();
//                    Gson gson = new Gson();
//                    listaRecuperadaActores = gson.fromJson(responseDatos, new TypeToken<ArrayList<Actor>>() {
//                    }.getType());
//
//                    //lista.addAll(listaRecuperada);
//                    if (ActorestListener != null) {
//                        ActorestListener.OnListaCargada(listaRecuperadaActores);
//                    }
//                    Log.d("OK", "Respuesta correcta: " + response.code());
//
//                } else {
//                    Log.e("KO", "Fallo en la respuesta: " + response.code());
//                }
//            }
//        });
//    }
//
//    //03.BUSCAR PELI
//
//    public void BuscarUsuariobyID(String idusuario) {
//
//        cliente = new OkHttpClient();
//        String idp = Integer.toString(idPeli);
//
//        HttpUrl url = HttpUrl.parse("http://10.0.2.2:8080/obtenerPelicula")
//                .newBuilder()
//                .addQueryParameter("id", idp)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        cliente.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.e("Error", "Error al realizar la solicitud", e);
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful() && response.body() != null) {
//
//
//                    String responseDatos = response.body().string();
//                    Gson gson = new Gson();
//                    Pelicula peliRecuperada = gson.fromJson(responseDatos, Pelicula.class);
//
//
//                    if (listener != null) {
//                        listener.OnPeliCargada(peliRecuperada);
//                    }
//                    Log.d("OK", "Respuesta correcta: " + response.code());
//
//                } else {
//                    Log.e("KO", "Fallo en la respuesta: " + response.code());
//                }
//            }
//        });
//    }
}

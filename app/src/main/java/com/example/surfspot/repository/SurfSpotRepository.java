package com.example.surfspot.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.surfspot.Enum.SurfBreak;
import com.example.surfspot.MainActivity;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class SurfSpotRepository {
    private static final String TAG = "SurfSpotRepository";
    private static SurfSpotRepository instance;
    private final List<SurfSpot> surfSpots = new ArrayList<>();
    private final Context context;
    private final MutableLiveData<List<SurfSpot>> surfSpotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<SurfSpot> selectedSpotLiveData = new MutableLiveData<>();

    private interface API {
        @GET("/api/spots")
        Call<List<SurfSpot>> getAllSpots();  // Changé à List<SurfSpot> au lieu de ApiResponse
    }

    // Classe ApiResponse supprimée car non nécessaire

    // Constructeur privé
    private SurfSpotRepository(Context context) {
        this.context = context.getApplicationContext(); // Pour éviter les memory leaks
        loadData();
    }

    // Singleton avec passage de contexte
    public static SurfSpotRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SurfSpotRepository(context);
        }
        return instance;
    }

    private void loadData() {
        isLoading.setValue(true);

        // Vérifier l'état de la connexion Internet
        if (isNetworkAvailable()) {
            // Si connecté, utiliser l'API
            Log.d(TAG, "AAAAAAAAAAAAAAAAA loading api");
            loadFromApi();
        } else {
            // Si non connecté, utiliser les données locales
            Log.d(TAG, "AAAAAAAAAAAAAAAAA loading local");
            loadFromLocal();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void loadFromApi() {

        // Utiliser la méthode getRetrofitInstance de MainActivity
        Retrofit retrofit = MainActivity.getRetrofitInstance();
        API api = retrofit.create(API.class);

        api.getAllSpots().enqueue(new Callback<List<SurfSpot>>() {
            @Override
            public void onResponse(Call<List<SurfSpot>> call, Response<List<SurfSpot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    surfSpots.clear();
                    List<SurfSpot> spotList = response.body();
                    for (SurfSpot spot : spotList) {
                        if (spot != null) {
                            surfSpots.add(spot);
                            Log.d(TAG, "Spot chargé: " + spot.getName() + ", ID: " + spot.getId());
                        } else {
                            Log.e(TAG, "Spot null dans la réponse");
                        }
                    }
                    // Mettre à jour LiveData
                    surfSpotsLiveData.setValue(new ArrayList<>(surfSpots));
                } else {
                    Log.e(TAG, "Erreur lors du chargement des spots: " +
                            (response.errorBody() != null ? response.errorBody().toString() : "Inconnu") +
                            ", Code: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<SurfSpot>> call, Throwable t) {
                Log.e(TAG, "Échec du chargement des spots", t);
                isLoading.setValue(false);
            }
        });
    }

    private void loadFromLocal() {
        try {
            // Lecture du fichier JSON dans /res/raw/surfspots.json
            InputStream inputStream = context.getResources().openRawResource(R.raw.surfspots);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            inputStream.close();

            String jsonData = builder.toString();

            // Parsing JSON
            JSONArray root = new JSONArray(jsonData);

            for (int i = 0; i < root.length(); i++) {
                JSONObject aSpot = root.getJSONObject(i);

                // Récupération du nom
                int id = aSpot.optInt("id");
                String name = aSpot.optString("name");
                String address = aSpot.optString("address");
                int difficulty = aSpot.optInt("difficultyLevel");
                SurfBreak surfBreak = SurfBreak.valueOf(aSpot.optString("surfBreak"));
                String photoUrl = aSpot.optString("photoUrl");
                String seasonStart = aSpot.optString("seasonStart");
                String seasonEnd = aSpot.optString("seasonEnd");
                double longitude = aSpot.optDouble("longitude");
                double latitude = aSpot.optDouble("latitude");

                // Création de l'objet SurfSpot
                SurfSpot spot = new SurfSpot(id, name, address, difficulty, surfBreak, photoUrl, seasonStart, seasonEnd, longitude, latitude);
                surfSpots.add(spot);

                // Mettre à jour LiveData
                surfSpotsLiveData.setValue(new ArrayList<>(surfSpots));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace(); // Tu peux aussi logguer avec Log.e() pour Android
        }
        isLoading.setValue(false);

    }

    public LiveData<List<SurfSpot>> getSurfSpotsLiveData() {
        return surfSpotsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<SurfSpot> getSelectedSpotLiveData() {
        return selectedSpotLiveData;
    }

    public SurfSpot getSurfSpotById(int id) {
        // Chercher le spot dans la liste locale
        for (SurfSpot spot : surfSpots) {
            if (spot.getId() != 0 && spot.getId() == id) {
                selectedSpotLiveData.setValue(spot);
                Log.d(TAG, "Spot trouvé par ID: " + spot.getName());
                return spot;
            }
        }
        Log.e(TAG, "Spot non trouvé pour l'ID: " + id);
        return null;
    }

    // Méthode pour forcer le rechargement des données
    public void refreshData() {
        loadData();
    }
}
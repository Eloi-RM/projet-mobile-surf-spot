package com.example.surfspot;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.ui.detail.SpotDetailFragment;
import com.example.surfspot.ui.detail.SurfSpotListFragment;
import com.example.surfspot.viewmodel.SpotDetailViewModel;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SurfSpotListFragment.OnSpotSelectedListener {
    private final static  String TAG = "MainActivity";

    private static final String BASE_URL = getApiBaseUrl();

    private static String getApiBaseUrl() {
        if (isEmulator()) {
            // Utiliser 10.0.2.2 pour l'émulateur qui pointe vers localhost de la machine hôte
            return "http://10.0.2.2:8080";
        } else {
            // Utiliser l'adresse IP réelle de votre serveur pour les appareils physiques
            return "http://192.168.12.209:8080"; // Remplacez par l'adresse IP de votre serveur
        }
    }

    private static boolean isEmulator() {
        return Build.PRODUCT.contains("sdk") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu");
    }

    private SpotDetailViewModel viewModel;

    public static Retrofit getRetrofitInstance() {
        // Créer un client OkHttp avec un intercepteur pour ajouter l'en-tête d'autorisation
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "je suis avant le viewmodel provider");

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SpotDetailViewModel.class);

        Log.d(TAG, "je suis apres le viewmodel provider");

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Spots de Surf");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SurfSpotListFragment())
                    .commit();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle("Spots de Surf");
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            actionBar.setDisplayHomeAsUpEnabled(false);
                        }
                    }
                } else {
                    setEnabled(false);
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onSpotSelected(int spotId) {
        // Charger les détails du spot dans le ViewModel
        Log.d(TAG, "je suis avant le viewmodel loadspot");

        viewModel.loadSurfSpot(spotId);

        Log.d(TAG, "je suis apres le viewmodel loadspot");

        SpotDetailFragment detailFragment = SpotDetailFragment.newInstance(spotId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Détails du spot");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Log.d(TAG, "onSpotSelected: Titre changé pour 'Détails du spot', ID: " + spotId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Spots de Surf");
                actionBar.setDisplayHomeAsUpEnabled(false);
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                }
            }
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onSupportNavigateUp();
        }
        return super.onOptionsItemSelected(item);
    }
}
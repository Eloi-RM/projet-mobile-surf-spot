package com.example.surfspot;

import static android.content.ContentValues.TAG;

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

    private static final String API_TOKEN = "pat8izimFSK0Fww6B.5176ea7c229d33209d88c01bca6a7cec2b0c002d503937f5ccb8f21616c3bb89";
    private static final String BASE_URL = "https://api.airtable.com";

    private SpotDetailViewModel viewModel;

    public static Retrofit getRetrofitInstance() {
        // Créer un client OkHttp avec un intercepteur pour ajouter l'en-tête d'autorisation
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + API_TOKEN)
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

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SpotDetailViewModel.class);

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
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.searchview,menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSpotSelected(String spotId) {
        // Charger les détails du spot dans le ViewModel
        viewModel.loadSurfSpot(spotId);

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
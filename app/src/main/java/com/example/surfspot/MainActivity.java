package com.example.surfspot;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class MainActivity extends AppCompatActivity implements SurfSpotListFragment.OnSpotSelectedListener {

    interface requestAllSurfSpots{
        @GET("api/oskour/{id}")
        Call<SurfSpot> getSpotById(@Path("id") int id);
    }

    private SpotDetailViewModel viewModel;  // Déclarée ici, initialisée dans onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialiser le ViewModel ici (contexte valide à ce stade)
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
    public void onSpotSelected(long spotId) {
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

        Log.d(TAG, "onSpotSelected: Titre changé pour 'Détails du spot'");
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Spots de Surf");
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

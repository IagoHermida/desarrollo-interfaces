package com.example.proyectofirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.LoginActivity;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.viewmodels.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getItemsLiveData().observe(this, items -> {
            Log.d("Dashboard", "Items: " + items.toString());
            com.example.proyectofirebase.adapters.ItemAdapter adapter = new com.example.proyectofirebase.adapters.ItemAdapter(items, this, new com.example.proyectofirebase.adapters.ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Item item) {
                    // Pasamos los datos a DetailActivity
                    Intent intent = new Intent(DashboardActivity.this, DetailActivity.class);
                    intent.putExtra("itemId", item.getId());
                    intent.putExtra("productTitle", item.getNombre());
                    intent.putExtra("productDescription", item.getDescripcion());
                    intent.putExtra("imageUrl", item.getImagenUrl());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        });

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
        Button themeButton = findViewById(R.id.themeButton);
        themeButton.setOnClickListener(view -> {
            boolean isDarkMode = sharedPref.getBoolean("darkMode", false);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("darkMode", !isDarkMode);
            editor.apply();

            // Cambiar el tema dinámicamente
            if (!isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
           } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Recrea la actividad para aplicar los cambios
        //    recreate();
        });
    }
}

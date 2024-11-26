package com.example.mycatalog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Usamos if-else en lugar de switch para evitar el error
            if (item.getItemId() == R.id.nav_catalog) {
                selectedFragment = new CatalogFragment();
            } else if (item.getItemId() == R.id.nav_about) {
                selectedFragment = new AboutFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Cargar el fragmento inicial
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_catalog);  // Esto establece el fragmento inicial
        }
    }
}
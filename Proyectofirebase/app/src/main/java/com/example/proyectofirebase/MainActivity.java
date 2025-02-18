package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;  // DataBinding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Manejamos los eventos del menú lateral
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_dashboard:
                    openFragment(new DashboardFragment());
                    break;
                case R.id.nav_favourites:
                    openFragment(new FavouritesFragment());
                    break;
                case R.id.nav_profile:
                    openFragment(new ProfileFragment());
                    break;
                case R.id.nav_logout:
                    logoutUser();
                    break;
            }
            // Al pulsar en un ítem, cerramos el drawer
            binding.drawerLayout.closeDrawers();
            return true;
        });

        // Cargar por defecto el DashboardFragment
        if (savedInstanceState == null) {
            openFragment(new DashboardFragment());
        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // Redireccionar a LoginActivity
        Intent intent = new Intent(this, LoginFragment.class);
        startActivity(intent);
        finish(); // Para que no pueda volver con el botón atrás
    }
}
package com.example.aerocatalogo.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.example.aerocatalogo.R;
import com.example.aerocatalogo.databinding.ActivityMainBinding;
import com.example.aerocatalogo.repositories.DashboardRepository;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Manejamos los eventos del menú lateral
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                openFragment(new DashboardFragment());
            } else if (id == R.id.nav_favorites) {
                openFragment(new FavoritesFragment()); // Permite regresar al fragmento anterior con el botón atrás
            } else if (id == R.id.nav_random) {
                openFragment(new RandomFragment()); // Permite regresar al fragmento anterior con el botón atrás
            } else if (id == R.id.nav_profile) {
                openFragment(new ProfileFragment()); // Permite regresar al fragmento anterior con el botón atrás
            } else if (id == R.id.nav_logout) {
                logoutUser();
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
                .addToBackStack(null) // Permite regresar al fragmento anterior con el botón atrás
                .commit();
    }

    private void logoutUser() {
        DashboardRepository dashboardRepository = new DashboardRepository();
        dashboardRepository.cleanup();

        getViewModelStore().clear(); // Limpiar ViewModel
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        // Redireccionar a LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}

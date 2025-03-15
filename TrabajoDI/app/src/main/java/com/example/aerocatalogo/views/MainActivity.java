package com.example.aerocatalogo.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.example.aerocatalogo.R;
import com.example.aerocatalogo.databinding.ActivityMainBinding;
import com.example.aerocatalogo.repositories.DashboardRepository;
import com.example.aerocatalogo.repositories.FavoritesRepository;
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
            } else if (id == R.id.nav_clear_favorites) {
                clearFavorites();
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
        // Primero, desloguear al usuario en Firebase
        FirebaseAuth.getInstance().signOut();

        // Eliminar el UID del usuario de SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("userId");  // Eliminar el UID del usuario
        editor.apply();  // Guardar cambios

        // Redirigir a la pantalla de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();  // Cerrar la MainActivity
    }

    private void clearFavorites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FavoritesRepository favoritesRepository = new FavoritesRepository(userId);

        favoritesRepository.clearFavorites().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Favoritos eliminados", Toast.LENGTH_SHORT).show();
                openFragment(new DashboardFragment()); // Recargar DashboardFragment
            } else {
                Toast.makeText(this, "Error al eliminar favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        binding.drawerLayout.closeDrawers(); // Cerrar el menú
    }

}

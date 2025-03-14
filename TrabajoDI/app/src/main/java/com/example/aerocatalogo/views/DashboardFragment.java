package com.example.aerocatalogo.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aerocatalogo.R;
import com.example.aerocatalogo.adapters.ItemAdapter;
import com.example.aerocatalogo.models.Item;
import com.example.aerocatalogo.viewmodels.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private DashboardViewModel viewModel;
    private ItemAdapter adapter;
    private FirebaseAuth mAuth;

    // Constructor vacío
    public DashboardFragment() {}

    // Se infla el layout del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configurar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializar adapter con lista vacía
        adapter = new ItemAdapter(new ArrayList<>(), this::openDetailFragment);
        recyclerView.setAdapter(adapter);

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Obtener referencias de las vistas
        FloatingActionButton logoutFab = view.findViewById(R.id.logoutFab);
        FloatingActionButton settingsFab = view.findViewById(R.id.settingsFab);
        FloatingActionButton favoritesListFab = view.findViewById(R.id.favoritesListFab);

        // Establecer descripciones de accesibilidad.
        logoutFab.setContentDescription("Cerrar sesión");
        settingsFab.setContentDescription("Abrir configuración");
        favoritesListFab.setContentDescription("Abrir lista de favoritos");

        // Observar cambios en la lista de aviones
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            adapter = new ItemAdapter(items, this::openDetailFragment);
            recyclerView.setAdapter(adapter);
        });

        // Observar errores
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Configurar botón de logout
        logoutFab.setOnClickListener(v -> {
            getViewModelStore().clear(); // Limpiar ViewModel
            mAuth.signOut();
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Configurar botón de configuración
        settingsFab.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, profileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configurar botón de favoritos
        favoritesListFab.setOnClickListener(v -> {
            FavoritesFragment favoritesFragment = new FavoritesFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, favoritesFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // El bundle almacena pares clave valor
    private void openDetailFragment(Item item) {
        Bundle bundle = new Bundle();
        bundle.putString("item_title", item.getTitle());
        bundle.putString("item_description", item.getDescription());
        bundle.putString("item_url", item.getUrl());
        bundle.putString("item_id", item.getId());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        // se encarga de cambiar los fragments
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null) // Permite regresar al fragmento anterior con el botón atrás
                .commit();
    }
}

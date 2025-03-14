package com.example.aerocatalogo.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aerocatalogo.R;
import com.example.aerocatalogo.adapters.FavoritesAdapter;
import com.example.aerocatalogo.viewmodels.FavoritesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FavoritesViewModel favoritesViewModel;
    private FavoritesAdapter adapter;

    // Constructor vacío
    public FavoritesFragment() {}

    // Se infla el layout del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Configurar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializar adapter con lista vacía
        adapter = new FavoritesAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Configurar ViewModel
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Obtener referencias de las vistas
        FloatingActionButton returnFab = view.findViewById(R.id.returnFab);

        // Establecer descripciones de accesibilidad.
        returnFab.setContentDescription("Volver");

        // Configurar botón volver
        returnFab.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Observar cambios en la lista de aviones
        favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), favorites ->
                adapter.updateFavorites(favorites)
        );

        return view;
    }
}

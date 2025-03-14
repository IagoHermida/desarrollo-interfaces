package com.example.aerocatalogo.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.aerocatalogo.R;
import com.example.aerocatalogo.models.Item;
import com.example.aerocatalogo.viewmodels.FavoritesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DetailFragment extends Fragment {
    private FloatingActionButton favoriteFab;

    // Constructor vacío
    public DetailFragment() {}

    // Se infla el layout del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Obtener los datos pasados por el Bundle
        Bundle bundle = getArguments();

        if (bundle != null) {
            String title = bundle.getString("item_title");
            String description = bundle.getString("item_description");
            String url = bundle.getString("item_url");
            String itemId = bundle.getString("item_id");

            // Construir el objeto Plane
            Item currentItem = new Item(itemId, title, description, url);

            FavoritesViewModel favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

            // Obtener referencias de las vistas
            TextView titleTextView = view.findViewById(R.id.titleTextView);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
            FloatingActionButton returnFab = view.findViewById(R.id.returnFab);
            favoriteFab = view.findViewById(R.id.favoriteFab);

            // Establecer descripciones de accesibilidad.
            titleTextView.setContentDescription("Producto" + title);
            imageView.setContentDescription("Imagen del producto " + title);
            descriptionTextView.setContentDescription("Descripción: " + description);
            returnFab.setContentDescription("Volver a la lista de productos");
            favoriteFab.setContentDescription("Añadir o eliminar de favoritos");

            // Configurar botón de volver
            returnFab.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager().popBackStack();
            });

            // Asignar datos a las vistas
            titleTextView.setText(title);
            descriptionTextView.setText(description);

            // Cargar la imagen con Glide
            Glide.with(this)
                    .load(url)
                    .into(imageView);

            // Observar estado favorito
            favoritesViewModel.isFavorite(currentItem.getId()).observe(getViewLifecycleOwner(), isFavorite ->
                    updateFabIcon(isFavorite)
            );

            // Comfigurar botón de favoritos
            favoriteFab.setOnClickListener(v ->
                    favoritesViewModel.toggleFavorite(currentItem)
            );

            // Observar cambios y mostrar Snackbar
            favoritesViewModel.getFavoriteActionStatus().observe(getViewLifecycleOwner(), isFavorite -> {
                String message = isFavorite ?
                        "Añadido a favoritos" :
                        "Eliminado de favoritos";

                View rootView = requireActivity().findViewById(android.R.id.content); // Asegurar una vista válida

                if (rootView != null) {
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void updateFabIcon(boolean isFavorite) {
        favoriteFab.setImageResource(
                isFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24
        );
    }
}

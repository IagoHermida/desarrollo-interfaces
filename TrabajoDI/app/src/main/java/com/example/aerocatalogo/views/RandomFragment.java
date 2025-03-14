package com.example.aerocatalogo.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.aerocatalogo.R;
import com.example.aerocatalogo.models.Item;
import com.example.aerocatalogo.viewmodels.RandomViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Random;

public class RandomFragment extends Fragment {
    private RandomViewModel viewModel;
    private Random random;

    public RandomFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random, container, false);

        viewModel = new ViewModelProvider(this).get(RandomViewModel.class);
        random = new Random();

        // Obtener referencias de las vistas
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        FloatingActionButton returnFab = view.findViewById(R.id.returnFab);
        Button randomButton = view.findViewById(R.id.randomButton);

        // Observar errores
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Observar cambios en la lista de aviones
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null && !items.isEmpty()) {
                RandomItem(items, titleTextView, descriptionTextView, imageView);
            }
        });

        // Configurar botón de volver
        returnFab.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Botón para obtener un avión aleatorio
        randomButton.setOnClickListener(v -> {
            List<Item> items = viewModel.getItems().getValue();
            if (items != null && !items.isEmpty()) {
                RandomItem(items, titleTextView, descriptionTextView, imageView);
            }
        });

        return view;
    }

    private void RandomItem(List<Item> items, TextView titleTextView, TextView descriptionTextView, ImageView imageView) {
        // Seleccionar un avión aleatorio
        Item item = items.get(random.nextInt(items.size()));

        // Establecer datos
        titleTextView.setText(item.getTitle());
        descriptionTextView.setText(item.getDescription());

        // Cargar la imagen con Glide
        Glide.with(this)
                .load(item.getUrl())
                .into(imageView);

        // Accesibilidad
        titleTextView.setContentDescription("Producto " + item.getTitle());
        imageView.setContentDescription("Imagen del producto " + item.getTitle());
        descriptionTextView.setContentDescription("Descripción: " + item.getDescription());
    }
}


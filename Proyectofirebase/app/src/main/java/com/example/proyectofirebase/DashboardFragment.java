package com.example.proyectofirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.adapters.ItemAdapter;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.viewmodels.DashboardViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;
    private DatabaseReference productosRef;
    private ProgressBar progressBar; // Variable de instancia
    private DashboardViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Utilizamos la variable de instancia
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(itemList, getContext(), item -> {
            // Al hacer clic en un ítem del RecyclerView, abrir DetailFragment
            DetailFragment detailFragment = new DetailFragment();

            // Pasar datos con un Bundle
            Bundle bundle = new Bundle();
            bundle.putString("ITEM_ID", item.getId());
            bundle.putString("productTitle", item.getNombre());
            bundle.putString("productDescription", item.getDescripcion());
            bundle.putString("imageUrl", item.getImagenUrl());
            detailFragment.setArguments(bundle);

            // Reemplazar el fragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null) // Permite volver con el botón "atrás"
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Observar cambios en la lista de productos
        viewModel.getItemsLiveData().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                itemList.clear();
                itemList.addAll(items);  // Solo agregamos si 'items' no es null
                adapter.notifyDataSetChanged();
            } else {
                Log.e("DashboardFragment", "Los datos de los items son null");
            }

            // Ocultar la barra de progreso
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });


        cargarProductos(); // Cargar los productos de Firebase

        return view;
    }

    private void cargarProductos() {

        productosRef = FirebaseDatabase.getInstance().getReference("productos");

        // Asegurarse de que el ProgressBar no es null antes de modificarlo
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);  // Hacer visible el ProgressBar
        }

        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Item item = data.getValue(Item.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
                // Asegurarse de que el ProgressBar no es null antes de modificarlo
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);  // Esconder el ProgressBar después de cargar los datos
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar los productos", Toast.LENGTH_SHORT).show();
                // Asegurarse de que el ProgressBar no es null antes de modificarlo
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);  // Esconder el ProgressBar en caso de error
                }
            }
        });
    }
}

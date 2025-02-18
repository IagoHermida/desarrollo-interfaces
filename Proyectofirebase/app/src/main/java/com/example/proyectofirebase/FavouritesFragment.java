package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.DetailFragment;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.adapters.ItemAdapter;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.viewmodels.FavouritesViewModel;

import java.util.List;

public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private FavouritesViewModel viewModel;

    public FavouritesFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
        viewModel.getFavoritosLiveData().observe(getViewLifecycleOwner(), this::updateUI);

        return view;
    }

    private void updateUI(List<Item> favoritos) {
        adapter = new ItemAdapter(favoritos, getContext(), item -> {
            Intent intent = new Intent(getContext(), DetailFragment.class);
            intent.putExtra("itemId", item.getId());
            intent.putExtra("productTitle", item.getNombre());
            intent.putExtra("productDescription", item.getDescripcion());
            intent.putExtra("imageUrl", item.getImagenUrl());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}

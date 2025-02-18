package com.example.proyectofirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.DetailFragment;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.adapters.ItemAdapter;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.viewmodels.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private DashboardViewModel viewModel;

    public DashboardFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getItemsLiveData().observe(getViewLifecycleOwner(), items -> {
            Log.d("Dashboard", "Items: " + items);
            ItemAdapter adapter = new ItemAdapter(items, getContext(), item -> {
                Intent intent = new Intent(getContext(), DetailFragment.class);
                intent.putExtra("itemId", item.getId());
                intent.putExtra("productTitle", item.getNombre());
                intent.putExtra("productDescription", item.getDescripcion());
                intent.putExtra("imageUrl", item.getImagenUrl());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
        });

        Button themeButton = view.findViewById(R.id.themeButton);
        themeButton.setOnClickListener(v -> {
            boolean isDarkMode = sharedPref.getBoolean("darkMode", false);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("darkMode", !isDarkMode);
            editor.apply();

            if (!isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        return view;
    }
}

package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.LoginActivity;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.viewmodels.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        viewModel.getItemsLiveData().observe(this, items -> {
            Log.d("Dashboard", "Items: " + items.toString());
            com.example.proyectofirebase.adapters.ItemAdapter adapter = new com.example.proyectofirebase.adapters.ItemAdapter(items, this, new com.example.proyectofirebase.adapters.ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Item item) {
                    // Pasamos los datos a DetailActivity
                    Intent intent = new Intent(DashboardActivity.this, DetailActivity.class);
                    intent.putExtra("productTitle", item.getNombre());
                    intent.putExtra("productDescription", item.getDescripcion());
                    intent.putExtra("imageUrl", item.getImagenUrl());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        });

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
}

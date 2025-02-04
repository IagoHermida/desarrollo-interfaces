package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofirebase.adapters.ItemAdapter;
import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.repositories.UserRepository;
import com.example.proyectofirebase.viewmodels.FavouritesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> favoritosList;
    private DatabaseReference favoritosRef;
    private FavouritesViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerViewFavourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoritosList = new ArrayList<>();
        adapter = new ItemAdapter(favoritosList, this, item -> {
            // Lógica para manejar el clic en un producto favorito
        });
        recyclerView.setAdapter(adapter);

        // Acceder a los favoritos del usuario actual
        favoritosRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favoritos");

        cargarFavoritos();
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        viewModel.getFavoritosLiveData().observe(this, favoritos -> {
            adapter = new ItemAdapter(favoritos, this, item -> {
                Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
                intent.putExtra("itemId", item.getId());
                intent.putExtra("productTitle", item.getNombre());
                intent.putExtra("productDescription", item.getDescripcion());
                intent.putExtra("imageUrl", item.getImagenUrl());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        });

    }

    private void cargarFavoritos() {
        favoritosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                favoritosList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String productId = data.getKey();  // Obtiene el ID del producto

                    // Obtenemos los detalles del producto usando el ID
                    obtenerDetallesProducto(productId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FavouritesActivity.this, "Error al cargar los favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDetallesProducto(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("productos").child(productId);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        Log.d("FavouritesActivity", "Producto encontrado: " + item.getNombre() + " - Imagen: " + item.getImagenUrl());

                        favoritosList.add(item);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FavouritesActivity", "Item es NULL para ID: " + productId);
                    }
                } else {
                    Log.e("FavouritesActivity", "Snapshot no existe para ID: " + productId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FavouritesActivity.this, "Error al obtener los detalles del producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

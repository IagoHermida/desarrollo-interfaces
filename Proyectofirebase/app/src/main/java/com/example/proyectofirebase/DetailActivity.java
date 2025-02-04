package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectofirebase.repositories.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private Button buttonGoToFavorites;
    private TextView titleTextView, descriptionTextView;
    private ImageView imageView;
    private FloatingActionButton fabFavorite;
    private UserRepository userRepository;
    private String itemId;
    private boolean esFavorito = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        buttonGoToFavorites = findViewById(R.id.buttonGoToFavorites);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inicializar los componentes de la interfaz
        titleTextView = findViewById(R.id.detailTitleTextView);
        descriptionTextView = findViewById(R.id.detailDescriptionTextView);
        imageView = findViewById(R.id.detailImageView);

        // Obtener los datos enviados desde DashboardActivity
        Intent intent = getIntent();
        String title = intent.getStringExtra("productTitle");
        String description = intent.getStringExtra("productDescription");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Mostrar los datos en la interfaz
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Cargar la imagen usando Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        // Obtener ID del producto de la intención
        itemId = getIntent().getStringExtra("itemId");
        if (itemId == null || itemId.isEmpty()) {
            Log.e("DetailActivity", "itemId no encontrado o nulo");
            Toast.makeText(this, "Error: El ID del producto no se ha encontrado.", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza la actividad si el itemId es inválido
            return;
        }

        // Inicializar repositorio
        userRepository = new UserRepository();

        // Botón flotante
        fabFavorite = findViewById(R.id.fab_favorite);

        // Verificar si el producto ya es favorito
        verificarSiEsFavorito();

        // Manejar clic en el FAB
        fabFavorite.setOnClickListener(v -> {
            if (esFavorito) {
                userRepository.eliminarDeFavoritos(itemId);
                esFavorito = false;
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                userRepository.agregarAFavoritos(itemId);
                esFavorito = true;
                fabFavorite.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonGoToFavorites = findViewById(R.id.buttonGoToFavorites);
        buttonGoToFavorites.setOnClickListener(v -> {
            Intent intent2 = new Intent(DetailActivity.this, FavouritesActivity.class);
            startActivity(intent2);
        });
    }
    private void verificarSiEsFavorito() {
        userRepository.verificarFavorito(itemId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    esFavorito = true;
                    fabFavorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    esFavorito = false;
                    fabFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Error al verificar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

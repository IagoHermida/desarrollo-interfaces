package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inicializar los componentes de la interfaz
        titleTextView = findViewById(R.id.detailTitleTextView);
        descriptionTextView = findViewById(R.id.detailDescriptionTextView);
        imageView = findViewById(R.id.detailImageView);

        // Obtener los datos enviados desde DashboardActivity
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Mostrar los datos en la interfaz
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Cargar la imagen usando Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }
}

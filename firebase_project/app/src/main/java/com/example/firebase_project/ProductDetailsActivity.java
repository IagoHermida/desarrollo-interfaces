package com.example.firebase_project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase_project.R;

public class ProductDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Recibe los datos pasados a través del Intent
        String productName = getIntent().getStringExtra("productName");
        Long quantity = getIntent().getLongExtra("quantity", 0);  // Valor predeterminado: 0
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Encuentra los elementos de la interfaz
        TextView productNameTextView = findViewById(R.id.productNameTextView);
        TextView quantityTextView = findViewById(R.id.quantityTextView);
        TextView imageUrlTextView = findViewById(R.id.imageUrlTextView);

        // Muestra los datos en los TextViews
        productNameTextView.setText("Producto: " + productName);
        quantityTextView.setText("Cantidad: " + quantity);
        imageUrlTextView.setText("Imagen: " + imageUrl);
    }
}
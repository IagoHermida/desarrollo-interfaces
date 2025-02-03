package com.example.proyectofirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofirebase.DetailActivity;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.models.Item;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private List<Item> productList;
    private Context context;

    // Constructor
    public DashboardAdapter(List<Item> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    // Crear las vistas
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    // Enlazar los datos
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item product = productList.get(position);
        holder.titleTextView.setText(product.getNombre());
        Glide.with(context).load(product.getImagenUrl()).into(holder.imageView);

        // Agregar el click listener
        holder.itemView.setOnClickListener(v -> {
            // Crear un Intent para abrir DetailActivity
            Intent intent = new Intent(context, DetailActivity.class);
            // Pasar los datos del producto a DetailActivity
            intent.putExtra("title", product.getNombre());
            intent.putExtra("description", product.getDescripcion());
            intent.putExtra("imageUrl", product.getImagenUrl());
            context.startActivity(intent);
        });
    }

    // Obtener el número de elementos
    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder para las vistas del item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTitleTextView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }
    }
}

package com.example.proyectofirebase.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public ItemAdapter(List<Item> items, Context context, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        Log.d("ItemAdapter", "Item en la posición " + position + ": " + item.getNombre());
        holder.titleTextView.setText(item.getNombre());
        Glide.with(context).load(item.getImagenUrl()).into(holder.imageView);
        holder.imageView.setContentDescription(item.getNombre());
        // Detecta el clic en el item
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Interfaz OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    // ViewHolder
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemNameTextView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }
    }
}

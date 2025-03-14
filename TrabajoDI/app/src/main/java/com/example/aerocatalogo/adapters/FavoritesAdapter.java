package com.example.aerocatalogo.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aerocatalogo.R;
import com.example.aerocatalogo.databinding.ItemPlaneBinding;
import com.example.aerocatalogo.models.Item;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ItemViewHolder> {
    private List<Item> items;
    private OnItemClickListener listener; // Cambiado a la interfaz propia

    public FavoritesAdapter(List<Item> items) {
        this.items = items;
    }

    public void updateFavorites(List<Item> newFavorites) {
        this.items.clear();
        this.items.addAll(newFavorites);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaneBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_plane,
                parent,
                false
        );
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) { // Ahora usa el ViewHolder correcto
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(Item plane);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaneBinding binding;

        public ItemViewHolder(@NonNull ItemPlaneBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Configurar el evento de clic
            binding.getRoot().setOnClickListener(v -> {
                Item item = binding.getPlane();
                if (item != null && listener != null) {
                    listener.onItemClick(item); // Llamamos al listener correctamente
                }
            });
        }

        public void bind(Item item) {
            binding.setPlane(item);
            Glide.with(binding.getRoot().getContext())
                    .load(item.getUrl())
                    .centerCrop()
                    .into(binding.itemImage);
            binding.executePendingBindings();
        }
    }
}

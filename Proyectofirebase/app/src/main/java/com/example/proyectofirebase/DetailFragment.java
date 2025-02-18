package com.example.proyectofirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.proyectofirebase.R;
import com.example.proyectofirebase.repositories.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailFragment extends Fragment {

    private TextView titleTextView, descriptionTextView;
    private ImageView imageView;
    private FloatingActionButton fabFavorite;
    private UserRepository userRepository;
    private String itemId;
    private boolean esFavorito = false;

    public DetailFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        titleTextView = view.findViewById(R.id.detailTitleTextView);
        descriptionTextView = view.findViewById(R.id.detailDescriptionTextView);
        imageView = view.findViewById(R.id.detailImageView);
        fabFavorite = view.findViewById(R.id.fab_favorite);

        Bundle args = getArguments();
        if (args != null) {
            itemId = args.getString("itemId");
            titleTextView.setText(args.getString("productTitle"));
            descriptionTextView.setText(args.getString("productDescription"));

            Glide.with(this)
                    .load(args.getString("imageUrl"))
                    .into(imageView);
        }

        userRepository = new UserRepository();
        verificarSiEsFavorito();

        fabFavorite.setOnClickListener(v -> {
            if (esFavorito) {
                userRepository.eliminarDeFavoritos(itemId);
                esFavorito = false;
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                userRepository.agregarAFavoritos(itemId);
                esFavorito = true;
                fabFavorite.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(getContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void verificarSiEsFavorito() {
        userRepository.verificarFavorito(itemId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                esFavorito = snapshot.exists();
                fabFavorite.setImageResource(esFavorito ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error al verificar favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

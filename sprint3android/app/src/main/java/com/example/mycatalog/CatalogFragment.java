package com.example.mycatalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CatalogFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        Button btnNavigateToDetail = view.findViewById(R.id.boton);
        btnNavigateToDetail.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
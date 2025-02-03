package com.example.proyectofirebase.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofirebase.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {
    private final DatabaseReference databaseRef;

    public DashboardRepository() {
        databaseRef = FirebaseDatabase.getInstance().getReference("productos");
    }

    public void obtenerItems(MutableLiveData<List<Item>> itemsLiveData) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    // Mapea los datos de Firebase a los atributos correctos de Item
                    String producto = data.child("producto").getValue(String.class);
                    String imagenUrl = data.child("imagen").getValue(String.class);
                    String descripcion = "Descripción no disponible";

                    Item item = new Item(data.getKey(), producto, descripcion, imagenUrl); // Creamos el item
                    if (item != null) {
                        itemList.add(item); // Añadimos el item a la lista
                    }
                }
                itemsLiveData.setValue(itemList); // Actualizamos el LiveData
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Si hay un error, setea el LiveData como null
                itemsLiveData.setValue(null);
            }
        });
    }
}

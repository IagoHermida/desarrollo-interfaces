package com.example.aerocatalogo.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aerocatalogo.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RandomRepository {
    private final FirebaseDatabase firebaseDatabase;
    private final MutableLiveData<List<Item>> itemsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private ValueEventListener valueEventListener;

    public RandomRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance(); // Asegúrate de inicializar Firebase en tu app.
        itemsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchItems() {
        // Para que después del logout el DashboardRepository no siga intentando escuchar cambios
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            errorLiveData.setValue("Usuario no autenticado");
            return;
        }

        if (valueEventListener != null) {
            firebaseDatabase.getReference("products").removeEventListener(valueEventListener);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    // Obtener la clave que usaremos como id:
                    String itemId = itemSnapshot.getKey();

                    // Convierte el snapshot en un objeto Plane. Para qllo, la clase Plane debe tener un constructor vacío.
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null) {
                        item.setId(itemId);
                        items.add(item);
                    }
                }
                itemsLiveData.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue("Error al obtener datos: " + error.getMessage());
            }
        };

        firebaseDatabase.getReference("products").addValueEventListener(valueEventListener);
    }

    // Limpiar listener tras logout
    public void cleanup() {
        if (valueEventListener != null) {
            firebaseDatabase.getReference("products").removeEventListener(valueEventListener);
            valueEventListener = null;
        }
        itemsLiveData.setValue(null);
        errorLiveData.setValue(null);
    }
}

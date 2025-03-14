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

public class DashboardRepository {
    private final FirebaseDatabase firebaseDatabase;
    private final MutableLiveData<List<Item>> itemsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private ValueEventListener valueEventListener;

    public DashboardRepository() {
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
        // Verificamos si el usuario está autenticado
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            errorLiveData.setValue("Usuario no autenticado");
            return;
        }

        // Limpiamos cualquier listener previo
        if (valueEventListener != null) {
            firebaseDatabase.getReference("products").removeEventListener(valueEventListener);
        }

        // Obtenemos los favoritos del usuario
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FavoritesRepository favoritesRepository = new FavoritesRepository(userId);

        favoritesRepository.getFavorites().observeForever(favorites -> {
            List<String> favoriteIds = new ArrayList<>();
            for (Item favoriteItem : favorites) {
                favoriteIds.add(favoriteItem.getId());  // Guardamos los IDs de los favoritos
            }

            // Ahora que tenemos los favoritos, cargamos los productos
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Item> items = new ArrayList<>();
                    // Filtramos los productos, excluyendo los favoritos
                    for (DataSnapshot planeSnapshot : snapshot.getChildren()) {
                        String itemID = planeSnapshot.getKey();
                        Item item = planeSnapshot.getValue(Item.class);

                        if (item != null && !favoriteIds.contains(itemID)) {
                            item.setId(itemID); // Asignamos el ID al item
                            items.add(item);  // Añadimos el producto a la lista
                        }
                    }

                    // Actualizamos la lista de productos en el LiveData
                    itemsLiveData.setValue(items);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    errorLiveData.setValue("Error al obtener datos: " + error.getMessage());
                }
            };

            // Consultamos los productos en Firebase
            firebaseDatabase.getReference("products").addValueEventListener(valueEventListener);
        });
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

package com.example.aerocatalogo.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aerocatalogo.models.Item;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesRepository {
    private DatabaseReference userFavoritesRef;
    private DatabaseReference planesFavoritesRef;


    public FavoritesRepository(String userId) {
        userFavoritesRef = FirebaseDatabase.getInstance()
                .getReference("users/" + userId + "/favorites");
        planesFavoritesRef =FirebaseDatabase.getInstance().getReference("products");
    }

    public Task<Boolean> toggleFavorite(Item item) {
        String itemId = item.getId();

        return userFavoritesRef.child(itemId).get()
                .continueWithTask(task -> {
                    DataSnapshot snapshot = task.getResult();
                    boolean isFavorite = snapshot.exists();

                    return isFavorite
                            ? userFavoritesRef.child(itemId).removeValue()
                            .continueWith(removeTask -> false)
                            : userFavoritesRef.child(itemId).setValue(item)
                            .continueWith(addTask -> true);
                });
    }

    public LiveData<List<Item>> getFavorites() {
        MutableLiveData<List<Item>> favoritesLiveData = new MutableLiveData<>();

        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> favIds = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    String itemId = child.getKey();
                    favIds.add(itemId);
                }

                    planesFavoritesRef.addValueEventListener(new ValueEventListener() {
                        final List<Item> favorites = new ArrayList<>();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                           for (String itemId : favIds) {
                               Item item = favSnapshot.child(itemId).getValue(Item.class);
                               item.setId(itemId);
                               favorites.add(item);
                           }
                            favoritesLiveData.setValue(favorites);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                favoritesLiveData.setValue(new ArrayList<>());
            }
        });

        return favoritesLiveData;
    }
    public LiveData<Boolean> isFavorite(String itemId) {
        MutableLiveData<Boolean> isFavoriteLiveData = new MutableLiveData<>();

        userFavoritesRef.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isFavoriteLiveData.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isFavoriteLiveData.setValue(false);
            }
        });

        return isFavoriteLiveData;
    }
}

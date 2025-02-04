package com.example.proyectofirebase.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseAuth mAuth;

    private final DatabaseReference databaseRef;
    private String userId;
    private DatabaseReference favoritosRef;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        favoritosRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favoritos");
    }

    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public void registerUser(User user, MutableLiveData<Boolean> isSuccessful, MutableLiveData<String> errorMessage) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso, guardar datos en Firebase Realtime Database
                        String userId = mAuth.getCurrentUser().getUid();
                        databaseRef.child(userId).setValue(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        isSuccessful.setValue(true);
                                    } else {
                                        errorMessage.setValue("Error al guardar los datos.");
                                    }
                                });
                    } else {
                        errorMessage.setValue("Error en el registro: " + task.getException().getMessage());
                    }
                });
    }
    public void agregarAFavoritos(String itemId) {
        databaseRef.child(itemId).setValue(true);
    }

    public void eliminarDeFavoritos(String itemId) {
        databaseRef.child(itemId).removeValue();
    }

    public void verificarFavorito(String itemId, ValueEventListener listener) {
        databaseRef.child(itemId).addListenerForSingleValueEvent(listener);
    }
    public void obtenerFavoritos(MutableLiveData<List<Item>> favoritosLiveData) {
        favoritosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> favoritos = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String itemId = data.getKey();
                    if (itemId != null) {
                        // Obtener detalles del producto
                        FirebaseDatabase.getInstance().getReference("productos").child(itemId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                                        Item item = productSnapshot.getValue(Item.class);
                                        if (item != null) {
                                            favoritos.add(item);
                                            favoritosLiveData.setValue(favoritos); // Notificar cambios
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                favoritosLiveData.setValue(null);
            }
        });
    }

}

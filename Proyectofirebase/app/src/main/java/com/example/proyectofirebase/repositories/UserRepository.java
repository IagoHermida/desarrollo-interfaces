package com.example.proyectofirebase.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.proyectofirebase.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseRef;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
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
}

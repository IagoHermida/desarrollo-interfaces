package com.example.aerocatalogo.repositories;

import com.example.aerocatalogo.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    // Necesitan de un constructor
    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseRef;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void registerUser(String fullName, String email, String phone, String address, String password, OnUserRegisteredListener listener) {
        // Crear usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Esperar a que la autenticación se complete

                            String uid = firebaseUser.getUid();
                            User newUser = new User(fullName, email, phone, address);

                            databaseRef.child(uid).setValue(newUser)
                                    .addOnSuccessListener(aVoid -> listener.onSuccess())
                                    .addOnFailureListener(listener::onFailure);
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public interface OnUserRegisteredListener {
        void onSuccess();
        void onFailure(Exception e);
    }


    public void loginUser(String email, String password, OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public interface OnLoginListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public Task<Void> changePassword(String currentPass, String newPass) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !newPass.isEmpty()) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), currentPass);

            return user.reauthenticate(credential)
                    .continueWithTask(task -> {
                        if (task.isSuccessful()) {
                            return user.updatePassword(newPass);
                        }
                        throw task.getException();
                    });
        }
        return Tasks.forException(new Exception("Usuario no válido o contraseña vacía"));
    }

}

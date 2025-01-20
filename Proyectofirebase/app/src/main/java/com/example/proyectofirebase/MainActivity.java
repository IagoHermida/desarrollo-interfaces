package com.example.proyectofirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
        findViewById(R.id.loginButton).setOnClickListener(v -> loginUser());
        findViewById(R.id.logoutButton).setOnClickListener(v -> logoutUser());
        findViewById(R.id.readProductButton).setOnClickListener(v -> readProduct("producto1"));

    }

    private void registerUser() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loginUser() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error en autenticación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
    }
    private void readProduct(String productId) {
        // Verificar que la referencia a Firebase esté bien configurada
        if (databaseRef == null) {
            Toast.makeText(MainActivity.this, "Error de conexión a la base de datos", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseRef.child("productos").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String productName = snapshot.child("producto").getValue(String.class);
                    Long quantity = snapshot.child("cantidad").getValue(Long.class);
                    String imageUrl = snapshot.child("imagen").getValue(String.class);

                    // Crear un Intent para iniciar la nueva actividad
                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);

                    // Pasar los datos al Intent
                    intent.putExtra("productName", productName);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("imageUrl", imageUrl);

                    // Iniciar la nueva actividad
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Producto no encontrado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al leer datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

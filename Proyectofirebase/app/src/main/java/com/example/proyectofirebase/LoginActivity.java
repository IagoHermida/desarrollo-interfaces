package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofirebase.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        registerButton = findViewById(R.id.registerButton);

        // Crear una instancia del LoginViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observar el estado del inicio de sesión
        loginViewModel.isLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                // Si el inicio de sesión es exitoso, pasa a la siguiente actividad
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                finish();
            } else {
                // Si hay un error, muestra el mensaje de error
                Toast.makeText(LoginActivity.this, loginViewModel.getErrorMessage().getValue(), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });

        // Observar el mensaje de error
        loginViewModel.getErrorMessage().observe(this, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        });

        // Manejar el click en el botón de login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validar los campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa los datos correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                // Iniciar sesión
                progressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(email, password);
            }
        });
        registerButton.setOnClickListener(v -> {
            // Redirige a la pantalla de registro
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}

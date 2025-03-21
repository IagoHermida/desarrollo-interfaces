package com.example.aerocatalogo.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.aerocatalogo.R;
import com.example.aerocatalogo.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);

        // Aplicar el tema antes de setContentView()
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Verificar si ya hay un usuario autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Usuario ya autenticado, redirigir a MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Cerrar LoginActivity para evitar que el usuario regrese a ella
            return;  // Salir del metodo onCreate
        }

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Obtener referencias de las vistas
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        // Establecer descripciones de accesibilidad
        emailEditText.setContentDescription("Campo para escribir el correo electrónico");
        passwordEditText.setContentDescription("Campo para escribir la contraseña");
        loginButton.setContentDescription("Iniciar sesión");
        createAccountButton.setContentDescription("Crear una nueva cuenta");

        // Observadores
        loginViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                // Reseteamos el error para evitar que se dispare al recrear la actividad
                loginViewModel.resetLoginError();
            }
        });

        loginViewModel.isLoginSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful != null && isSuccessful) {
                Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();

                // Guardar el UID del usuario en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.apply();

                // Redirigir a MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                // Reseteamos el estado para evitar que se dispare al recrear la actividad
                loginViewModel.resetLoginState();
                finish();  // Finalizar la actividad actual (LoginActivity)
            }
        });

        // Configurar clic en los botones
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Delegar el inicio de sesión al ViewModel
            loginViewModel.loginUser(email, password);
        });

        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}

package com.example.aerocatalogo.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.aerocatalogo.R;
import com.example.aerocatalogo.viewmodels.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        
        // Obtener referencias de las vistas
        EditText fullNameEditText = findViewById(R.id.fullNameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);

        // Establecer descripciones de accesibilidad
        fullNameEditText.setContentDescription("Campo para escribir el nombre completo");
        emailEditText.setContentDescription("Campo para escribir el correo electrónico");
        phoneEditText.setContentDescription("Campo para escribir el número de teléfono");
        addressEditText.setContentDescription("Campo para escribir la dirección");
        passwordEditText.setContentDescription("Campo para escribir la contraseña");
        confirmPasswordEditText.setContentDescription("Campo para confirmar la contraseña");
        registerButton.setContentDescription("Registrar usuario");

        // Observadores para los resultados
        registerViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.isRegistrationSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful != null && isSuccessful) {
                Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Configurar el botón de registro
        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            // Delegar la validación y registro al ViewModel
            registerViewModel.registerUser(fullName, email, phone, address, password, confirmPassword);
        });
    }
}

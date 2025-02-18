package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofirebase.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar vistas
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);
        progressBar = view.findViewById(R.id.progressBar);
        registerButton = view.findViewById(R.id.registerButton);

        // Crear una instancia del LoginViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observar el estado del inicio de sesión
        loginViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                // Si el inicio de sesión es exitoso, pasa a la siguiente actividad
                Intent intent = new Intent(getActivity(), DashboardFragment.class);
                startActivity(intent);  // Inicia la actividad, no el fragmento

                // No es necesario usar finish() en un fragmento
                getActivity().finish(); // Cierra la actividad actual si lo deseas
            } else {
                // Si hay un error, muestra el mensaje de error
                Toast.makeText(getContext(), loginViewModel.getErrorMessage().getValue(), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });

        // Observar el mensaje de error
        loginViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        // Manejar el click en el botón de login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validar los campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, ingresa los datos correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                // Iniciar sesión
                progressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(email, password);
            }
        });

        registerButton.setOnClickListener(v -> {
            // Redirige a la pantalla de registro
            Intent intent = new Intent(getContext(), RegisterFragment.class);
            startActivity(intent);
        });

        return view; // Retornar la vista inflada
    }
}

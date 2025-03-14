package com.example.aerocatalogo.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aerocatalogo.R;
import com.example.aerocatalogo.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {
    // Constructor vacío
    public ProfileFragment() {}

    // Se infla el layout del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Reiniciamos el LiveData para que no tenga valores residuales
        profileViewModel.resetPasswordChangeResult();

        // Obtener referencias de las vistas
        TextView textViewDarkMode = view.findViewById(R.id.textViewDarkMode);
        Switch darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        FloatingActionButton returnFab = view.findViewById(R.id.returnFab);
        TextView textViewPassword = view.findViewById(R.id.textViewPassword);
        EditText currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        EditText newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);

        // Establecer descripciones de accesibilidad
        textViewDarkMode.setContentDescription("Modo oscuro");
        darkModeSwitch.setContentDescription("Activar o desactivar modo oscuro");
        returnFab.setContentDescription("Volver");
        textViewPassword.setContentDescription("Cambio de contraseña");
        currentPasswordEditText.setContentDescription("Campo para escribir la contraseña actual");
        newPasswordEditText.setContentDescription("Campo para escribir la nueva contraseña");
        changePasswordButton.setContentDescription("Cambiar contraseña");

        // Observar el estado del modo oscuro
        profileViewModel.getDarkModeLiveData().observe(getViewLifecycleOwner(), darkModeSwitch::setChecked);

        // Observar el resultado del cambio de contraseña
        profileViewModel.getPasswordChangeResult().observe(getViewLifecycleOwner(), result -> {
            // Si el resultado es null, salimos inmediatamente.
            if (result == null) {
                return;
            }
            switch (result) {
                case "SUCCESS":
                    Toast.makeText(requireContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_LENGTH":
                    Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_UPDATE":
                    Toast.makeText(requireContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                    break;
                case "ERROR_AUTH":
                    Toast.makeText(requireContext(), "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
                    break;
            }

            // Reiniciamos el valor para que no se vuelva a emitir al recrear el Fragment
            profileViewModel.resetPasswordChangeResult();
        });

        // Configurar switch
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Aplicar el tema inmediatamente
            profileViewModel.setDarkMode(isChecked);
        });

        // Configurar botón de volver
        returnFab.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Configurar botón de cambio de contraseña
        changePasswordButton.setOnClickListener(v -> {
            String currentPass = currentPasswordEditText.getText().toString();
            String newPass = newPasswordEditText.getText().toString();

            // Validar que los campos no estén vacíos
            if (currentPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(requireContext(), "Los campos de contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            profileViewModel.changePassword(currentPass, newPass);
        });

        return view;
    }
}

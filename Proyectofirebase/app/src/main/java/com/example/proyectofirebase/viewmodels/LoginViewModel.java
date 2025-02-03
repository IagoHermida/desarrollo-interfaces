package com.example.proyectofirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofirebase.repositories.UserRepository;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    // Método para iniciar sesión
    public void login(String email, String password) {
        userRepository.loginUser(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isLoggedIn.setValue(true);  // El inicio de sesión fue exitoso
                    } else {
                        errorMessage.setValue("Error en el inicio de sesión: " + task.getException().getMessage());
                        isLoggedIn.setValue(false);
                    }
                });
    }

    public LiveData<Boolean> isLoggedIn() {
        return isLoggedIn;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}

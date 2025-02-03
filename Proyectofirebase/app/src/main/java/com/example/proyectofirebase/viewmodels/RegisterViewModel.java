package com.example.proyectofirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofirebase.models.User;
import com.example.proyectofirebase.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> isSuccessful;
    private final MutableLiveData<String> errorMessage;

    public RegisterViewModel() {
        userRepository = new UserRepository();
        isSuccessful = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void registerUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setValue("Todos los campos son obligatorios.");
            return;
        }

        User user = new User(email, password);
        userRepository.registerUser(user, isSuccessful, errorMessage);
    }
}

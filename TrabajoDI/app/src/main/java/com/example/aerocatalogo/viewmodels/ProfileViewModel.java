package com.example.aerocatalogo.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aerocatalogo.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;


public class ProfileViewModel extends AndroidViewModel {
    // Variables dark mode
    private static final String PREFS_NAME = "AppConfig";
    private static final String KEY_DARK_MODE = "darkMode";
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Boolean> darkModeLiveData = new MutableLiveData<>();
    // Variables cambio contraseña
    private final UserRepository userRepository;
    private final MutableLiveData<String> passwordChangeResult = new MutableLiveData<>();

    public ProfileViewModel(Application application) {
        super(application);
        // Inicialización de dark mode
        sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        darkModeLiveData.setValue(sharedPreferences.getBoolean(KEY_DARK_MODE, false));
        // Inicialización de cambio de contraseña
        userRepository = new UserRepository();
    }

    public LiveData<Boolean> getDarkModeLiveData() {
        return darkModeLiveData;
    }

    public void setDarkMode(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isEnabled).apply();
        darkModeLiveData.setValue(isEnabled);
        applyDarkMode(isEnabled);
    }

    private void applyDarkMode(boolean enable) {
        if (enable) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void changePassword(String currentPass, String newPass) {
        if (newPass.length() < 6) {
            passwordChangeResult.setValue("ERROR_LENGTH");
            return;
        }

        userRepository.changePassword(currentPass, newPass)
                .addOnSuccessListener(aVoid -> passwordChangeResult.setValue("SUCCESS"))
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        passwordChangeResult.setValue("ERROR_AUTH");
                    } else {
                        passwordChangeResult.setValue("ERROR_UPDATE");
                    }
                });
    }

    public LiveData<String> getPasswordChangeResult() {
        return passwordChangeResult;
    }

    public void resetPasswordChangeResult() {
        passwordChangeResult.setValue(null);
    }
}


package com.example.aerocatalogo.utils;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

public class ItemCatalogoApp extends Application {
    private static final String PREFS_NAME = "AppConfig";
    private static final String DARK_MODE_KEY = "darkMode";

    @Override
    public void onCreate() {
        super.onCreate();

        // Recuperar y aplicar el tema guardado al iniciar la app
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean(DARK_MODE_KEY, false);

        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
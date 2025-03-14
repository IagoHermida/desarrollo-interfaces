package com.example.proyectofirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.repositories.UserRepository;

import java.util.List;

public class FavouritesViewModel extends ViewModel {
    private MutableLiveData<List<Item>> favoritosLiveData = new MutableLiveData<>();
    private UserRepository userRepository;

    public FavouritesViewModel() {
        userRepository = new UserRepository();
        favoritosLiveData = new MutableLiveData<>();
        cargarFavoritos();
    }

    public LiveData<List<Item>> getFavoritosLiveData() {
        return favoritosLiveData;
    }

    private void cargarFavoritos() {
        userRepository.obtenerFavoritos(favoritosLiveData);
    }
}

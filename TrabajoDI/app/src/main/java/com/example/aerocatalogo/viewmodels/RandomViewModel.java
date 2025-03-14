package com.example.aerocatalogo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.aerocatalogo.models.Item;
import com.example.aerocatalogo.repositories.RandomRepository;

import java.util.List;

public class RandomViewModel extends ViewModel {
    private final RandomRepository repository;

    public RandomViewModel() {
        repository = new RandomRepository();
        repository.fetchItems(); // Iniciar la carga de datos.
    }

    public LiveData<List<Item>> getItems() {
        return repository.getItemsLiveData();
    }

    public LiveData<String> getError() {
        return repository.getErrorLiveData();
    }

    // Limpiar listener tras logout
    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cleanup();
    }
}

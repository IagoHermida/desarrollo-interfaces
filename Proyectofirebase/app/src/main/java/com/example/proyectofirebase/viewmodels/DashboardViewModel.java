package com.example.proyectofirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofirebase.models.Item;
import com.example.proyectofirebase.repositories.DashboardRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final DashboardRepository repository;
    private final MutableLiveData<List<Item>> itemsLiveData;

    public DashboardViewModel() {
        repository = new DashboardRepository();
        itemsLiveData = new MutableLiveData<>();
        repository.obtenerItems(itemsLiveData);
    }

    private void cargarItems() {
        repository.obtenerItems(itemsLiveData);
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }


}

package com.example.haidtracker.ui.siklus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.haidtracker.data.api.ApiClient;
import com.example.haidtracker.data.api.ApiService;
import com.example.haidtracker.data.model.cycle.CreateCycleRequest;
import com.example.haidtracker.data.model.cycle.Cycle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiklusViewModel extends ViewModel {
    
    private MutableLiveData<List<Cycle>> cycles = new MutableLiveData<>();
    private ApiService apiService;

    public SiklusViewModel() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Cycle>> getCycles() {
        return cycles;
    }

    public void fetchCycles(String authToken) {
        apiService.getMyCycles(authToken).enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {
                if (response.isSuccessful()) {
                    cycles.setValue(response.body());
                } else {
                    cycles.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                cycles.setValue(null);
            }
        });
    }

    public void createCycle(String authToken, CreateCycleRequest request, Callback<Cycle> callback) {
        apiService.createCycle(authToken, request).enqueue(callback);
    }

    public void deleteCycle(String authToken, int cycleId, Callback<Void> callback) {
        apiService.deleteCycle(authToken, cycleId).enqueue(callback);
    }
}

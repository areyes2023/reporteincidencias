package com.aplicacion.incidencias.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevoReporteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NuevoReporteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Crear Nuevo Reporte de Incidencias");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
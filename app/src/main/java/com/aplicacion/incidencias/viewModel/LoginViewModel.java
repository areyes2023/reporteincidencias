package com.aplicacion.incidencias.viewModel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aplicacion.incidencias.model.Login;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Login> userMutableLiveData;

    public MutableLiveData<Login> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void login(String username, String password) {
        userMutableLiveData.setValue(new Login(username, password));
    }
}

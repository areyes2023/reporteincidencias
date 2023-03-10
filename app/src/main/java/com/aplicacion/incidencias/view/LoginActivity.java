package com.aplicacion.incidencias.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aplicacion.incidencias.databinding.ActivityLoginBinding;
import com.aplicacion.incidencias.model.Login;
import com.aplicacion.incidencias.viewModel.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getUser().observe(this, new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                binding.loading.setVisibility(View.GONE);
                if (TextUtils.isEmpty(Objects.requireNonNull(login).getStrUser())) {
                    binding.user.setError("Ingresar el Usuario");
                    binding.user.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(login).getStrPass())) {
                    binding.pass.setError("Ingresar la contraseña");
                    binding.pass.requestFocus();
                }else if(!login.getStrUser().toLowerCase().equals("admin")) {
                    binding.user.setError("Usuario incorrecto");
                    binding.user.requestFocus();
                }else if(!login.getStrPass().toLowerCase().equals("admin")) {
                    binding.pass.setError("Contraseña incorrecto");
                    binding.pass.requestFocus();
                }
                else {
                    binding.user.setText(login.getStrUser());
                    binding.pass.setText(login.getStrPass());
                    Toast.makeText(getApplicationContext(), "Bienvenido, "+login.getStrUser(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loading.setVisibility(View.VISIBLE);
                loginViewModel.login(binding.user.getText().toString(), binding.pass.getText().toString() );
            }
        });

    }
}
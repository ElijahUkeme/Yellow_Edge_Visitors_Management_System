package com.elijah.ukeme.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.visitorsmanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    FloatingActionButton fab;
    ProgressBar progressBar;

    EditText username, password;

    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        textView = findViewById(R.id.textviewSignUp);
        fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progress_bar);
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);

        fab.setOnClickListener(v -> {
            login();

        });

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void intoDashBoard(){
        Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
        startActivity(intent);
    }

    private void login(){
        String name = username.getText().toString();
        String pass = password.getText().toString();
        if (name.isEmpty()){
            username.setError("Username required");
            username.requestFocus();
            cancel = true;
        }else if (pass.isEmpty()){
            password.setError("password Required");
            password.requestFocus();
            cancel = true;
        }else {
            Call <LoginResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .userLogin(name,pass);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getStatus() == 1){
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    
}
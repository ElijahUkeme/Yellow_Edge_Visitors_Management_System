package com.elijah.ukeme.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.visitorsmanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class SignUpActivity extends AppCompatActivity {

    EditText username, password, firstname, lastname, email, phone, houseId;
    Button signUp;
    TextView signIn;
    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.editText_username_signUP);
        password = findViewById(R.id.editText_password_signup);
        firstname = findViewById(R.id.editText_firstname_signup);
        lastname = findViewById(R.id.editText_lastname_signup);
        email = findViewById(R.id.editText_email_signup);
        phone = findViewById(R.id.editText_phone_number_signup);
        houseId = findViewById(R.id.editText_house_id_signup);
        signUp = findViewById(R.id.sign_up_button);
        signIn = findViewById(R.id.textview_to_sign_in);

        signIn.setOnClickListener(v -> toLogIn());

        signUp.setOnClickListener(v -> signUp());
    }

    private void toLogIn() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        String name = username.getText().toString();
        String userPassword = password.getText().toString();
        String firstName = firstname.getText().toString();
        String lastName = lastname.getText().toString();
        String userEmail = email.getText().toString().trim();
        String phoneNumber = phone.getText().toString();
        String houseIdNumber = houseId.getText().toString();

        if (name.isEmpty()) {
            username.setError("Username required");
            username.requestFocus();
            cancel = true;
        } else if (userPassword.isEmpty()) {
            password.setError("Password required");
            password.requestFocus();
            cancel = true;
        } else if (userPassword.length() < 6) {
            password.setError("Password must be atleast 6 characters");
            password.requestFocus();
            cancel = true;
        } else if (firstName.isEmpty()) {
            firstname.setError("First name required");
            firstname.requestFocus();
            cancel = true;
        } else if (lastName.isEmpty()) {
            lastname.setError("Last name required");
            lastname.requestFocus();
            cancel = true;
        } else if (userEmail.isEmpty()) {
            email.setError("Email required");
            email.requestFocus();
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            cancel = true;
        } else if (phoneNumber.isEmpty()) {
            phone.setError("Phone number required");
            phone.requestFocus();
            cancel = true;
        } else if (houseIdNumber.isEmpty()) {
            houseId.setError("House id required");
            houseId.requestFocus();
            cancel = true;
        } else {
            // go ahead and register user by making the api call

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .registerUser(name, userPassword, firstName, lastName, userEmail, phoneNumber, houseIdNumber);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String res = null;
                    try {


                        if (response.code() == 0) {
                            res = response.body().string();

                        } else {
                             res = response.errorBody().string();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (res != null){
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
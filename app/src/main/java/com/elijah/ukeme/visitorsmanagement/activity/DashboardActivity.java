package com.elijah.ukeme.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elijah.ukeme.visitorsmanagement.R;

public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView cardView,newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_item);

        cardView = findViewById(R.id.card_profile);
        newUser = findViewById(R.id.register_new_user);

        newUser.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this,ProfileActivity.class);
            startActivity(intent);
        });

        initToolbar();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
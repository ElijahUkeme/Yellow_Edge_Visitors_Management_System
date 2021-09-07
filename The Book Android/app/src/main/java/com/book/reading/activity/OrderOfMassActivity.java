package com.book.reading.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.book.reading.R;

public class OrderOfMassActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView intro, liturgy,eucharist,communion,concluding,important,confess,gloria,nicen,apostleCreed,holy,father,lamb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_of_mass);
        toolbar = findViewById(R.id.tool);
        toolbar.setTitle("ORDER OF MASS: ENGLISH/LATIN");
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intro = findViewById(R.id.intro_rites);
        liturgy = findViewById(R.id.liturgy_of_word);
        eucharist = findViewById(R.id.liturgy_of_eucharist);
        communion = findViewById(R.id.communion_rite);
        concluding = findViewById(R.id.concluding_rites);
        important = findViewById(R.id.important_prayers);
        confess = findViewById(R.id.i_confess);
        gloria = findViewById(R.id.gloria);
        nicen = findViewById(R.id.niceno);
        apostleCreed = findViewById(R.id.apostles_creed);
        holy = findViewById(R.id.holy_holy);
        father = findViewById(R.id.our_father);
        lamb = findViewById(R.id.lamb_of_god);

        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,IntroductoryActivity.class);
                startActivity(intent);
            }
        });
        liturgy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,LiturgyActivity.class);
                startActivity(intent);

            }
        });
        eucharist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,EcharistActivity.class);
                startActivity(intent);
            }
        });
        communion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,CommunionActivity.class);
                startActivity(intent);
            }
        });
        concluding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,ConcludingActivity.class);
                startActivity(intent);
            }
        });
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        confess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,ConfessionActivity.class);
                startActivity(intent);
            }
        });
        gloria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,GloriaActivity.class);
                startActivity(intent);
            }
        });
        nicen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,NicenoActivity.class);
                startActivity(intent);
            }
        });
        apostleCreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,ApostleActivity.class);
                startActivity(intent);
            }
        });
        holy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,HolyActivity.class);
                startActivity(intent);
            }
        });
        father.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,OurFatherActivity.class);
                startActivity(intent);
            }
        });
        lamb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderOfMassActivity.this,LambActivity.class);
                startActivity(intent);
            }
        });
    }

}
package com.book.reading.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.book.reading.DatabaseHelper.MainDBHelper;
import com.book.reading.R;

public class IndexActivity extends AppCompatActivity {

    Button exit,submit;
    RadioGroup group;
    RadioButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15;
    String clickIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        exit = findViewById(R.id.button_exit);
        submit = findViewById(R.id.button_submit);
        group = findViewById(R.id.radioGroup);
        btn1 = findViewById(R.id.radioButton);
        btn2 = findViewById(R.id.radioButton2);
        btn3 = findViewById(R.id.radioButton3);
        btn4 = findViewById(R.id.radioButton4);
        btn5 = findViewById(R.id.radioButton5);
        btn6 = findViewById(R.id.radioButton6);
        btn7 = findViewById(R.id.radioButton7);
        btn8 = findViewById(R.id.radioButton8);
        btn9 = findViewById(R.id.radioButton9);
        btn10 = findViewById(R.id.radioButton10);
        btn11 = findViewById(R.id.radioButton11);
        btn12 = findViewById(R.id.radioButton12);
        btn13 = findViewById(R.id.radioButton13);
        btn14 = findViewById(R.id.radioButton14);
        btn15 = findViewById(R.id.radioButton15);

            btn1.setText("Entrance Hymns 1 - 42F");
            btn1.setTextColor(Color.parseColor("#06610D"));

            btn2.setText("Offertory Hymns 43 - 67A");
            btn2.setTextColor(Color.parseColor("#06610D"));

            btn3.setText("Communion Hymns 68 - 125C");
            btn3.setTextColor(Color.parseColor("#06610D"));

            btn4.setText("Recessional Hymns 126 - 155");
            btn4.setTextColor(Color.parseColor("#06610D"));

            btn5.setText("Hymns for Advent 156 - 164");
            btn5.setTextColor(Color.parseColor("#06610D"));

            btn6.setText("Hymns for Christmas 165 - 190");
            btn6.setTextColor(Color.parseColor("#06610D"));

            btn7.setText("Hymns for Lent 191 - 202");
            btn7.setTextColor(Color.parseColor("#06610D"));

            btn8.setText("Hymns for Easter 203 - 217");
            btn8.setTextColor(Color.parseColor("#06610D"));

            btn9.setText("General Hymns 218 - 270");
            btn9.setTextColor(Color.parseColor("#06610D"));

            btn10.setText("Hymns to Our Lord 271 - 289");
            btn10.setTextColor(Color.parseColor("#06610D"));

            btn11.setText("Hymns to Our Lady 290 - 309");
            btn11.setTextColor(Color.parseColor("#06610D"));

            btn12.setText("Hymns for the Dead 310 - 316");
            btn12.setTextColor(Color.parseColor("#06610D"));

            btn13.setText("Latin Hymns 317 - 339");
            btn13.setTextColor(Color.parseColor("#06610D"));

            btn14.setText("Aditional Hymns 340 - 357");
            btn14.setTextColor(Color.parseColor("#06610D"));

            btn15.setText("Other Traditional Hymns 358 - 383");
            btn15.setTextColor(Color.parseColor("#06610D"));


        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        clickIndex = "one";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton2:
                        clickIndex = "two";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton3:
                        clickIndex = "three";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton4:
                        clickIndex = "four";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton5:
                        clickIndex = "five";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton6:
                        clickIndex = "six";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton7:
                        clickIndex = "seven";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton8:
                        clickIndex = "eight";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton9:
                        clickIndex = "nine";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton10:
                        clickIndex = "ten";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton11:
                        clickIndex = "eleven";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton12:
                        clickIndex = "twelve";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton13:
                        clickIndex = "thirteen";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton14:
                        clickIndex = "fourteen";
                        break;
                }
                switch (checkedId){
                    case R.id.radioButton15:
                        clickIndex = "fifteen";
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              goToTheHymns();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void goToTheHymns(){

        Intent intent = new Intent(IndexActivity.this,MainActivity.class);
        intent.putExtra("index",clickIndex);
        startActivity(intent);
    }
}
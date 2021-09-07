package com.book.reading.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.book.reading.R;

public class ContactActivity extends AppCompatActivity {
    EditText senderEmail, receiverEmail,subject,message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        senderEmail = findViewById(R.id.editText_sender_email);
        receiverEmail = findViewById(R.id.editText_receipient_email);
        subject = findViewById(R.id.editText_subject);
        message = findViewById(R.id.editText_message);
        send = findViewById(R.id.button_send_email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }
    private void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,receiverEmail.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT,message.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent,"Send mail..."));
            Toast.makeText(this, "Message send", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
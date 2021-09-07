package com.elijah.ukeme.visitorsmanagement.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.elijah.ukeme.visitorsmanagement.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;

    TextView textView;
    RadioGroup group;
    AppCompatRadioButton male,female;
    Button buttonSignUp,date,time;
    AppCompatEditText name,email,phone,address,location,purpose,meetingWith,venue;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = findViewById(R.id.select_photo);
        name = findViewById(R.id.editText_name);
        email = findViewById(R.id.editText_email);
        phone = findViewById(R.id.editText_phone_number);
        address = findViewById(R.id.editText_address);
        location = findViewById(R.id.editText_location);
        purpose = findViewById(R.id.editText_purpose);
        meetingWith = findViewById(R.id.editText_meeting_with);
        venue = findViewById(R.id.editText_meeting_venue);
        date = findViewById(R.id.date_registration);
        time = findViewById(R.id.time_registration);
        group = findViewById(R.id.gender);
        male = findViewById(R.id.radio_male);
        female = findViewById(R.id.radio_female);
        mImageView = findViewById(R.id.circular_image);

        date.setOnClickListener(v -> {
            setDatePicker((Button)v);

        });

        time.setOnClickListener(v -> {
            setTimePicker((Button)v);

        });

        buttonSignUp = findViewById(R.id.email_sign_in_button);
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        textView.setOnClickListener(v -> {
            openFileChooser();
            textView.setVisibility(View.INVISIBLE);
        });
    }

    private void setDatePicker(final Button btn){
        Calendar current_calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DATE,dayOfMonth);
            long date_millis = calendar.getTimeInMillis();
            btn.setText(getFormattedDateEvent(date_millis));
        },
                current_calendar.get(Calendar.YEAR),
                current_calendar.get(Calendar.MONTH),
                current_calendar.get(Calendar.DATE));

        pickerDialog.setThemeDark(false);
        pickerDialog.setAccentColor(getResources().getColor(R.color.pink_600));
        pickerDialog.setMinDate(current_calendar);
       pickerDialog.show(getFragmentManager(),"Date pickerDialog");

    }

    public static String getFormattedDateEvent(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
        return newFormat.format(new Date(dateTime));
    }

    private void setTimePicker(final Button button){
        Calendar cur_calendar = Calendar.getInstance();
        TimePickerDialog pickerDialog = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.AM_PM,calendar.get(Calendar.AM_PM));
            long time_millis = calendar.getTimeInMillis();
            button.setText(getFormattedTimeEvent(time_millis));

        },
                cur_calendar.get(Calendar.HOUR_OF_DAY),cur_calendar.get(Calendar.MINUTE),true);
        pickerDialog.setThemeDark(false);
        pickerDialog.setAccentColor(getResources().getColor(R.color.pink_600));
        pickerDialog.show(getFragmentManager(),"DialogTimePicker");
    }
    public static String getFormattedTimeEvent(Long time) {
        SimpleDateFormat newFormat = new SimpleDateFormat("h:mm a");
        return newFormat.format(new Date(time));
    }

    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

}
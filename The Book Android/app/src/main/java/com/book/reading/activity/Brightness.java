package com.book.reading.activity;

import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.book.reading.R;

public class Brightness extends AppCompatActivity {
	private SeekBar seekBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seekbar);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setMax(255);
		float curBrightnessValue = 0;
		try {
			curBrightnessValue = android.provider.Settings.System.getInt(getApplicationContext().
					getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}

		int screen_brightness = (int) curBrightnessValue;
		seekBar.setProgress(screen_brightness);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;
			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue,
					boolean fromUser) {
				progress = progresValue;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				android.provider.Settings.System.putInt(getApplication().getContentResolver(),
						android.provider.Settings.System.SCREEN_BRIGHTNESS,
						progress);
			}
		});
		
	}

	

}

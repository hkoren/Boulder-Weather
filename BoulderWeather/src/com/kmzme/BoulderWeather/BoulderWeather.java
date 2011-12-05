package com.kmzme.BoulderWeather;

import com.kmzme.BoulderWeather.BoulderWeather.PressureUnit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class BoulderWeather extends Activity {
	
	public enum TempUnit {
		CELSIUS,
		FAHRENHEIT,
		KELVIN
	}
	
	public static TempUnit selectedTempUnit = TempUnit.FAHRENHEIT;
	public static final String TEMP_SYMBOL = "F";
	
	public enum SpeedUnit {
		M_PER_S,
		MILES_PER_H
	}
	public static SpeedUnit selectedSpeedUnit = SpeedUnit.MILES_PER_H;
	
	public static final String TAG = "BoulderWeather";

	public static final String MESA_URL = "http://kmz.me/mesa";
	public static final String FOOTHILLS_URL = "http://kmz.me/foothills";
	public static final String BOULDERXWEATHER_TWITTER = "http://twitter.com/#!/boulderXweather";
	public static final String NATIONAL_WEATHER_SERVICE = "http://www.crh.noaa.gov/bou/";
	public static final String WEATHER_UNDERGROUND = "http://www.wunderground.com/US/CO/Boulder.html"; 
	public static final String WEATHER_CHANNEL = "http://www.weather.com/weather/today/Boulder+CO+USCO0038";
	public static final PressureUnit selectedPressureUnit = PressureUnit.INHG;
	public enum PressureUnit {
		INHG,
		MBAR,
		PSI
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupForecast();
		setupStations();
		setupImagery();
	}

	private void setupImagery() {
		
	}

	private void setupStations() {
		URLClickButton(R.id.ncar_mesa, MESA_URL);
		URLClickButton(R.id.ncar_foothills, FOOTHILLS_URL);
		
	}

	private void setupForecast() {
		URLClickButton(R.id.boulder_weather_twitter, BOULDERXWEATHER_TWITTER);
		URLClickButton(R.id.colorado_weather, FOOTHILLS_URL);
	}
	
	
	
	private class URLClickListener implements OnClickListener {
		private String url;
		public URLClickListener(String url) {
			this.url= url;
		}
		public void onClick(View v) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}		
	}
	private void URLClickButton(int id, String url) {
        Button button = (Button)findViewById(id);
        button.setOnClickListener(new URLClickListener(url));		
	}

}

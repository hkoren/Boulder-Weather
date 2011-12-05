package com.kmzme.BoulderWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class WeatherInfo {

	public static final float KELVIN_OFFSET=273.15f;
	public static final float MPS_PER_MPH = 0.44704f;
	public static final float MPH_PER_KMPH = 1.609344f;
	public static final float INHG_PER_MBAR = 0.0295333727f;
	public static final float PSI_PER_MBAR = 2.03625437f;
	
	public NCARStation mesa, foothills;
	private Context context;
	
	public WeatherInfo(Context context) {
		this.context =context;
		
		String restURL = "http://app.boulderandroid.com/weatherfeed" ;
		RestClient client = new RestClient(restURL);
		Log.e(BoulderWeather.TAG, "Querying " + restURL);

		try {
			client.Execute(RequestMethod.GET);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String response = client.getResponse();
		Log.e(BoulderWeather.TAG, "Response: " + response);
		//TextResults.setText(response);
		try {
			System.out.println(response);
			String jsonContent = response.substring(0, response.length());
		    JSONArray jArray = new JSONArray(jsonContent);
	
			JSONObject mesaJSON = jArray.getJSONObject(NCARStation.MESA);
			JSONObject foothillsJSON = jArray.getJSONObject(NCARStation.FOOTHILLS);
			
			mesa      = new NCARStation(NCARStation.MESA,      mesaJSON);
			foothills = new NCARStation(NCARStation.FOOTHILLS, foothillsJSON);
			
			      //TextResults.setText(temp) ; 

		} catch (JSONException e) {
		       //BoulderWeather.Log.e(BoulderWeather.TAG, e.getMessage()) ;
		}
	}
	
	public static float ctof(float deg_c) {
		return ((9f/5f)*deg_c) + 32f;
	}
	public static float ftoc(float deg_f) {
		return (deg_f-32) * (5f/9f);
	}
	public static float ctok(float deg_c) {
		return deg_c + KELVIN_OFFSET;
	}
		
	public static float getTemp(float deg_c) {
		if (BoulderWeather.selectedTempUnit == BoulderWeather.TempUnit.FAHRENHEIT) {
			return ctof(deg_c);
		} else if (BoulderWeather.selectedTempUnit == BoulderWeather.TempUnit.CELSIUS) {
			return deg_c;
		} else {			// Kelvin
			return ctok(deg_c);
		}
	}
	
	
	// Speed Conversion
	private static float mpstokmph(float speed_m_per_sec) {
		return speed_m_per_sec / MPS_PER_MPH / MPH_PER_KMPH;
	}

	private static float mpstomph(float speed_m_per_sec) {
		return speed_m_per_sec / MPS_PER_MPH;
	}
	
	public static float getSpeed(float speed_m_per_sec) {
		if (BoulderWeather.selectedSpeedUnit == BoulderWeather.SpeedUnit.MILES_PER_H) {
			return mpstomph(speed_m_per_sec);
		} else if (BoulderWeather.selectedSpeedUnit == BoulderWeather.SpeedUnit.M_PER_S) {
			return speed_m_per_sec;
		} else {			// Kmph
			return mpstokmph(speed_m_per_sec);
		}
	}
	
	// Pressure Conversion
	private static float mbartoinhg(float mbar) {
		return mbar * INHG_PER_MBAR;
	}
	
	private static float mbartopsi(float mbar) {
		return mbar * PSI_PER_MBAR;
	}


	public static float getPressure(float pressure_mbar) {
		if (BoulderWeather.selectedPressureUnit == BoulderWeather.PressureUnit.INHG) {
			return mbartoinhg(pressure_mbar);
		} else if (BoulderWeather.selectedPressureUnit == BoulderWeather.PressureUnit.PSI) {
			return mbartopsi(pressure_mbar);
		} else {
			return pressure_mbar;
		}
	}

	public static String pressureUnit() {
		switch(BoulderWeather.selectedPressureUnit) {
		case INHG:
			return "\"Hg";
		case PSI:
			return "PSI";
		case MBAR:
			return "mBar";
		default:
			return "?";
		}
	}

}

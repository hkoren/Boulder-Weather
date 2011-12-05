package com.kmzme.BoulderWeather;

import org.json.JSONException;
import org.json.JSONObject;


public class NCARStation extends Station {
	private String station;
	private int stationId;
	private float temp_deg_c, relative_humidity, pressure_millibars, 
				dewpoint_deg_c, wind_chill_deg_c, wind_speed_m_per_s, 
				peak_gust_m_per_s, wind_from_deg, rain_accumulation_mm;
	private String wind_from_dir;

	
	public NCARStation(int stationId, JSONObject jsonData) {
		try {
			this.stationId = stationId; 
			temp_deg_c           = new Float(jsonData.getString("temp_deg_c"));
			relative_humidity    = new Float(jsonData.getString("relative_humidity"));
			dewpoint_deg_c       = new Float(jsonData.getString("dewpoint_deg_c"));
			wind_chill_deg_c     = new Float(jsonData.getString("wind_chill_deg_c"));
			wind_from_deg        = new Float(jsonData.getString("wind_from_deg"));
			wind_speed_m_per_s   = new Float(jsonData.getString("wind_speed_m_per_s"));
			peak_gust_m_per_s    = new Float(jsonData.getString("peak_gust_m_per_s"));
			rain_accumulation_mm = new Float(jsonData.getString("rain_accumulation_mm"));
			pressure_millibars   = new Float(jsonData.getString("pressure_millibars"));
			wind_from_dir        = jsonData.getString("wind_from_dir");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static final int MESA = 0;
	static final int FOOTHILLS = 1;
	
	
	public float getTemp() {
		return WeatherInfo.getTemp(temp_deg_c);
	}
	public float getDewPoint() {
		return WeatherInfo.getTemp(dewpoint_deg_c);
	}
	public float getWindChill() {
		return WeatherInfo.getTemp(wind_chill_deg_c);
	}
	public float getWindSpeedAverage() {
		return WeatherInfo.getSpeed(wind_speed_m_per_s);
	}
	public float getWindSpeedPeak() {
		return WeatherInfo.getSpeed(peak_gust_m_per_s);
	}
	public float getWindDirectionDegrees() {
		return wind_from_deg;
	}
	public String getWindDirection() {
		return wind_from_dir;
	}
	public float getHumidity() {
		return relative_humidity;
	}
	
	public float getPressure() {
		return WeatherInfo.getPressure(pressure_millibars);
	}
	public String formatTemp() {
		return String.valueOf(Math.round(getTemp()*10f)/10f) + "°" + BoulderWeather.TEMP_SYMBOL;
	}
	public String formatDewPoint() {
		return String.valueOf(Math.round(getDewPoint()*10f)/10f) + "°" + BoulderWeather.TEMP_SYMBOL;
	}
	public String formatHumidity() {
		// TODO Auto-generated method stub
		return String.valueOf(Math.round(getHumidity())) + "%";
	}
	public String formatPressure() {
		// TODO Auto-generated method stub
		return String.valueOf(Math.round(getPressure())) + WeatherInfo.pressureUnit();
	}
}


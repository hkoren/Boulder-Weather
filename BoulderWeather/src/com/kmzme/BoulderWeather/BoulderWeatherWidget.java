package com.kmzme.BoulderWeather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class BoulderWeatherWidget extends AppWidgetProvider  {

	private static final String ACTION_WIDGET_TOUCH = "touch";
	private static final String ACTION_WIDGET_MESA = "mesa";
	private static final String ACTION_WIDGET_FOOTHILLS = "foothills";
	private final int UPDATE_INTERVAL = 5*60*1000;
	private final int[] desc_IDs = { R.id.top_left, R.id.temp_heading, R.id.h_p_heading, R.id.wind_heading };
	private final int[] mesa_IDs = { R.id.heading_mesa, R.id.temp_mesa, R.id.dewpoint_mesa, R.id.humidity_mesa, R.id.pressure_mesa, R.id.wind_mesa };
	private final int[] foothills_IDs = { R.id.heading_foothills, R.id.dewpoint_foothills, R.id.temp_foothills, R.id.pressure_foothills, R.id.humidity_foothills, R.id.wind_foothills };
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Timer timer = new Timer();
		Log.i(BoulderWeather.TAG, "Running Update");
		timer.scheduleAtFixedRate(new WeatherUpdate(context, appWidgetManager), 1, UPDATE_INTERVAL);
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		// Main App Button
		Intent active = new Intent(context, BoulderWeather.class);
		active.setAction(ACTION_WIDGET_TOUCH);
		PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, active, 0);		
		putPendingIntentToGroup(desc_IDs,actionPendingIntent,remoteViews);

		// Foothills Button
		Intent foothillsIntent = new Intent(Intent.ACTION_VIEW);
		foothillsIntent.setData(Uri.parse(BoulderWeather.FOOTHILLS_URL));
		PendingIntent foothillsPendingIntent = PendingIntent.getActivity(context, 0, foothillsIntent, 0);		
		putPendingIntentToGroup(foothills_IDs,foothillsPendingIntent,remoteViews);

		// Mesa Button
		Intent mesaIntent = new Intent(Intent.ACTION_VIEW);
		mesaIntent.setData(Uri.parse(BoulderWeather.MESA_URL));
		PendingIntent mesaPendingIntent = PendingIntent.getActivity(context, 0, mesaIntent, 0);		
		putPendingIntentToGroup(mesa_IDs,mesaPendingIntent,remoteViews);
		
		// Update the Widget with the remote views
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
	
	private void putPendingIntentToGroup(int[] IDs, PendingIntent pendingIntent, RemoteViews remoteViews) {
		for (int id : IDs) {
			remoteViews.setOnClickPendingIntent(id, pendingIntent);
		}
		
		
		
	}
	
	private class WeatherUpdate extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		Context context;
		
		DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
		public WeatherUpdate(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			this.context=context;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			thisWidget = new ComponentName(context, BoulderWeatherWidget.class);
		}
		@Override
		public void run() {
			Log.i(BoulderWeather.TAG, "WeatherUpdate run()");
			WeatherInfo w = new WeatherInfo(context);
			
				remoteViews.setTextViewText(R.id.temp_foothills, getTemp(w.foothills));
				remoteViews.setTextViewText(R.id.dewpoint_foothills, getDewPoint(w.foothills));
				remoteViews.setTextViewText(R.id.temp_mesa,          getTemp(w.mesa));
				remoteViews.setTextViewText(R.id.dewpoint_mesa,      getDewPoint(w.mesa));
				remoteViews.setTextViewText(R.id.humidity_foothills, getHumidity(w.foothills));
				remoteViews.setTextViewText(R.id.pressure_foothills, getPressure(w.foothills));
				remoteViews.setTextViewText(R.id.humidity_mesa,      getHumidity(w.mesa));
				remoteViews.setTextViewText(R.id.pressure_mesa,      getPressure(w.mesa));
				remoteViews.setTextViewText(R.id.wind_foothills, getWind(w.foothills));
				remoteViews.setTextViewText(R.id.wind_mesa,      getWind(w.mesa));
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		}
		
		
	}
	public String getTemp(NCARStation station) {
		return station.formatTemp();
	}
	
	public String getDewPoint(NCARStation station) {
		return station.formatDewPoint();
	}
	
	public String getHumidity(NCARStation station) {
		return station.formatHumidity();
	}
	public String getPressure(NCARStation station) {
		return station.formatPressure();
	}	
	public String getHP(NCARStation station) {
		return String.valueOf(Math.round(station.getHumidity())) + "% / " + String.valueOf(Math.round(station.getPressure()) + WeatherInfo.pressureUnit());
	}
	public String getWind(NCARStation station) {
		return Math.round(station.getWindSpeedAverage()) + " (" + Math.round(station.getWindSpeedPeak()) + " max) " + station.getWindDirection();
	}
}
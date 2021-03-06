package org.hcilab.projects.logeverything.sensor.implementation;

import java.io.IOException;

import org.hcilab.projects.logeverything.sensor.AbstractSensor;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


public class AirplaneModeSensor extends AbstractSensor {
	
	private static final long serialVersionUID = 1L;

	public AirplaneModeSensor() {
		m_IsRunning = false;
		TAG = getClass().getName();
		SENSOR_NAME = "Airplane Mode";
		FILE_NAME = "airplane_mode.csv";
		m_FileHeader = "TimeUnix,Value";
	}

	@Override
	public View getSettingsView(Context context) {
		return null;
	}

	@Override
	public boolean isAvailable(Context context) {
		return true;
	}
	
	private static boolean isAirplaneModeOn(Context context) {
		return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
	}

	@Override
	public void start(Context context){
		super.start(context);
		Long t = System.currentTimeMillis();
		if (!m_isSensorAvailable)
			return;

		try {		
			if(isAirplaneModeOn(context)) {
				m_FileWriter.write(t + ",on");
			} else {
				m_FileWriter.write(t + ",off");
			}			
			m_FileWriter.write("\n");
			m_FileWriter.flush();			
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		m_IsRunning = true;
	}

	@Override
	public void stop() {
		if(m_IsRunning) {
			m_IsRunning = false;
			try {
				m_FileWriter.flush();
				m_FileWriter.close();
				m_FileWriter = null;
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}		
			
		}	
	}

}

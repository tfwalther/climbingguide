package de.climbingguide.erzgebirsgrenzgebiet;

 import java.text.DateFormat;
import java.util.Calendar;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.renderer.MapWorker;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.reader.MapFile;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

public class ClimbingGuideApplication extends Application {

	/*
	 * type to use for maps to store in the external files directory
	 */
	public static final String MAPS = "maps";

	public static final String SETTING_DEBUG_TIMING = "debug_timing";
	public static final String SETTING_SCALE = "scale";
	public static final String SETTING_TEXTWIDTH = "textwidth";
	public static final String SETTING_WAYFILTERING = "wayfiltering";
	public static final String SETTING_WAYFILTERING_DISTANCE = "wayfiltering_distance";
	public static final String SETTING_TILECACHE_PERSISTENCE = "tilecache_persistence";
	public static final String SETTING_TILECACHE_THREADING = "tilecache_threading";
	public static final String SETTING_TILECACHE_QUEUESIZE = "tilecache_queuesize";
	public static final String TAG = "SAMPLES APP";	
	
    private static ClimbingGuideApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		AndroidGraphicFactory.createInstance(this);
		Log.e(TAG,
				"Device scale factor "
						+ Float.toString(DisplayModel.getDeviceScaleFactor()));
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		float fs = Float.valueOf(preferences.getString(SETTING_SCALE,
				Float.toString(DisplayModel.getDefaultUserScaleFactor())));
		Log.e(TAG, "User ScaleFactor " + Float.toString(fs));
		if (fs != DisplayModel.getDefaultUserScaleFactor()) {
			DisplayModel.setDefaultUserScaleFactor(fs);
		}

		MapFile.wayFilterEnabled = preferences.getBoolean(SETTING_WAYFILTERING, true);
		if (MapFile.wayFilterEnabled) {
			MapFile.wayFilterDistance = Integer.parseInt(preferences.getString(SETTING_WAYFILTERING_DISTANCE, "20"));
		}
		MapWorker.DEBUG_TIMING = preferences.getBoolean(SETTING_DEBUG_TIMING, false);
	}    
    
    public ClimbingGuideApplication() {
        setInstance(this);
    }

    private static void setInstance(final ClimbingGuideApplication application) {
        instance = application;
    }

    public static ClimbingGuideApplication getInstance() {
        return instance;
    }
    
    //Utilitys
    public static String getDate(long time) {
    	DateFormat formatter = DateFormat.getDateInstance();
			
    	Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }
    
    public static void setEigeneWegeHinweis(TextView textViewHinweis, boolean isVorgestiegen, boolean isRP, boolean isoU) {
    	String hinweis=null;
    	if (isRP) {
    		getInstance().getString(R.string.hinweis_rp);
    	} else {
	    	if (isVorgestiegen) {
	    		hinweis = getInstance().getString(R.string.hinweis_vorstieg);
	    	} else {
	    		hinweis = getInstance().getString(R.string.hinweis_nachstieg);
	    	}
	    	if (isoU) {
	    		hinweis += " " + getInstance().getString(R.string.oulong);
	    	}
    	}
		if (isRP) {
			textViewHinweis.setTextColor(getInstance().getResources().getColor(R.color.darkred));
			hinweis = getInstance().getString(R.string.hinweis_rp);
		}
		else textViewHinweis.setTextColor(Color.BLACK);
		textViewHinweis.setText(hinweis);
    }
    
    public static void setTabsSettings(final PagerSlidingTabStrip tabs) {
		tabs.setIndicatorColorResource(R.color.lightblue);
		tabs.setTextColor(Color.BLACK);	
		tabs.setBackgroundColor(getInstance().getResources().getColor(R.color.tabsgrey));
    }
    
}

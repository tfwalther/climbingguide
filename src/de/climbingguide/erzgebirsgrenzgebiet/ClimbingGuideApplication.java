package de.climbingguide.erzgebirsgrenzgebiet;

 import java.text.DateFormat;
import java.util.Calendar;

import android.app.Application;
import android.graphics.Color;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

public class ClimbingGuideApplication extends Application {

    private static ClimbingGuideApplication instance;

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

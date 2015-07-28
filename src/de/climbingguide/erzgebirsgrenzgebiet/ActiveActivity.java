package de.climbingguide.erzgebirsgrenzgebiet;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class ActiveActivity extends ActionBarAppActivity {

	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
				
		//wakeLock akzeptieren oder löschen
		if (preferences.getBoolean("wakeLock", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);						
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);			
		}		
	}		
}

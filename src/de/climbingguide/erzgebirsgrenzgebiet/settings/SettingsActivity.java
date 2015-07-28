package de.climbingguide.erzgebirsgrenzgebiet.settings;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.Downloader;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
import de.climbingguide.erzgebirsgrenzgebiet.maps.LiveKarteActivity;
import de.climbingguide.erzgebirsgrenzgebiet.statistik.StatistikActivity;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheActivity;

//public class SettingsActivity extends ActionBarActivity {

 
/**
 * Activity to edit the application preferences.
 */
public class SettingsActivity extends PreferenceActivity implements Downloader {
	
	Preference fullscreenPref;
	Preference offlinemapPref;
	Context thisContext;
	Activity thisActivity;
	Downloader thisDownloader;
	public AlertDialog.Builder builder;
	File file;
		
	//Filedownload Zeug
	public DownloadHandler activityHandler;
	private DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }
	public DownloadHandler getDownloadHandler() { return activityHandler; }

	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		thisContext=this;
		thisActivity=this;
		thisDownloader=this;
		builder = new AlertDialog.Builder(this);
		file = new File(Environment.getExternalStorageDirectory().getPath() + "/Elbi/sachsen.map");

		
		addPreferencesFromResource(R.xml.preferences);
		
		//Bei auf Vollbildschirm klicken Vollbildschirm anzeigen
		fullscreenPref = (Preference) findPreference("fullscreen");
		fullscreenPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		             public boolean onPreferenceClick(Preference preference) {
		            	 checkFullScreen();
		            	 return true;
		             }
		         });
		offlinemapPref = (Preference) findPreference("mapGenerator");
		offlinemapPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
		             public boolean onPreferenceChange (Preference preference, Object newValue) {	
		            	 String str = (String) newValue;
		     			 if (KleFuContract.isOfflineRenderer(str)) {
		            		 if (!file.exists()) {
		            			 	// Dialog anzeigen, ob Karte heruntergeladen werden soll
	                        		builder.setTitle(thisActivity.getString(R.string.download_kartendownload));
	                        		String messageString=thisActivity.getString(R.string.download_kartendownload_desc);
	                        		builder.setMessage(Html.fromHtml(messageString));
	                        		builder.setPositiveButton(R.string.ok,
	                        			new DialogInterface.OnClickListener() {
	                    					@Override
	                    					public void onClick(DialogInterface dialog, int which) {
	                    						dialog.dismiss();
	                    						//Download starten
	                    						activityHandler = new DownloadHandler(thisActivity, thisDownloader, downloaderThread);	        
	        		            		    	downloaderThread = new DownloaderThread(thisActivity, thisDownloader);
	        		            		        downloaderThread.start();	                    						
	                    					}
	                        			}
	                    			);
	                        		builder.setNegativeButton(R.string.cancel,
		                        			new DialogInterface.OnClickListener() {
		                    					@Override
		                    					public void onClick(DialogInterface dialog, int which) {
		                   		            	 	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);		                   		            	 	
		                   		            	 	Editor editor = preferences.edit();
		                   		            	 	editor.putString("mapGenerator", getString(R.string.preferences_map_generator_default));
		                   		            	 	editor.commit();
		                   		     			 	dialog.dismiss();
		                    					}	
		                    				}	                        		
	                        		);
	                        		builder.show();		            			 
		            		 }
		            	 }		     			 
		            	 return true;
		             }
		         });
		Intent intent = getIntent();
		if (intent.getBooleanExtra(KleFuEntry.INTENT_EXTRA_CLOSE, false)) {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkFullScreen();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_map:
			openLiveKarte();
			return true;
		case R.id.action_statistik:
			openStatistik();
			return true;
		case R.id.own_list:
			openOwnList();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void checkFullScreen() {
		// check if the full screen mode should be activated
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}
	
	public void openSearch() {
		Intent intent = new Intent(this, SucheActivity.class);
		startActivity(intent);
	}
	
	public void openLiveKarte() {
		Intent intent = new Intent(this, LiveKarteActivity.class);
		startActivity(intent);
	}
	public void openStatistik() {
		Intent intent = new Intent(this, StatistikActivity.class);
		startActivity(intent);
	}
	/* 
	 * öffnet die eigene Wegeliste
	 */

	public void openOwnList() {
		Intent intent = new Intent(this, de.climbingguide.erzgebirsgrenzgebiet.ownlist.ListActivity.class);
		startActivity(intent);
	}
}
//}

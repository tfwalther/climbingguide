package de.climbingguide.erzgebirsgrenzgebiet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.Downloader;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
import de.climbingguide.erzgebirsgrenzgebiet.maps.LiveKarteActivity;
import de.climbingguide.erzgebirsgrenzgebiet.settings.SettingsActivity;
import de.climbingguide.erzgebirsgrenzgebiet.statistik.StatistikActivity;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheActivity;

public class ActionBarAppActivity extends ActionBarActivity implements Downloader {

	private static ActionBarAppActivity instance;
	
	//Filedownload Zeug
	protected DownloadHandler activityHandler;
	public DownloadHandler getDownloadHandler() { return activityHandler; }	
	protected DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }	
	
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
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_statistik:
			openStatistik();
			return true;
		case R.id.action_map:
			openLiveKarte();
			return true;
		case R.id.own_list:
			openOwnList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	

@Override
public void onResume() {
	super.onResume();
	// set right activity context
	setInstance(this);
	// check if the full screen mode should be activated
	if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullscreen", false)) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	} else {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	}

}

/* 
 * öffnet den Suche Screen
 */

	public void openSearch(View view) {
		openSearch();
	}

	public void openSearch() {
		Intent intent = new Intent(this, SucheActivity.class);
		startActivity(intent);
	}

	/* 
	 * öffnet die eigene Wegeliste
	 */

		public void openOwnList(View view) {
			openOwnList();
		}

		public void openOwnList() {
			Intent intent = new Intent(this, de.climbingguide.erzgebirsgrenzgebiet.ownlist.ListActivity.class);
			startActivity(intent);
		}	
	
	/* 
	 * öffnet den Settings Screen	
	 */
	public void openSettings(View view) {
		openSettings();
	}		
	
	public void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void openSettingsForClose() {
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra(KleFuEntry.INTENT_EXTRA_CLOSE, true);
		startActivity(intent);
	}
	
	private void openStatistik() {
		Intent intent = new Intent(this, StatistikActivity.class);
		startActivity(intent);
	}
	
	// Wird benötigt um Live-Karte zu öffnen	
	public static AlertDialog.Builder builder;	
	
	public void openLiveKarte() {
		if (KleFuContract.mapFileExists()) {
			Intent intent = new Intent(this, LiveKarteActivity.class);
			startActivity(intent);			
		} else {
			karteHerunterladenClick();
		}		
	}	

	
	public void karteHerunterladenClick() {
    	builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.download_kartendownload));
		String messageString=getString(R.string.download_kartendownload_desc);
		builder.setMessage(Html.fromHtml(messageString));
		builder.setIcon(R.drawable.ic_action_questionmark);
		builder.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					karteHerunterladen(); 
				}
			}
		);
		builder.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
    		}
		);
		builder.show();
	}
	
	public void karteHerunterladen() {
        activityHandler = new DownloadHandler(this, this, downloaderThread);	        
    	downloaderThread = new DownloaderThread(this, this);
        downloaderThread.start();
	}

	public static ActionBarAppActivity getInstance() {
		return instance;
	}

	public static void setInstance(ActionBarAppActivity instance) {
		ActionBarAppActivity.instance = instance;
	}
}

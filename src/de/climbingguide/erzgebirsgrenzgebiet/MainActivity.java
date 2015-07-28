package de.climbingguide.erzgebirsgrenzgebiet;

import java.io.File;

import net.sqlcipher.Cursor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.db.AsyncTaskGetReadableDatabase;
import de.climbingguide.erzgebirsgrenzgebiet.db.DBZugriff;
import de.climbingguide.erzgebirsgrenzgebiet.statistik.StatistikActivity;
import de.climbingguide.erzgebirsgrenzgebiet.suche.Suchanfrage;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheHandler;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheThread;
import de.climbingguide.erzgebirsgrenzgebiet.suche.Sucher;

public class MainActivity extends ActionBarAppActivity implements Sucher {	
		
	//Filedownload Zeug
//	@Override
//	public DBWriteThread getDBThread() { return sucheThread; }

	//Suche Zeug
	private Suchanfrage suchanfrage;

	public SucheHandler sucheHandler;
	@Override 
	public SucheHandler getSucheHandler() { return sucheHandler; }	
	private SucheThread sucheThread;
	@Override
	public SucheThread getSucheThread() { return sucheThread; }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    //Datenbankzugriff
	    KleFuEntry.Zugriff = new DBZugriff(this);
	    getReadableDatabase();
	    
	    //Schutz, falls vom user Kartendatei gelöscht worden ist auf Online - Karte umschalten
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		Editor editor = preferences.edit();

		// Beim ersten Mal nach Download der Offline-Karte fragen
		if (!KleFuContract.mapFileExists()) {
			if (!preferences.getBoolean("firstTime", false)) {
				karteHerunterladenClick();
			} else {
				if (preferences.getString("mapGenerator", "").equals("DATABASE_RENDERER")) {
					editor.putString("mapGenerator", getString(R.string.preferences_map_generator_default));
				}
			}
		} else {
			// anderer Schriftzug auf Offline-Map-Knopf
			Button buttonMapDownload = (Button) findViewById(R.id.buttonKarteDownload);
			buttonMapDownload.setText(R.string.offline_karte_updaten);
		}
		if (!preferences.getBoolean("firstTime", false)) {
			openSettingsForClose();
		}
		// Nach erstem App-Start Marker setzen
		editor.putBoolean("firstTime", true);
		editor.commit();		
	}

	private void getReadableDatabase() {
		if (KleFuEntry.db == null) {
			AsyncTaskGetReadableDatabase asyncTaskGetReadableDatabase = new AsyncTaskGetReadableDatabase(this);
			asyncTaskGetReadableDatabase.execute(null, null, null);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		File file = new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + KleFuEntry.DOWNLOAD_LOCAL_MAP_NAME);
		if (file.exists()) {
			// anderer Schriftzug auf Offline-Map-Knopf
			Button buttonMapDownload = (Button) findViewById(R.id.buttonKarteDownload);
			buttonMapDownload.setText(R.string.offline_karte_updaten);
		}
	}	
	
	public void GebietClick(View view) {
	    AutoCompleteTextView eingabe = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewGebiet);
	    eingabe.showDropDown();
	}
	
	
	/*
	 * styled die ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Wird aufgerufen, wenn auf einen ActionBar Knopf gedrückt wird 
	 */
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
		case R.id.action_map:
			openLiveKarte();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}	
	
	public void onLiveKarteClick(View view) {
		openLiveKarte();
	}
	
	public void onStatistikClick(View view) {
		Intent intent = new Intent(this, StatistikActivity.class);
 		startActivity(intent);
	}
	
	public void karteHerunterladenClick(View view) {
		karteHerunterladenClick();
	}
	
	public void openLastSearch(View view) {
    	//Suchanfragen aus Speicher holen

	 String[] projection = { 
				KleFuEntry.COLUMN_NAME_GEBIET,//0
				KleFuEntry.COLUMN_NAME_GIPFELNUMMER_VON,//1
		        KleFuEntry.COLUMN_NAME_GIPFELNUMMER_BIS,//2
		        KleFuEntry.COLUMN_NAME_GIPFEL,//3
		        KleFuEntry.COLUMN_NAME_WEGNAME,//4
		        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_VON,//5
		        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_BIS,//6
		        KleFuEntry.COLUMN_NAME_GEKLETTERT,//7
		        KleFuEntry.COLUMN_NAME_NOCH_NICHT_GEKLETTERT//8
	 };
	Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_SUCHANFRAGEN,  // The table to query
		    projection,           // The columns to return
		    null,                                // The columns for the WHERE clause
		    null,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
	
	c.moveToLast();
	suchanfrage = new Suchanfrage(
			c.getString(0),
			c.getInt(1),
			c.getInt(2),
			c.getString(3),
			c.getString(4),
			new Schwierigkeit(c.getInt(5)),
			new Schwierigkeit(c.getInt(6)),
			c.getInt(7)>0,
			c.getInt(8)>0);
	c.close();
		
        sucheHandler = new SucheHandler(this, this, sucheThread);	        
    	sucheThread = new SucheThread(this, this);            	
        sucheThread.start(); 
	}
	
	@Override
	public Boolean getGebietBekannt() {
		return suchanfrage.isGebietBekannt();
	}

	@Override
	public String getGebiet() {
		return suchanfrage.getGebiet();
	}

	@Override
	public String getGipfel() {
		return suchanfrage.getGipfel();
	}

	@Override
	public String getWegname() {
		return suchanfrage.getWeg();
	}

	@Override
	public Integer getGipfelnummer() {
		return suchanfrage.getGipfelnummerVon();
	}

	@Override
	public Integer getGipfelnummerBis() {
		return suchanfrage.getGipfelnummerBis();
	}

	@Override
	public Boolean getGipfelBekannt() {
		return suchanfrage.isGipfelBekannt();
	}

	@Override
	public Integer getSchwierigkeitBisInt() {
		return suchanfrage.getSchwierigkeitBis().getSchwierigkeitInt();
	}

	@Override
	public Integer getSchwierigkeitVonInt() {
		return suchanfrage.getSchwierigkeitVon().getSchwierigkeitInt();
	}

	@Override
	public Boolean isSchonGeklettert() {
		return suchanfrage.isBereitsGeklettert();
	}

	@Override
	public Boolean isNochNichtGeklettert() {
		return suchanfrage.isNochNichtGeklettert();
	}

}

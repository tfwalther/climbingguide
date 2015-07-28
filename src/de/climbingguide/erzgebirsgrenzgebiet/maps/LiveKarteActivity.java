package de.climbingguide.erzgebirsgrenzgebiet.maps;

import java.io.File;

import net.sqlcipher.Cursor;

import org.mapsforge.android.AndroidUtils;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapController;
import org.mapsforge.android.maps.MapScaleBar;
import org.mapsforge.android.maps.MapScaleBar.TextField;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorFactory;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorInternal;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.overlay.ArrayCircleOverlay;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayCircle;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.MainActivity;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.settings.SettingsActivity;
import de.climbingguide.erzgebirsgrenzgebiet.statistik.StatistikActivity;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheActivity;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheHandler;
import de.climbingguide.erzgebirsgrenzgebiet.suche.SucheThread;
import de.climbingguide.erzgebirsgrenzgebiet.suche.Sucher;

public class LiveKarteActivity extends MapActivity implements Sucher{ 
	//Filedownload Zeug
	public static SucheHandler sucheHandler;
	@Override
	public SucheHandler getSucheHandler() { return sucheHandler; }	
	private static SucheThread sucheThread;
	@Override
	public SucheThread getSucheThread() { return sucheThread; }
	
	private MapView mapView;	
	private MapGeneratorInternal mapGeneratorInternal;
	private SharedPreferences preferences;
	private static String gipfel;
	private static boolean mapWasCentered = false;
	public static void setMapWasCentered(boolean mapWasCentered) { LiveKarteActivity.mapWasCentered = mapWasCentered;  };

	// Objekte um Position anzuzeigen
	private MyLocationListener myLocationListener;
	private LocationManager locationManager;	
	private boolean showMyLocation;
	private boolean snapToLocation;		
	protected OverlayCircle overlayCircle;
	protected static OverlayItem overlayItem;
	protected ArrayItemizedOverlay itemizedOverlay;
	protected ArrayItemizedOverlay itemizedOverlayGippel;
	
	protected ArrayCircleOverlay circleOverlay;
	protected MapController mapController;
	private Paint circleOverlayFill;
	private Paint circleOverlayOutline;
	
	protected static Activity thisActivity;
	protected static Sucher thisSucher;
	
	private static String gipfelClicked;
	private static OverlayItem overlayItemClicked;
	private static GeoPoint lastPosition;

	private static class MyItemizedOverlay extends ArrayItemizedOverlay{

		/**
		 * Constructs a new MyItemizedOverlay.
		 * 
		 * @param defaultMarker
		 *            the default marker (may be null).
		 * @param context
		 *            the reference to the application context.
		 */
		MyItemizedOverlay(Drawable defaultMarker) {
			super(defaultMarker);
		}

		/**
		 * Handles a tap event on the given item.
		 */
		@Override
		protected boolean onTap(int index) {
			overlayItemClicked = createItem(index);			
			if ((overlayItemClicked != null) && !(overlayItemClicked.equals(overlayItem))) {
				gipfelClicked = overlayItemClicked.getTitle();
		        sucheHandler = new SucheHandler(thisActivity, thisSucher, sucheThread);	        
		    	sucheThread = new SucheThread(thisActivity, thisSucher);            	
		        sucheThread.start();
//		        thisActivity.finish();
			}
			return true;
		}
		
		@Override
		protected boolean onLongPress(int index) {
			OverlayItem item = createItem(index);	
			if ((item != null) && !(item.equals(overlayItem))) {
				gipfelClicked = item.getTitle();
				Gipfel gipfel = new Gipfel(gipfelClicked);
				if (gipfel.isBestiegen()) 
					gipfelClicked = "<u>" + gipfelClicked + "</u>";
				gipfelClicked = gipfel.getGipfelnummer().toString() + " - " + gipfelClicked
						+ " (" + gipfel.getGebiet() + ")";
				showToastOnUiThread(gipfelClicked);
			}
			return true;
		}
		
		void showToastOnUiThread(final String text) {

			if (AndroidUtils.currentThreadIsUiThread()) {
				Toast toast = Toast.makeText(thisActivity, text, Toast.LENGTH_LONG);
				toast.show();
			} else {
				thisActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast toast = Toast.makeText(thisActivity, Html.fromHtml(text), Toast.LENGTH_LONG);
						toast.show();
					}
				});
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		thisActivity = this;
		thisSucher = this;
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.itemizedOverlay = new MyItemizedOverlay(null);
		this.itemizedOverlayGippel = new MyItemizedOverlay(null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.live_karte, menu);
	    return super.onCreateOptionsMenu(menu);
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
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_legend:
			openLegend();
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
	
	private void openSearch() {
		Intent intent = new Intent(this, SucheActivity.class);
		startActivity(intent);
	} 

	
	/* 
	 * öffnet den Einstellungen Screen	
	 */
	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);		
	}	

	private void openStatistik() {
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

	/* 
	 * öffnet die Legende der Karte	
	 */
	private void openLegend() {
		String defaultMapGenerator=getString(R.string.preferences_map_generator_default);
		String str = preferences.getString("mapGenerator", defaultMapGenerator);
		Uri uri=null;
		String uriString=null;
    	if (str.equals("DATABASE_RENDERER_ELEVATE")) {
    		uriString = "http://www.openandromaps.org/kartenlegende/elevation-hike-theme";
    	} else if (str.equals("DATABASE_RENDERER_OPENANDROMAPS")) {
    		uriString = "http://www.openandromaps.org/kartenlegende/andromaps_hc";
    	} else if (str.equals("DATABASE_RENDERER_OPENANDROMAPS_LIGHT")) {
    		uriString = "http://www.openandromaps.org/";	
    	} else if (str.equals("DATABASE_RENDERER_OPENANDROMAPS_CYCLE")) {
    		uriString = "http://www.openandromaps.org/kartenlegende/andromaps_mtb";
    	} else if (str.equals("MAPNIK")) {
    		uriString = getOSMURL();
    	} else if (str.equals("OPENCYCLEMAP")) {
    		uriString = getOSMURL() + "&layers=C";
    	}
    	
		uri = Uri.parse(uriString);

		
		
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivityForResult(intent, KleFuEntry.ACTIVITY_LIVEMAP_INTENT_CODE);		
	}
	
	private String getOSMURL() {
		MapViewPosition mapPosition = mapView.getMapPosition();
		GeoPoint mapCenter = mapPosition.getMapCenter();
		Double latitude = mapCenter.getLatitude();
		Double longitude = mapCenter.getLongitude();
		Byte zoomLevel = mapPosition.getZoomLevel();
		
		return "http://www.openstreetmap.org/#map=" + 
				zoomLevel.toString() + "/" +
				latitude.toString() + "/" +
				longitude.toString();		
	}
	
	private void overlay() {
		try {
		String[] projection = {
			    KleFuEntry.COLUMN_NAME_GIPFEL,//0
			    KleFuEntry.COLUMN_NAME_NORTH_COORDINATE,//1
			    KleFuEntry.COLUMN_NAME_EAST_COORDINATE,//2
			    KleFuEntry.COLUMN_NAME_BESTIEGEN,//3
			    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,//4
			    KleFuEntry.COLUMN_NAME_GEBIET//5
			};
			
			// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection,                               // The columns to return
		    null,                                // The columns for the WHERE clause
		    null,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );		

		// create an individual marker for an overlay item
		Drawable itemGreenMarker = getResources().getDrawable(R.drawable.gipfelbuchoffen);
		itemGreenMarker.setAlpha(200);

		// create the default marker for overlay items
		Drawable itemRedMarker = getResources().getDrawable(R.drawable.marker_red);
		itemRedMarker.setAlpha(200);

		// create the default marker for overlay items
		Drawable itemBlueMarker = getResources().getDrawable(R.drawable.gipfelbuch);
		itemBlueMarker.setAlpha(200);		
				
		// String aus Cursor lesen
		c.moveToFirst();
	    while (c.isAfterLast() == false) {
	    	String gipfelItem = c.getString(0);
	    	String gipfelSnippet=c.getString(4)+","+c.getString(5);
	    	if (gipfelItem.equals(gipfel)) {
		    	itemizedOverlayGippel.addItem(new OverlayItem(
		    			new GeoPoint(c.getDouble(1), c.getDouble(2)),
		    			gipfelItem,
		    			gipfelSnippet,
		    			ArrayItemizedOverlay.boundCenterBottom(itemRedMarker)
		    			)
		    	);
	    	} else if (c.getInt(3)>0) {
		    	itemizedOverlayGippel.addItem(new OverlayItem(
		    			new GeoPoint(c.getDouble(1), c.getDouble(2)),
		    			gipfelItem,
		    			gipfelSnippet,
		    			ArrayItemizedOverlay.boundCenter(itemGreenMarker)
		    			)
		    );
	    	} else {
	    	itemizedOverlayGippel.addItem(new OverlayItem(
	    			new GeoPoint(c.getDouble(1), c.getDouble(2)),
	    			gipfelItem,
	    			gipfelSnippet,
	    			ArrayItemizedOverlay.boundCenter(itemBlueMarker)
	    			)
	    	);
	    	}	    	
	   	    c.moveToNext();
	    }
	    c.close();
		this.mapView.getOverlays().add(itemizedOverlayGippel);

	} catch (Exception e) {
		Toast.makeText(this, "Overlay-Fehler " + e.toString(), Toast.LENGTH_LONG).show();
	}
	}

	private void configureMapView() {
		// configure the MapView and activate the zoomLevel buttons
		this.mapView.setClickable(true);
		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setFocusable(true);

		// set the localized text fields
		MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
		mapScaleBar.setText(TextField.KILOMETER, getString(R.string.unit_symbol_kilometer));
		mapScaleBar.setText(TextField.METER, getString(R.string.unit_symbol_meter));

		// get the map controller for this MapView
		this.mapController = this.mapView.getController();
	}
	
	
	@Override
	protected void onResume() {
		try {
		super.onResume();
		
		snapToLocation=preferences.getBoolean("snapLocation", false);
		
		setContentView(R.layout.activity_live_karte);
//		mapView = new MapView(this, new org.mapsforge.android.maps.mapgenerator.tiledownloader.MapnikTileDownloader());
		mapView = (MapView) findViewById(R.id.mapView);

//		File file = new File(Environment.getExternalStorageDirectory() + "/Elbi/sachsen.map");
		File file = new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + KleFuEntry.DOWNLOAD_LOCAL_MAP_NAME);
		String defaultMapGenerator=getString(R.string.preferences_map_generator_default);
		String mapGen = preferences.getString("mapGenerator", defaultMapGenerator);
		if (file.exists() && KleFuContract.isOfflineRenderer(mapGen)) {
//			String customRenderTheme=defaultMapGenerator;
//			if (mapGen.equals("DATABASE_RENDERER_ELEVATE")) {
//				customRenderTheme = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "Elevate.xml";
//			} else if (mapGen.equals("DATABASE_RENDERER_OPENANDROMAPS")) {
//				customRenderTheme = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "sachsen.map.xml";				
//			} else if (mapGen.equals("DATABASE_RENDERER_OPENANDROMAPS_LIGHT")) {
//				customRenderTheme = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "sachsen_light.map.xml";				
//			} else if (mapGen.equals("DATABASE_RENDERER_OPENANDROMAPS_CYCLE")) {
//				customRenderTheme = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "sachsen_mtb.map.xml";				
//			} 
			mapView.setMapFile(file);
//            try {
//                mapView.setRenderTheme(new File(customRenderTheme));
//            } catch (FileNotFoundException e) {
//                Toast.makeText(
//                        this,
//                        this.getResources().getString(R.string.warn_rendertheme_missing),
//                        Toast.LENGTH_LONG)
//                        .show();
//            }
		}		
		
		//Map zentrieren auf aktuellen Gipfel und evtl. Toast zeigen
		Intent intent = getIntent();
		if (!mapWasCentered) {
			if (intent.hasExtra(KleFuEntry.COLUMN_NAME_GIPFEL)) {
				gipfel = intent.getStringExtra(KleFuEntry.COLUMN_NAME_GIPFEL);
				Gipfel gipfelObject = new Gipfel(gipfel);
				double latitude = intent.getDoubleExtra(KleFuEntry.BREITE, KleFuEntry.DEFAULT_LATITUDE);
				double longitude = intent.getDoubleExtra(KleFuEntry.HOHE, KleFuEntry.DEFAULT_LONGITUDE);
				mapView.getController().setCenter(new GeoPoint(Double.valueOf(latitude), Double.valueOf(longitude)));
				Toast.makeText(this, Html.fromHtml(getString(R.string.map_gipfel_centered_pre) + " <i>" + gipfelObject.getGipfelHtml() + "</i> " + getString(R.string.map_gipfel_centered_post)), Toast.LENGTH_LONG).show();
			} else {
				if (snapToLocation==false) {
					if (lastPosition == null) {
						mapView.getController().setCenter(new GeoPoint(KleFuEntry.DEFAULT_LATITUDE, KleFuEntry.DEFAULT_LONGITUDE));					
					} else {
						mapView.getController().setCenter( lastPosition);										
					}
				}
			}
			mapWasCentered = true;
		}
		
		configureMapView();
		
		//Zeug um aktuelle Position anzuzeigen
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.myLocationListener = new MyLocationListener(this);
	
//		mapView.setMapTheme();	
		
		// Show the Up button in the action bar.
//		setupActionBar();	
		
		//Overlay Zeug

		
		setMapRenderer();

		overlay();		
		
		// set up the paint objects for the location overlay
		this.circleOverlayFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.circleOverlayFill.setStyle(Paint.Style.FILL);
		this.circleOverlayFill.setColor(Color.BLUE);
		this.circleOverlayFill.setAlpha(48);

		this.circleOverlayOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.circleOverlayOutline.setStyle(Paint.Style.STROKE);
		this.circleOverlayOutline.setColor(Color.BLUE);
		this.circleOverlayOutline.setAlpha(128);
		this.circleOverlayOutline.setStrokeWidth(2);		
		
		//showLocation evtl. aktivieren
		if (preferences.getBoolean("showLocation", true)) {
			enableShowMyLocation(false);
			if (preferences.getBoolean("snapLocation", false)) {
				enableSnapToLocation(false);
			}
		} else { 
			disableShowMyLocation();
			disableSnapToLocation(false);
		}
		
		//Kartenskalierungseinstellungen
		MapScaleBar mapScaleBar = this.mapView.getMapScaleBar();
		mapScaleBar.setShowMapScaleBar(preferences.getBoolean("showScaleBar", false));
		String scaleBarUnitDefault = getString(R.string.preferences_scale_bar_unit_default);
		String scaleBarUnit = preferences.getString("scaleBarUnit", scaleBarUnitDefault);
		mapScaleBar.setImperialUnits(scaleBarUnit.equals("imperial"));

		//Schriftgröße von Kartenbeschriftungen ändern
		try {
			String textScaleDefault = getString(R.string.preferences_text_scale_default);
			this.mapView.setTextScale(Float.parseFloat(preferences.getString("textScale", textScaleDefault)));
		} catch (NumberFormatException e) {
			this.mapView.setTextScale(1);
		}		
		
		// check if the full screen mode should be activated	
		if (preferences.getBoolean("fullscreen", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		
		//wakeLock akzeptieren
		if (preferences.getBoolean("wakeLock", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);						
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);			
		}
		
		//Speichereinstellungen der Karte
		boolean persistent = preferences.getBoolean("cachePersistence", true);
		int capacity = Math.min(preferences.getInt("cacheSize", KleFuEntry.FILE_SYSTEM_CACHE_SIZE_DEFAULT),
				KleFuEntry.FILE_SYSTEM_CACHE_SIZE_MAX);
		TileCache fileSystemTileCache = this.mapView.getFileSystemTileCache();
		fileSystemTileCache.setPersistent(persistent);
		fileSystemTileCache.setCapacity(capacity);
		
		// evtl. aktuelle Position anzeigen
		showMyLocation=preferences.getBoolean("showLocation", false);
		if(showMyLocation) enableShowMyLocation(true);
		else disableShowMyLocation();
		
		} catch (Exception e) {
			openMain();
			finish();
		}
		
	}

	private void setMapRenderer() {
		//Einstellung, welche Karte verwendet werden soll
		String name = preferences.getString("mapGenerator", getString(R.string.preferences_map_generator_default));
			MapGeneratorInternal mapGeneratorInternalNew;
			try {
				if (KleFuContract.isOfflineRenderer(name)) {
					mapGeneratorInternalNew = MapGeneratorInternal.DATABASE_RENDERER;
				} else {
					mapGeneratorInternalNew = MapGeneratorInternal.valueOf(name);
				}
			} catch (IllegalArgumentException e) {
				mapGeneratorInternalNew = MapGeneratorInternal.OPENCYCLEMAP;
			}


			if (mapGeneratorInternalNew != this.mapGeneratorInternal) {
				MapGenerator mapGenerator = MapGeneratorFactory.createMapGenerator(mapGeneratorInternalNew);
				this.mapView.setMapGenerator(mapGenerator);
				this.mapGeneratorInternal = mapGeneratorInternalNew;
			}
	}
	
	@Override
	protected void onDestroy() {
		try {			
			super.onDestroy();
		} catch (Exception e) {
			super.onDestroy();
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		try {
			lastPosition = mapView.getMapPosition().getMapCenter();
			disableShowMyLocation();
/*			this.mapView.getOverlays().clear();
			this.mapView.getOverlays().remove(this.itemizedOverlayBlue);
			this.mapView.getOverlays().remove(this.itemizedOverlayGreen);
			this.mapView.getOverlays().remove(this.itemizedOverlayRed);
			itemizedOverlayBlue.clear();
			itemizedOverlayGreen.clear();
			itemizedOverlayRed.clear(); */
			super.onPause();
			finish();  
		} catch (Exception e) {
			super.onPause();
			finish();
		}
	}
	
	
	boolean isShowMyLocationEnabled() {
		return preferences.getBoolean("showLocation", false);
	}
	
	/**
	 * Returns the status of the "snap to location" mode.
	 * 
	 * @return true if the "snap to location" mode is enabled, false otherwise.
	 */
	boolean isSnapToLocationEnabled() {
		return preferences.getBoolean("snapLocation", false);
	}
	
	/**
	 * Shows myLocation
	 * 
	 * @centerAtFirstFix true if map should center at first fix.
	 */
	@SuppressWarnings("static-access")
	private void enableShowMyLocation(boolean centerAtFirstFix) {
		try {
		if (!this.showMyLocation) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String bestProvider = this.locationManager.getBestProvider(criteria, true);
			if (bestProvider == null) {
				Toast.makeText(this, getString(R.string.kein_gps_aktiviert), Toast.LENGTH_LONG).show();
				return;
			}

			this.showMyLocation = true;

			this.circleOverlay = new ArrayCircleOverlay(this.circleOverlayFill, this.circleOverlayOutline);
			this.overlayCircle = new OverlayCircle();
			this.circleOverlay.addCircle(this.overlayCircle);
			this.mapView.getOverlays().add(this.circleOverlay);

			this.overlayItem = new OverlayItem();
			this.overlayItem.setMarker(ItemizedOverlay.boundCenter(getResources().getDrawable(R.drawable.my_location)));
			itemizedOverlay.addItem(this.overlayItem);
			this.mapView.getOverlays().add(itemizedOverlay);

			if (gipfel == null) {
				this.myLocationListener.setCenterAtFirstFix(centerAtFirstFix);
			}
			this.locationManager.requestLocationUpdates(bestProvider, 1000, 0, this.myLocationListener);
//			this.snapToLocationView.setVisibility(View.VISIBLE);
		}
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.kein_gps_aktiviert), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Disables the "show my location" mode.
	 */
	void disableShowMyLocation() {
		try {
		if (this.showMyLocation) {
			this.showMyLocation = false;
			disableSnapToLocation(false);
			this.locationManager.removeUpdates(this.myLocationListener);
			this.mapView.getOverlays().remove(this.circleOverlay);
			this.mapView.getOverlays().remove(this.itemizedOverlay);
			circleOverlay.clear();
			itemizedOverlay.clear();
//			this.snapToLocationView.setVisibility(View.GONE);
		}
		} catch (Exception e) {
			Log.v("de.climbingguide.erzgebirsgrenzgebiet", e.toString());
		}
	}

	/**
	 * Enables the "snap to location" mode.
	 * 
	 * @param showToast
	 *            defines whether a toast message is displayed or not.
	 */
	void enableSnapToLocation(boolean showToast) {
		if (!this.snapToLocation) {
			this.snapToLocation = true;
			this.mapView.setClickable(false);
			if (showToast) {
				showToastOnUiThread(getString(R.string.snap_to_location_enabled));
			}
		}
	}	
	
	/**
	 * Disables the "snap to location" mode.
	 * 
	 * @param showToast
	 *            defines whether a toast message is displayed or not.
	 */
	void disableSnapToLocation(boolean showToast) {
		if (this.snapToLocation) {
			this.snapToLocation = false;
//			this.snapToLocationView.setChecked(false);
			this.mapView.setClickable(true);
			if (showToast) {
				showToastOnUiThread(getString(R.string.snap_to_location_disabled));
			}
		}
	}

	/**
	 * Centers the map to the last known position as reported by the most accurate location provider. If the last
	 * location is unknown, a toast message is displayed instead.
	 */
//	private void gotoLastKnownPosition() {
//		Location currentLocation;
//		Location bestLocation = null;
//		for (String provider : this.locationManager.getProviders(true)) {
//			currentLocation = this.locationManager.getLastKnownLocation(provider);
//			if (bestLocation == null || currentLocation.getAccuracy() < bestLocation.getAccuracy()) {
//				bestLocation = currentLocation;
//			}
//		}
//
//		// check if a location has been found
//		if (bestLocation != null) {
//			GeoPoint point = new GeoPoint(bestLocation.getLatitude(), bestLocation.getLongitude());
//			this.mapController.setCenter(point);
//		} else {
//			showToastOnUiThread(getString(R.string.error_last_location_unknown));
//		}
//	}

	/**
	 * Uses the UI thread to display the given text message as toast notification.
	 * 
	 * @param text
	 *            the text message to display
	 */
	void showToastOnUiThread(final String text) {

		if (AndroidUtils.currentThreadIsUiThread()) {
			Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.show();
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast toast = Toast.makeText(LiveKarteActivity.this, text, Toast.LENGTH_LONG);
					toast.show();
				}
			});
		}
	}	
	
	@Override
	public Boolean getGebietBekannt() {
		return true;
	}

	@Override
	public String getGebiet() {
		String item[]=overlayItemClicked.getSnippet().split(",");
		return item[1];
	}

	@Override
	public String getGipfel() {
		return gipfelClicked;
	}

	@Override
	public Integer getGipfelnummer() {
		String item[]=overlayItemClicked.getSnippet().split(",");
		return Integer.parseInt(item[0]);
	}

	@Override
	public Integer getGipfelnummerBis() {
		return 0;
	}

	@Override
	public Boolean getGipfelBekannt() {
		return true;
	}

	@Override
	public Integer getSchwierigkeitBisInt() {
		return 0;
	}

	@Override
	public Integer getSchwierigkeitVonInt() {
		return 0;
	}
	
	private void openMain() {
		Intent intent = new Intent (this, MainActivity.class);		 
		startActivity(intent);
		finish();
	}

	@Override
	public Boolean isSchonGeklettert() {
		return true;
	}

	@Override
	public Boolean isNochNichtGeklettert() {
		return true;
	}

	@Override
	public String getWegname() {
		return "";
	}
}
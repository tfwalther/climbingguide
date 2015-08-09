package de.climbingguide.erzgebirsgrenzgebiet.maps;

import java.io.File;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapDataStore;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class LiveKarteActivity extends Activity {

	private SharedPreferences preferences;
//	private boolean snapToLocation;
	private static String gipfel;
	private static boolean mapWasCentered = false;
	public static void setMapWasCentered(boolean mapWasCentered) { LiveKarteActivity.mapWasCentered = mapWasCentered;  };
	
	private static LatLong lastPosition;	
	
    // name of the map file in the external storage
    private static final String MAPFILE = "/Elbi/sachsen.map";

    private MapView mapView;    
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(this.getApplication());

        this.mapView = new MapView(this);
        setContentView(this.mapView);

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f, 
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 12);

        // tile renderer layer using internal render theme
        MapDataStore mapDataStore = new MapFile(getMapFile());
        this.tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                this.mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
        
        centerCard();
    }
    
//    @Override
//    protected void onResume() {
////        super.onResume();
////        snapToLocation=preferences.getBoolean("snapLocation", false);
////    	snapToLocation = false;
//        centerCard();
//    }
    
//	@Override
//	protected void onPause() {
//		try {
//			lastPosition = this.mapView.getModel().mapViewPosition.getCenter();
////			disableShowMyLocation();
///*			this.mapView.getOverlays().clear();
//			this.mapView.getOverlays().remove(this.itemizedOverlayBlue);
//			this.mapView.getOverlays().remove(this.itemizedOverlayGreen);
//			this.mapView.getOverlays().remove(this.itemizedOverlayRed);
//			itemizedOverlayBlue.clear();
//			itemizedOverlayGreen.clear();
//			itemizedOverlayRed.clear(); */
//			super.onPause();
////			finish();  
//		} catch (Exception e) {
//			super.onPause();
////			finish();
//		}
//	}    
    
    protected void centerCard() {
		//center map on actual position and show toast
		Intent intent = getIntent();
		if (!mapWasCentered) {
			if (intent.hasExtra(KleFuEntry.COLUMN_NAME_GIPFEL)) {
				gipfel = intent.getStringExtra(KleFuEntry.COLUMN_NAME_GIPFEL);
				Gipfel gipfelObject = new Gipfel(gipfel);
				double latitude = intent.getDoubleExtra(KleFuEntry.BREITE, KleFuEntry.DEFAULT_LATITUDE);
				double longitude = intent.getDoubleExtra(KleFuEntry.HOHE, KleFuEntry.DEFAULT_LONGITUDE);
				this.mapView.getModel().mapViewPosition.setCenter(new LatLong(Double.valueOf(latitude), Double.valueOf(longitude)));
				Toast.makeText(this, Html.fromHtml(getString(R.string.map_gipfel_centered_pre) + " <i>" + gipfelObject.getGipfelHtml() + "</i> " + getString(R.string.map_gipfel_centered_post)), Toast.LENGTH_LONG).show();
			} else {
//				if (snapToLocation==false) {
				if (true) {
					if (lastPosition == null) {
				        this.mapView.getModel().mapViewPosition.setCenter(new LatLong(KleFuEntry.DEFAULT_LATITUDE, KleFuEntry.DEFAULT_LONGITUDE));					
					} else {
						this.mapView.getModel().mapViewPosition.setCenter(lastPosition);										
					}
				}
			}
			mapWasCentered = true;
		}
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mapView.destroy();
    }

    private File getMapFile() {
        File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
        return file;
    }

}
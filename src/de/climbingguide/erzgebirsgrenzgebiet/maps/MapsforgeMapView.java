package de.climbingguide.erzgebirsgrenzgebiet.maps;

import java.io.File;
import java.io.FileNotFoundException;

import org.mapsforge.android.maps.MapView;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class MapsforgeMapView extends MapView {

    public MapsforgeMapView(Context context) {
        super(context);
    }	
	
    public MapsforgeMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setMapTheme() {
        String customRenderTheme = Environment.getExternalStorageDirectory().getPath() + "/Elbi/andromaps_hc.xml";
//        if (!StringUtils.isEmpty(customRenderTheme)) {
            try {
                setRenderTheme(new File(customRenderTheme));
            } catch (FileNotFoundException e) {
                Toast.makeText(
                        getContext(),
                        getContext().getResources().getString(R.string.warn_rendertheme_missing),
                        Toast.LENGTH_LONG)
                        .show();
            }
//        } else {
//            setRenderTheme(DEFAULT_RENDER_THEME);
//        }
    }
}

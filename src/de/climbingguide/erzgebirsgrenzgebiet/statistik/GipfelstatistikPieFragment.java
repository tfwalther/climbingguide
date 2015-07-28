package de.climbingguide.erzgebirsgrenzgebiet.statistik;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.Cursor;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class GipfelstatistikPieFragment extends Fragment {

	public GipfelstatistikPieFragment () {}	
	
	private GraphicalView mChartView;
	private CategorySeries serie;
	
	private List<String> gebiete;
	private View rootView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_gipfelkuchen, container, false);	
		
		// Diagramm-Zeuch
//		serie = new CategorySeries(getString(R.string.anzahl_bestiegen));
//		serie.add("Fehlen", 12);
//		serie.add("Bestiegen", 8);
//	    int[] colors = new int[] { Color.BLUE, Color.GREEN };
		init();						
		refresh();	   
	    
	    Spinner spinnerGebiete = (Spinner) rootView.findViewById(R.id.spinner_gebiete);
	    
	    setGebiete();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_item, gebiete);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGebiete.setAdapter(adapter);
	    spinnerGebiete.setOnItemSelectedListener(new OnItemSelectedListener() {

	        @Override
	        public void onItemSelected(AdapterView<?> parent, View arg1,
	                int arg2, long arg3) {

	            //do some work, update UI or reset qty and discount spinner or whatever
//	            strItem = spnSelectItem.getItemAtPosition(arg2).toString();
	        	
//	            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
	            ((TextView) parent.getChildAt(0)).setTextSize(25);
	            
	        	init();
	        	refresh();
	        }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
	    });
	    
	    return rootView;		
	}
	
	private void setGebiete() {
	// Define a projection that specifies which columns from the database
	// you will actually use after this query.
	String[] projection = {
	    KleFuEntry.COLUMN_NAME_GEBIET
	};
	
	// Gebiete aus Datenbank lesen
	Cursor c = KleFuEntry.db.query(
	    KleFuEntry.TABLE_NAME_GEBIETE,  // The table to query
	    projection,                               // The columns to return
	    null,                                // The columns for the WHERE clause
	    null,                            // The values for the WHERE clause
	    null,                                     // don't group the rows
	    null,                                     // don't filter by row groups
	    null                                 // The sort order
	    );
	
	gebiete = new ArrayList<String>();
	gebiete.add(getString(R.string.alle_gebiete));
	int cursorLaenge=c.getCount();
	if (!(cursorLaenge<1)) {
		// String aus Cursor lesen
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			gebiete.add(c.getString(0));
			c.moveToNext();
		}
	}
    c.close();
}
    
    @Override
    public void onResume() {
    	super.onResume();
		init();						
		refresh();	    	
    }    
    
	public void init(){
		serie = new CategorySeries(getString(R.string.anzahl));
		
		//Anzahl bestiegene Gipfel ermitteln
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
				
		String[] projection = {
		    KleFuEntry.COLUMN_NAME_VORSTIEG
		};

		// String für die Where Clause
		String whereClause =
			    KleFuEntry.COLUMN_NAME_BESTIEGEN + " LIKE ?";
		// String für die Where Options
		// String für die Where Options
		ArrayList<String> arrayListWhereOptions=new ArrayList<String>();

		arrayListWhereOptions.add("1");
		
		String spinnerGebiet;
		try {		
			Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_gebiete);
			spinnerGebiet = spinner.getSelectedItem().toString();
			if (!spinnerGebiet.equals(getString(R.string.alle_gebiete))) {
				whereClause = whereClause + " AND " + KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ?";
				arrayListWhereOptions.add(spinnerGebiet);
			} else {
				spinnerGebiet = KleFuContract.ANZAHL_ALLE_GIPFEL;
			}
		} catch (Exception e) {
			spinnerGebiet = KleFuContract.ANZAHL_ALLE_GIPFEL;
		}

		String[] whereOptions = new String[arrayListWhereOptions.size()];
		arrayListWhereOptions.toArray(whereOptions);

		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection,                               // The columns to return
		    whereClause,                              // The columns for the WHERE clause
		    whereOptions,                             // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
		c.moveToFirst();
		int anzahlVorgestiegen=0;
		while (!c.isAfterLast()) {
			if (c.getInt(0)>0) anzahlVorgestiegen++;
			c.moveToNext();
		}
		double cursorLaenge=c.getCount();
	    c.close();
		
	    TextView prozentTextView = (TextView) rootView.findViewById(R.id.textViewProzent);
	    
	    Double prozentD = (cursorLaenge/KleFuContract.getMaxGipfel(spinnerGebiet))*100;
	    Integer prozent = prozentD.intValue();
	    prozentTextView.setText(prozent.toString() + " % " + getString(R.string.bestiegene));
	    
		serie.add(getActivity().getString(R.string.nicht_bestiegen), KleFuContract.getMaxGipfel(spinnerGebiet)-cursorLaenge);
		serie.add(getActivity().getString(R.string.bestiegene)+" "+getActivity().getString(R.string.im_nachstieg), cursorLaenge-anzahlVorgestiegen);
		serie.add(getActivity().getString(R.string.bestiegene)+" "+getActivity().getString(R.string.im_vorstieg), anzahlVorgestiegen);
		
		
	    int[] colors = new int[] { getResources().getColor(R.color.lightblue), getResources().getColor(R.color.orange), getResources().getColor(R.color.green) };
	    DefaultRenderer renderer = new DefaultRenderer();
	    renderer.setLabelsTextSize(25);
	    renderer.setLegendTextSize(25);
	    renderer.setMargins(new int[] { 20, 30, 15, 0 });
	    for (int color : colors) {
	      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      r.setColor(color);
	      renderer.addSeriesRenderer(r);
	    }
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setZoomButtonsVisible(false);
	    renderer.setZoomEnabled(true);
	    renderer.setChartTitleTextSize(20);
	    renderer.setDisplayValues(true);
	    renderer.setShowLabels(false);
	    renderer.setPanEnabled(false);
//	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
//	    r.setGradientEnabled(true);
//	    r.setGradientStart(0, Color.BLUE);
//	    r.setGradientStop(0, Color.GREEN);
//	    r.setHighlighted(true);		
		
	    mChartView = ChartFactory.getPieChartView(getActivity(), serie, renderer);	     	   
	 }
	
	public void refresh() {
		mChartView.repaint();
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout_chart);
		layout.removeAllViews();
	    layout.addView(getMChartView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT));
	 }
	
	public GraphicalView getMChartView(){
	     return mChartView;
	}
}

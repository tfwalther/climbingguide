package de.climbingguide.erzgebirsgrenzgebiet.statistik;

import net.sqlcipher.Cursor;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class SchwierigkeitsstatistikFragment extends Fragment implements ZoomListener {

	public SchwierigkeitsstatistikFragment () {}	
	
	private XYMultipleSeriesDataset mDataset;
    private XYMultipleSeriesRenderer mRenderer;
	private GraphicalView mChartView;
	private XYSeries serieNachstieg;	
	private XYSeries serieVorstieg;	
	private XYSeries serieRP;
	private XYSeries serieAnnotations;
	private XYSeriesRenderer rendererRP;
	private XYSeriesRenderer rendererVorstieg;
	private XYSeriesRenderer rendererNachstieg;
	
	private final float TEXTSIZE=30;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_schwierigkeitsstatistik, container, false);	
		
		// Diagramm-Zeuch
 		serieNachstieg = new XYSeries(getString(R.string.anzahl_nachgestiegen));
		serieVorstieg = new XYSeries(getString(R.string.anzahl_vorgestiegen));
		serieRP = new XYSeries(getString(R.string.anzahl_RP));
		serieAnnotations = new XYSeries("");
		
		mDataset= new XYMultipleSeriesDataset();
		mRenderer=new XYMultipleSeriesRenderer();
		init();						
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout_chart);
	    layout.addView(getMChartView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT));
	    
	    return rootView;		
	}
    
    @Override
    public void onResume() {
    	super.onResume();
		addPoints();
		refresh();		    	
    }
	
	public void init(){
	     mRenderer.setApplyBackgroundColor(true);
	     mRenderer.setBackgroundColor(getResources().getColor(R.color.white));
	     mRenderer.setXLabelsColor( getResources().getColor(R.color.black));
	     mRenderer.setYLabelsColor(0, getResources().getColor(R.color.black));
	     mRenderer.setMarginsColor(getResources().getColor(R.color.white));
	     mRenderer.setGridColor(getResources().getColor(R.color.white));
	     mRenderer.setAxisTitleTextSize(16);
	     mRenderer.setChartTitleTextSize(20);
	     mRenderer.setLabelsTextSize(25);
	     mRenderer.setLegendTextSize(25);
	     mRenderer.setMargins(new int[] { 20, 80, 50, 0 });
	     mRenderer.setZoomButtonsVisible(true);
	     mRenderer.setYAxisMin(0);
	     mRenderer.setXTitle(getString(R.string.schwierigkeit));
	     mRenderer.setLabelsColor(getResources().getColor(R.color.black));
	     mRenderer.setAxesColor(getResources().getColor(R.color.black));
	     mRenderer.setYTitle(getString(R.string.anzahl));
	     mRenderer.setYLabelsAlign(Align.RIGHT, 0);
	     mRenderer.setAxisTitleTextSize(25);
	     mRenderer.setShowAxes(true);	     
	     mRenderer.setBarSpacing(2f);
  		 mRenderer.setXAxisMin(0);
	     mRenderer.setPanEnabled(true);
  		 mRenderer.setYLabelsPadding(8);
	     
//	     mRenderer.setBarSpacing(1);
	     
	     //XYSeriesRenderer renderer = new XYSeriesRenderer();
	     rendererRP = new XYSeriesRenderer();
	     rendererRP.setColor(getResources().getColor(R.color.green));
//	     mRenderer.setYLabels(0);
	     rendererRP.setAnnotationsColor(getResources().getColor(R.color.black));
	     rendererVorstieg = new XYSeriesRenderer();
	     rendererVorstieg.setColor(getResources().getColor(R.color.orange));
	     rendererVorstieg.setAnnotationsTextAlign(Align.CENTER);
	     rendererVorstieg.setAnnotationsTextSize(TEXTSIZE);
	     rendererVorstieg.setAnnotationsColor(getResources().getColor(R.color.lemonchiffon));
	     rendererNachstieg = new XYSeriesRenderer();
	     rendererNachstieg.setAnnotationsTextAlign(Align.CENTER);
	     rendererNachstieg.setAnnotationsTextSize(TEXTSIZE);
	     rendererNachstieg.setAnnotationsColor(Color.BLACK);
	     rendererNachstieg.setColor(getResources().getColor(R.color.lightblue));
	     XYSeriesRenderer rendererLastAnnotations = new XYSeriesRenderer();
	     rendererLastAnnotations.setColor(Color.TRANSPARENT);
	     rendererLastAnnotations.setAnnotationsColor(getResources().getColor(R.color.black));
	     rendererLastAnnotations.setAnnotationsTextAlign(Align.CENTER);
	     rendererLastAnnotations.setAnnotationsTextSize(TEXTSIZE);
	     rendererLastAnnotations.setShowLegendItem(false);
	    // mRenderer.addSeriesRenderer(renderer);
	     mRenderer.addSeriesRenderer(rendererRP);
	     mRenderer.addSeriesRenderer(rendererVorstieg);
	     mRenderer.addSeriesRenderer(rendererNachstieg);
	     mRenderer.addSeriesRenderer(rendererLastAnnotations);
	     
//	     mDataset.addSeries(serie);
	     mDataset.addSeries(serieRP);	     
	     mDataset.addSeries(serieVorstieg);
	     mDataset.addSeries(serieNachstieg);
	     mDataset.addSeries(serieAnnotations);
	     mChartView = ChartFactory.getBarChartView(getActivity(), mDataset, mRenderer, 
	    		 BarChart.Type.STACKED); 
	     mChartView.addZoomListener(this, true, true);
	 }

	private void setXTextLabels() {			
		mRenderer.setXLabels(0);	
		mRenderer.clearXTextLabels();
		
		double xAxisMin = mRenderer.getXAxisMin();
		double xAxisMax = mRenderer.getXAxisMax();
		int maxXLabelAllowed;
		switch (getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			maxXLabelAllowed=15;
			break;
		default://Configuration.ORIENTATION_PORTRAIT:
			maxXLabelAllowed=8;
			break;
		} 
		
		int count = ((int)(xAxisMax-xAxisMin)/maxXLabelAllowed)+1;
		
		for (int i=11; i<=KleFuEntry.MAXSCHWIERIGKEIT; i=i+count) {
		    mRenderer.addXTextLabel(i, Schwierigkeit.SchwierigkeitIntToString(i));
		}
		for (int i=1; i<5; i=i+count) {
		    mRenderer.addXTextLabel(i+5, Schwierigkeit.SchwierigkeitIntToString(i));
		}
	}
	
	public void refresh() {
		 mChartView.repaint();
	 }

	public void addPoints(){
		
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF,//0
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP,//1
		    KleFuEntry.COLUMN_NAME_VORSTIEG,//2
		    KleFuEntry.COLUMN_NAME_RP,//3
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU,//4
		    KleFuEntry.COLUMN_NAME_OU//5
		};


		// String für die Where Clause
		String whereClause =
			    KleFuEntry.COLUMN_NAME_GEKLETTERT + " LIKE ?";
		// String für die Where Options
		String[] whereOptions = {
			    "1"
		};	
		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
		    projection,                               // The columns to return
		    whereClause,                              // The columns for the WHERE clause
		    whereOptions,                             // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
		int cursorLaenge=c.getCount();
			if (!(cursorLaenge<1)) {	
			SparseIntArray hm = new SparseIntArray();
			SparseIntArray hmVorstieg = new SparseIntArray();
			SparseIntArray hmRP = new SparseIntArray();
			// String aus Cursor lesen	
			c.moveToFirst();
		    while (c.isAfterLast() == false) {
		    	int key;
		    	boolean isRP;
		    	boolean isoU;
		    	try {
		    		isoU=c.getInt(5)>0;
		    	} catch (Exception e) {
		    		isoU=false;
		    	}		    			    	
		    	
		    	try {
		    		isRP=c.getInt(3)>0;
		    	} catch (Exception e) {
		    		isRP=false;
		    	}
		    	if (isRP) {//is Weg RP geklettert?
		    		int schwierigkeitRP = c.getInt(1);
		    		if (schwierigkeitRP == 0) {
		    			key = isoU ? c.getInt(4) : c.getInt(0);
		    			if (c.getInt(4)==0) key = c.getInt(0);
		    		} else {
		    			key = schwierigkeitRP;
		    		}
		        	hmRP.put(key, hmRP.get(key)+1);
		    	} else {
		    		key = isoU ? c.getInt(4) : c.getInt(0);
		    		if (c.getInt(4)==0) key = c.getInt(0);
		    	}

		    	boolean isVorstieg;
		    	try {
		    		isVorstieg=c.getInt(2)>0;
		    	} catch (Exception e) {
		    		isVorstieg=false;
		    	}		    	
		    	if (isVorstieg) {//Wurde Weg vorgestiegen
		    		hmVorstieg.put(key,  hmVorstieg.get(key)+1);
		    	}
		    	
	        	hm.put(key, hm.get(key)+1);

	        	c.moveToNext();
		    }
		    
		    //Alle ermittelten Punkte dem Diagramm hinzufügen
//		    for(int i = 0; i < hm.size(); i++) {
//	           addPoint(hm.keyAt(i), hm.valueAt(i));		    	
//		    }
		    
		    for(int i = 0; i < hm.size(); i++) {
		    	int key = hm.keyAt(i);
		    	Integer anzahlGesamt = hm.get(key);
		    	Integer anzahlRP = hmRP.get(key);
		    	Integer anzahlVorstieg = hmVorstieg.get(key);
		    	Integer anzahlNachstieg = anzahlGesamt-anzahlVorstieg;
		    	serieRP.add(key, anzahlGesamt);
		    	float annotationY = anzahlGesamt.floatValue()-(anzahlRP.floatValue())/2;
		    	if (anzahlRP != 0)  {
		    		serieVorstieg.addAnnotation(anzahlRP.toString(), key, annotationY);
		    	} 
		    	serieVorstieg.add(key, anzahlGesamt-anzahlRP);		    	
		    	if ((anzahlVorstieg-anzahlRP) != 0) {
		    		annotationY = anzahlGesamt.floatValue()-anzahlRP.floatValue()-(anzahlVorstieg.floatValue()-anzahlRP.floatValue())/2;
		    		serieNachstieg.addAnnotation(((Integer)(anzahlVorstieg-anzahlRP)).toString(), key, annotationY);
		    	} 
		    	serieNachstieg.add(key, anzahlNachstieg);
		    	if (anzahlNachstieg!=0) {
			    	annotationY = anzahlGesamt.floatValue()-anzahlVorstieg.floatValue() - (anzahlNachstieg.floatValue())/2;
			    	serieAnnotations.addAnnotation(anzahlNachstieg.toString(), key, annotationY);		    		
		    	}
		    	serieAnnotations.add(key, 0);		    	
		    }		   		   
		}
	    c.close();
	    
	    setAxisLimits();
	}

	private void setAxisLimits() {
		//x min und max festlegen
		double xmin = serieRP.getMinX();
		double xmax = serieRP.getMaxX();
		mRenderer.setXAxisMin(xmin-1);
		mRenderer.setXAxisMax(xmax+1);
		double panLimits[] = {
				xmin-1, xmax+1, 0, serieRP.getMaxY()+1
		};
        mRenderer.setPanLimits(panLimits);
		setXTextLabels();
	}
	
	public GraphicalView getMChartView(){
	     return mChartView;
	}

	@Override
	public void zoomApplied(ZoomEvent arg0) {
		mRenderer.setYAxisMin(0);
		double xAxisMin = mRenderer.getXAxisMin();
		double xAxisMax = mRenderer.getXAxisMax();
		if (xAxisMin < 5) mRenderer.setXAxisMin(5);
		if (xAxisMax > KleFuEntry.MAXSCHWIERIGKEIT+1) mRenderer.setXAxisMax(KleFuEntry.MAXSCHWIERIGKEIT+1);
		setXTextLabels();
		refresh();
	}
 
	@Override
	public void zoomReset() {
		setAxisLimits();		
	}
}

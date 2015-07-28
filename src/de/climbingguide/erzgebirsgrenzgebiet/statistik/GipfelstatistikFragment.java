package de.climbingguide.erzgebirsgrenzgebiet.statistik;

import java.util.HashMap;

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

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class GipfelstatistikFragment extends Fragment implements ZoomListener{

	public GipfelstatistikFragment () {}	
	
	private XYMultipleSeriesDataset mDataset;
    private XYMultipleSeriesRenderer mRenderer;
	private GraphicalView mChartView;
	private XYSeries serie;
	private XYSeries white;
	private XYSeries seriesAnnotations;
	private XYSeries seriesAnnotationsZwei;
	private XYSeriesRenderer renderer;
	private XYSeriesRenderer rendererWhite;
	private HashMap<String, Integer> gebiete;
	double panLimits[];
	
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_schwierigkeitsstatistik, container, false);	
		
		// Diagramm-Zeuch
	    String bestiegene=getString(R.string.bestiegene);
		serie = new XYSeries(bestiegene+" "+getString(R.string.im_vorstieg));
		white = new XYSeries(bestiegene+" "+getString(R.string.im_nachstieg));
		seriesAnnotations = new XYSeries("");
		seriesAnnotationsZwei = new XYSeries("");
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

	private void setXAxisLimits() {
		double xmin = serie.getMinX();
		double xmax = serie.getMaxX();
		mRenderer.setXAxisMin(xmin-0.5);
		mRenderer.setXAxisMax(xmax+0.5);
        mRenderer.setPanLimits(panLimits);
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
	     mRenderer.setMargins(new int[] { 20, 80, 114, 0 });
//	     mRenderer.setMargins(new int[] { 20, 80, 50, 0 });
	     mRenderer.setZoomButtonsVisible(true);
	     mRenderer.setXLabels(0);
	     mRenderer.setYAxisMin(0);
	     mRenderer.setAxesColor(getResources().getColor(R.color.black));
	     mRenderer.setLabelsColor(getResources().getColor(R.color.black));	     
	     mRenderer.setYTitle(getString(R.string.anzahl));
	     mRenderer.setYLabelsAlign(Align.RIGHT, 0);
	     mRenderer.setShowAxes(true);
	     mRenderer.setBarSpacing(2f);	     
	     mRenderer.setXLabelsAngle(45);
	     mRenderer.setAxisTitleTextSize(25);
	     mRenderer.setYLabelsPadding(8);
	     mRenderer.setPanEnabled(true, true);
	     
	     renderer = new XYSeriesRenderer();
	     renderer.setColor(getResources().getColor(R.color.green));
	     rendererWhite = new XYSeriesRenderer();
	     rendererWhite.setColor(getResources().getColor(R.color.lightblue));
	     rendererWhite.setAnnotationsColor(Color.BLACK);
	     rendererWhite.setAnnotationsTextSize(25);
	     rendererWhite.setAnnotationsTextAlign(Align.CENTER);
	     XYSeriesRenderer rendererAnnotations = new XYSeriesRenderer();
	     rendererAnnotations.setShowLegendItem(false);
	     rendererAnnotations.setAnnotationsColor(Color.BLACK);
	     rendererAnnotations.setAnnotationsTextSize(Color.BLACK);
	     rendererAnnotations.setColor(Color.TRANSPARENT);
	     rendererAnnotations.setAnnotationsTextSize(25);
	     rendererAnnotations.setAnnotationsTextAlign(Align.CENTER);
	     
	     mRenderer.addSeriesRenderer(renderer);
	     mRenderer.addSeriesRenderer(rendererWhite);
	     mRenderer.addSeriesRenderer(rendererAnnotations);
	     mRenderer.addSeriesRenderer(rendererAnnotations);
	     mDataset.addSeries(serie);
	     mDataset.addSeries(white);
	     mDataset.addSeries(seriesAnnotations);
	     mDataset.addSeries(seriesAnnotationsZwei);	     
	     mChartView = ChartFactory.getBarChartView(getActivity(), mDataset, mRenderer, BarChart.Type.STACKED);
	     mChartView.addZoomListener(this, true, true);
	     //Gebietelabels holen
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    KleFuEntry.COLUMN_NAME_GEBIET
		};

		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GEBIETE,  // The table to query
		    projection,                               // The columns to return
		    null,                              // The columns for the WHERE clause
		    null,                             // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
			// Strings aus Cursor lesen
			gebiete = new HashMap<String, Integer>();
			c.moveToFirst();
			int j = 0;
		    while (c.isAfterLast() == false) {
		    	String gebiet = c.getString(0);
			    mRenderer.addXTextLabel(j, gebiet);
	        	this.gebiete.put(gebiet, j);
	        	j++;
	        	c.moveToNext();
		    }
		    c.close();		    
	 }
	
	public void refresh() {
		 mChartView.repaint();
	 }
	public void addPoint(double x, double y){
		 serie.add(x, y);
	}
	public void addPoints(){
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    KleFuEntry.COLUMN_NAME_GEBIET,
		    KleFuEntry.COLUMN_NAME_VORSTIEG
		};


		// String für die Where Clause
		String whereClause =
			    KleFuEntry.COLUMN_NAME_BESTIEGEN + " LIKE ?";
		// String für die Where Options
		String[] whereOptions = {
			    "1"
		};	
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
		
		int cursorLaenge=c.getCount();
			if (!(cursorLaenge<1)) {	
			SparseIntArray hm = new SparseIntArray();
			SparseIntArray hmVorstieg = new SparseIntArray();
			SparseIntArray hmGesamt = new SparseIntArray(); 
			// String aus Cursor lesen	
			c.moveToFirst();
			SparseArray<String> hmGebiete = new SparseArray<String>();
		    while (c.isAfterLast() == false) {
		    	String gebiet = c.getString(0);
		    	int key = gebiete.get(gebiet);
		    	hmGebiete.put(key, gebiet);
	        	hm.put(key, hm.get(key)+1);
	        	if (c.getInt(1)>0) hmVorstieg.put(key, hmVorstieg.get(key)+1);
	        	c.moveToNext();
		    }
		    
		    //Alle ermittelten Punkte dem Diagramm hinzufügen
		    for(int i = 0; i < hm.size(); i++) {
		    	int key=hm.keyAt(i);
		    	int anzahlGes=hm.valueAt(i);
		    	int anzahlImVorstieg=hmVorstieg.valueAt(i);
		    	int anzahlImNachstieg=anzahlGes-anzahlImVorstieg;
		    	int maxGipfel=KleFuContract.getMaxGipfel(hmGebiete.get(key));
		    	boolean isVorUndNachstieg=(anzahlImVorstieg>0)&&(anzahlImNachstieg>0);
	           addPoint(key, anzahlGes);
	           white.add(key, anzahlGes-anzahlImVorstieg);
	           if (anzahlImVorstieg>0) {	        	   
	        	   white.addAnnotation(((Integer)((Float)(((Integer)anzahlImVorstieg).floatValue()/maxGipfel*100)).intValue()).toString() + " %",
	        		   key, anzahlGes-((Integer)anzahlImVorstieg).floatValue()/2);
	           }	        	   
	           seriesAnnotations.add(key, 0);
	           seriesAnnotationsZwei.add(key, 0);
	           if (anzahlImNachstieg>0) {
	        	   seriesAnnotations.addAnnotation(((Integer)((Float)(((Integer)(anzahlImNachstieg)).floatValue()/maxGipfel*100)).intValue()).toString() + " %",
	        		   key, ((Integer)anzahlImNachstieg).floatValue()/2);
	           }
	           if (isVorUndNachstieg) {
	        	   hmGesamt.put(key, anzahlGes);
	           } else {
	        	   hmGesamt.put(key, -1);
	           }
		    }
		    double maxY = serie.getMaxY();
		    for (int i=0; i<hmGesamt.size(); i++) {
		    	int key=hmGesamt.keyAt(i);
		    	int anzahlGes=hmGesamt.get(key);
		    	if (anzahlGes>0) {
		    		int maxGipfel=KleFuContract.getMaxGipfel(hmGebiete.get(key));
		    		seriesAnnotationsZwei.addAnnotation(((Integer)((Float)(((Integer)(anzahlGes)).floatValue()/maxGipfel*100)).intValue()).toString() + " %", key, anzahlGes+maxY*0.01);
		    	}
//		    	seriesAnnotationsZwei.addAnnotation(((Integer)((Float)(((Integer)(anzahlGes)).floatValue()/maxGipfel*100)).intValue()).toString() + " %", key, 2);
		    }
		    mRenderer.setYAxisMax(maxY*1.15);
		}
	    c.close();
	    
		double panLimits[] = {
				serie.getMinX()-1, serie.getMaxX()+1, 0, serie.getMaxY()+1
		};
		this.panLimits=panLimits;
		setXAxisLimits();

	}	
	public GraphicalView getMChartView(){
	     return mChartView;
	}

	@Override
	public void zoomApplied(ZoomEvent arg0) {
		mRenderer.setYAxisMin(0);
		double xAxisMin = mRenderer.getXAxisMin();
		double xAxisMax = mRenderer.getXAxisMax();
		if (xAxisMin < (serie.getMinX()-0.5)) mRenderer.setXAxisMin(serie.getMinX()-0.5);
		if (xAxisMax > (serie.getMaxX()+0.5)) mRenderer.setXAxisMax(serie.getMaxX()+0.5);
		refresh();		
	}

	@Override
	public void zoomReset() {
		setXAxisLimits();				
	}
}

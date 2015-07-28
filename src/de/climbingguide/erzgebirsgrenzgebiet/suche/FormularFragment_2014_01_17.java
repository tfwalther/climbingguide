package de.climbingguide.erzgebirsgrenzgebiet.suche;

import java.util.ArrayList;

import net.sqlcipher.Cursor;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;
import de.climbingguide.erzgebirsgrenzgebiet.settings.SettingsActivity;

public class FormularFragment_2014_01_17 extends Fragment implements OnClickListener, OnItemClickListener,
OnFocusChangeListener, Sucher {

	public FormularFragment_2014_01_17 () {}
	
	//Suche Zeug
	public SucheHandler sucheHandler;
	@Override 
	public SucheHandler getSucheHandler() { return sucheHandler; }	
	private SucheThread sucheThread;
	@Override
	public SucheThread getSucheThread() { return sucheThread; }

	private static boolean gebietBekannt=false;
	private static boolean gipfelBekannt=false;
	private static boolean gipfelnummerbisEnabled=false;
	private static boolean schwierigkeitBisEnabled=false;
	private static boolean fehlerhafteEingabe=false;
	private static ArrayList<String> gebiete = new ArrayList<String>();
	private static ArrayList<Gipfel> gipfel = new ArrayList<Gipfel>();
	private static ArrayList<String> alleGipfel = new ArrayList<String>();
	private static ArrayList<String> gipfelnummer = new ArrayList<String>();
	private static ArrayList<String> gipfelnummerBis = new ArrayList<String>();
	private static ArrayList<Weg> wege = new ArrayList<Weg>();
	private static ArrayList<String> schwierigkeitVon = new ArrayList<String>();
	private static ArrayList<String> schwierigkeitBis = new ArrayList<String>();

	private static int maxGipfel=KleFuEntry.MAXGIPFEL;
	
	private static final int COMPLETETEXTVIEWGIPFEL=0; // Welche CompleteTextView wurde gedrückt
	private static final int COMPLETETEXTVIEWGEBIETE=1;
	private static final int COMPLETETEXTVIEWGIPFELNUMMER=2;
	private static final int COMPLETETEXTVIEWGIPFELNUMMERBIS=3;
	private static final int COMPLETETEXTVIEWWEGE=4;
	private static final int COMPLETETEXTVIEWSCHWIERIGKEIT=5;
	private static final int COMPLETETEXTVIEWSCHWIERIGKEITBIS=6;
	
	
	private static int welcheAutoCompleteTextView=COMPLETETEXTVIEWGEBIETE;
	protected static boolean afterPageChange=false;
	
	private View rootView;
	
	DialogInterface.OnClickListener okListener =
	new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}	
	};

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_start_suche:
			buttonSucheClick();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(
	      Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.suche_formular, menu);
	}
	
	/* 
	 * öffnet den Einstellungen Screen	
	 */
	public void openSettings() {
		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		startActivity(intent);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.activity_suche_formular, container, false);

		initiateSchwierigkeitVon();
	// Strings für Auto-ausfüllen aus Datenbank laden
		// richtige Datenbank laden
		setGebiete(); //private String[] Gebiete mit den installierten Gebieten belegen
		setDropDownMenuGebiete(); //weist dem AutoCompleteTextViewGebiete den String[] Gebiete zu 
		
		setGipfel();
		alleGipfel = new ArrayList<String>(); 
		
		for (Gipfel gippel : gipfel) {
			alleGipfel.add(gippel.getGipfel());
		}
		
		setDropDownMenuGipfel();
		
		setGipfelnummer();
		setDropDownMenuGipfelnummer();
		
		setWege();		
		setDropDownMenuWege();
		
		setDropDownMenuSchwierigkeit();

//	Gebiete bei anklicken aufklappen wenn Item gewählt wird Methode zuweisen 
 	     AutoCompleteTextView gebiet = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGebiet);
	     gebiet.setOnItemClickListener(this);
	     gebiet.setOnClickListener(this);
	     gebiet.setOnFocusChangeListener(this);

	    //	Gipfel wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView gipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
	     gipfel.setOnItemClickListener(this);
	     gipfel.setOnClickListener(this);
	     gipfel.setOnFocusChangeListener(this);	     
	     
		 //	Gipfelnummer wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView gipfelnummer = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
	     gipfelnummer.setOnItemClickListener(this);
	     gipfelnummer.setOnClickListener(this);
	     gipfelnummer.setOnFocusChangeListener(this);

		 //	Gipfelnummer wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView gipfelnummerbis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
	     gipfelnummerbis.setOnItemClickListener(this);
	     gipfelnummerbis.setOnClickListener(this);
	     gipfelnummerbis.setOnFocusChangeListener(this);	     
	     
		 //	Weg wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView textViewWeg = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);
	     textViewWeg.setOnItemClickListener(this);
	     textViewWeg.setOnClickListener(this);
	     textViewWeg.setOnFocusChangeListener(this);
	     
		 //	Schwierigkeit wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView textViewSchwierigkeit = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
	     textViewSchwierigkeit.setOnItemClickListener(this);
	     textViewSchwierigkeit.setOnClickListener(this);
	     textViewSchwierigkeit.setOnFocusChangeListener(this);	
	     
		 //	SchwierigkeitBis wenn Item gewählt wird Methode zuweisen 
	     AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
	     textViewSchwierigkeitBis.setOnItemClickListener(this);
	     textViewSchwierigkeitBis.setOnClickListener(this);
	     textViewSchwierigkeitBis.setOnFocusChangeListener(this);	     
        
	     Button buttonStarteSuche = (Button) rootView.findViewById(R.id.ButtonStarteSuche);
	     buttonStarteSuche.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				buttonSucheClick();				
			}
		});
	     
	     Button buttonClearClick = (Button) rootView.findViewById(R.id.buttonClear);
	     buttonClearClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clear();
			}
		});
	     
        return rootView;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	clear();
    }
    
	private void initiateSchwierigkeitVon() {
		for (int i=11; i<35; i++) {
			schwierigkeitVon.add(Schwierigkeit.SchwierigkeitIntToString(i));
		}
		for (int i=1; i<5; i++) {
			schwierigkeitVon.add(Schwierigkeit.SchwierigkeitIntToString(i));
		}
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
	
	int cursorLaenge=c.getCount();
	if (!(cursorLaenge<1)) {
		gebiete = new ArrayList<String>();
	
		// String aus Cursor lesen
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			gebiete.add(c.getString(0));
			c.moveToNext();
		}
	}
    c.close();
}
	
	private void setGipfel(String stringGebiet) {
	// Define a projection that specifies which columns from the database
	// you will actually use after this query.
	String[] projection = {
	    KleFuEntry.COLUMN_NAME_GIPFEL,
	    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,
	    KleFuEntry.COLUMN_NAME_GEBIET,
	    KleFuEntry.COLUMN_NAME_BESTIEGEN
	};


	// String für die Where Clause
	String whereClause =
		    KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ?";
	// String für die Where Options
	String[] whereOptions = {
		    stringGebiet
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
		gipfel.clear();
		
		// String aus Cursor lesen
		c.moveToFirst();
	    while (c.isAfterLast() == false) {
	        gipfel.add(new Gipfel(
	        		c.getString(0),
	        		c.getInt(1),
	        		c.getString(2),
	        		c.getInt(3)>0));
	   	    c.moveToNext();
	    }
	}
    c.close();
}

	private void setGipfel() {
	// Define a projection that specifies which columns from the database
	// you will actually use after this query.
	String[] projection = {
	    KleFuEntry.COLUMN_NAME_GIPFEL,
	    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,
	    KleFuEntry.COLUMN_NAME_GEBIET,
	    KleFuEntry.COLUMN_NAME_BESTIEGEN
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
	
	int cursorLaenge=c.getCount();
	if (!(cursorLaenge<1)) {
		gipfel.clear();
		
		// String aus Cursor lesen
		c.moveToFirst();
	    while (c.isAfterLast() == false) {
	        gipfel.add(new Gipfel(
	        		c.getString(0),
	        		c.getInt(1),
	        		c.getString(2),
	        		c.getInt(3)>0));
	   	    c.moveToNext();
	    }
	}
    c.close();
}
	
	private void setWege() {
		wege.clear();		
	}
	
	private void setWege(String gipfelId) {
		if (gipfelBekannt) {	
			//Datenbankabfrage nach den Wegen eines Gipfels
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry._ID
			};
			
			String whereClause =
				    KleFuEntry.COLUMN_NAME_GIPFELID + " LIKE ?";
			// String für die Where Options
			String[] whereOptions = {
				    gipfelId
			};
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereOptions,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			int cursorLaenge=c.getCount();
			if (!(cursorLaenge<1)) {
				wege.clear();
			
				// String aus Cursor lesen
				c.moveToFirst();
				while (c.isAfterLast() == false) {
					wege.add(new Weg(c.getInt(0)));
					c.moveToNext();
				}
			}
		    c.close();			
		}
	}
	
	private void setGipfelnummer() {
		int gipfelnummerSize=gipfelnummer.size();
		if (maxGipfel<gipfelnummerSize)
			for (Integer i=maxGipfel; i<gipfelnummerSize; i++) {
				gipfelnummer.remove(i);
		} else {			
			for (Integer i=gipfelnummerSize+1; i <=maxGipfel; i++) {			
				gipfelnummer.add(i.toString());
			}
		}
	}

	private void setGipfelnummerBis(int minGipfel) {
		gipfelnummerBis.clear();
		for (Integer i=minGipfel; i <=maxGipfel; i++) {			
			gipfelnummerBis.add(i.toString());
		}
	}
	private void setSchwierigkeitBis(Schwierigkeit schwierigkeit) {
//		if (schwierigkeit.getSchwierigkeitInt() <= KleFuEntry.MAXSCHWIERIGKEITSPRUNG) {
//			SchwierigkeitBis = new String[(KleFuEntry.MAXSCHWIERIGKEIT-schwierigkeit.getSchwierigkeitInt()-KleFuEntry.MAXSCHWIERIGKEITLUECKE)];			
//		} else {
//			SchwierigkeitBis = new String[(KleFuEntry.MAXSCHWIERIGKEIT-schwierigkeit.getSchwierigkeitInt())];			
//		}
		
		schwierigkeitBis.clear();
		for (int i=(schwierigkeit.getSchwierigkeitInt()+1); i <= KleFuEntry.MAXSCHWIERIGKEIT; i++) {			
			schwierigkeitBis.add(Schwierigkeit.SchwierigkeitIntToString(i));
			if (i==4) i=10; //Sprung von Sprung auf Wandkletterskala
		}
	}
	
	private void setDropDownMenuGebiete() {
//	 String-Liste dem Drop-down Menü zuweisen
//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//            android.R.layout.simple_dropdown_item_1line, gebiete);
//	AutoCompleteTextView textView = (AutoCompleteTextView)
//            rootView.findViewById(R.id.autoCompleteTextViewGebiet);
//    textView.setAdapter(adapter);	
	StringListAdapterLeft adapter = new StringListAdapterLeft(getActivity(), R.layout.drop_down_layout_left, gebiete);
AutoCompleteTextView textView = (AutoCompleteTextView)
      rootView.findViewById(R.id.autoCompleteTextViewGebiet);
textView.setAdapter(adapter);
	}
	
	private void setDropDownMenuGipfelnummer() {
	// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterCenter adapter = new StringListAdapterCenter(getActivity(), R.layout.drop_down_layout_centered, gipfelnummer);
	AutoCompleteTextView textView = (AutoCompleteTextView)
            rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
    textView.setAdapter(adapter);
	}
	
	private void setDropDownMenuGipfelnummerBis() {
	// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterCenter adapter = new StringListAdapterCenter(getActivity(), R.layout.drop_down_layout_centered, gipfelnummerBis);
	AutoCompleteTextView textView = (AutoCompleteTextView)
            rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
    textView.setAdapter(adapter);
	}	
	
	private void setDropDownMenuGipfel() {
	// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterGipfel adapter = new StringListAdapterGipfel(getActivity(), R.layout.drop_down_layout_gipfel, gipfel);
	AutoCompleteTextView textView = (AutoCompleteTextView)
            rootView.findViewById(R.id.autoCompleteTextViewGipfel);
    textView.setAdapter(adapter);
	}
	
	private void setDropDownMenuWege() {
		// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterWege adapter = new StringListAdapterWege(R.layout.drop_down_layout_left, wege);
		AutoCompleteTextView textView = (AutoCompleteTextView)
	            rootView.findViewById(R.id.AutoCompleteTextViewWeg);
	    textView.setAdapter(adapter);
	}

	private void setDropDownMenuSchwierigkeit() {
		// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterCenter adapter = new StringListAdapterCenter(getActivity(), R.layout.drop_down_layout_centered, schwierigkeitVon);
		AutoCompleteTextView textView = (AutoCompleteTextView)
	            rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
	    textView.setAdapter(adapter);
	}
	
	private void setDropDownMenuSchwierigkeitBis() {
		// String-Liste dem Drop-down Menü zuweisen
		StringListAdapterCenter adapter = new StringListAdapterCenter(getActivity(), R.layout.drop_down_layout_centered, schwierigkeitBis);
		AutoCompleteTextView textView = (AutoCompleteTextView)
	            rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
	    textView.setAdapter(adapter);
	}	
	
	@Override
	public void onClick(View v) {
		setDropDownMenuGebiete();		
		switch (v.getId()){
		case R.id.autoCompleteTextViewGebiet:
			 gebietBekannt=false;
		 	 setGipfel(); //Falls Benutzer bereits gesetztes Gebiet wieder löscht wieder alle Gipfel anzeigen
		 	 setDropDownMenuGipfel();
		 	 
		 	 setGipfelnummer();
		 	 setDropDownMenuGipfelnummer();
		 	 AutoCompleteTextView gebiet = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGebiet);
			 if (!fehlerhafteEingabe) gebiet.showDropDown();
		     welcheAutoCompleteTextView=COMPLETETEXTVIEWGEBIETE;
			 break;
		case R.id.AutoCompleteTextViewGipfelnummer:
			 gipfelBekannt=false;
			 welcheAutoCompleteTextView=COMPLETETEXTVIEWGIPFELNUMMER;
		 	 AutoCompleteTextView gipfelnummer = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
		 	if (!fehlerhafteEingabe) gipfelnummer.showDropDown();
			 break;
		case R.id.AutoCompleteTextViewGipfelnummerBis:
			 welcheAutoCompleteTextView=COMPLETETEXTVIEWGIPFELNUMMERBIS;
			 AutoCompleteTextView gipfelnummerbis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
			 if (gipfelnummerbisEnabled)
				 if (!fehlerhafteEingabe) gipfelnummerbis.showDropDown();
			 break;
		case R.id.autoCompleteTextViewGipfel:
			 gipfelBekannt=false;
		     welcheAutoCompleteTextView=COMPLETETEXTVIEWGIPFEL;
		 	 AutoCompleteTextView gipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
		 	if (!fehlerhafteEingabe) gipfel.showDropDown();
			 break;
		case R.id.AutoCompleteTextViewWeg:
		     welcheAutoCompleteTextView=COMPLETETEXTVIEWWEGE;		
		 	if (gipfelBekannt) { 
		 		AutoCompleteTextView textViewWeg = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);
		 		if (!fehlerhafteEingabe) textViewWeg.showDropDown();
		 	}
			break;
		case R.id.AutoCompleteTextViewSchwierigkeitVon:
			welcheAutoCompleteTextView=COMPLETETEXTVIEWSCHWIERIGKEIT;
			AutoCompleteTextView textViewSchwierigkeitVon = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
			if (!fehlerhafteEingabe) textViewSchwierigkeitVon.showDropDown();
			break;
		case R.id.AutoCompleteTextViewSchwierigkeitBis:
			welcheAutoCompleteTextView=COMPLETETEXTVIEWSCHWIERIGKEITBIS;
			if(schwierigkeitBisEnabled) {
				AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
				if (!fehlerhafteEingabe) textViewSchwierigkeitBis.showDropDown();
			}
		}
		fehlerhafteEingabe=false;
//		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
    	String stringSet;	    
    	stringSet=parent.getItemAtPosition(position).toString();
    	ItemSet(stringSet);
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		String stringSet;
		
		if (afterPageChange) {
			afterPageChange=false;
			return;
		}
		if (hasFocus==true) {
			onClick(v);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.fehlerhafte_eingabe);
			builder.setPositiveButton(R.string.ok, okListener);
			
			
			int viewId = v.getId();
			switch (viewId) {
			case R.id.autoCompleteTextViewGebiet:
				stringSet = getGebiet();
				if (gebiete.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("")) {
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;
						gebietBekannt=false;
					}
				}
			break;
			case R.id.autoCompleteTextViewGipfel:
				stringSet = getGipfel();
				if (alleGipfel.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("")) {						
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;							
						gipfelBekannt=false;
					}							
				}
			break;
			case R.id.AutoCompleteTextViewGipfelnummer:
				stringSet = getGipfelnummer().toString();
				if (gipfelnummer.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("0")) {						
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;						
						clearGipfelnummerBis();
						gipfelnummerbisEnabled=false;
					}							
				}
			break;
			case R.id.AutoCompleteTextViewGipfelnummerBis:
				stringSet = getGipfelnummerBis().toString();
				if (gipfelnummerBis.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("0")) {						
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;							
					}
				}
			break;
			case R.id.AutoCompleteTextViewSchwierigkeitVon:
				stringSet = getSchwierigkeitVonString();
				if (schwierigkeitVon.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("")) {
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;							
						clearSchwierigkeitBis();
						schwierigkeitBisEnabled=false;
					}
				}
			break;
			case R.id.AutoCompleteTextViewSchwierigkeitBis:
				stringSet = getSchwierigkeitBisString();
				if (schwierigkeitBis.contains(stringSet)) {
					ItemSet(stringSet);
				} else {
					if (!stringSet.equals("")) {						
						AutoCompleteTextView textViewSet = (AutoCompleteTextView) rootView.findViewById(viewId);
						textViewSet.setText("");
						builder.show();
						fehlerhafteEingabe=true;							
					}
				}
			break;
			case R.id.AutoCompleteTextViewWeg:
				stringSet = getWeg();
				if (wege.contains(stringSet)) {
					ItemSet(stringSet);
				}
//				else {
	//					if (!stringSet.equals("")) {						
//						Button buttonKurzansicht = (Button) rootView.findViewById(R.id.ButtonWegKurzansicht);
//						buttonKurzansicht.setVisibility(View.GONE);
//	//					}
//				}
			break;				
			}
		}
	}

	@Override
	public String getGebiet() {
		AutoCompleteTextView gebiet = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGebiet);
		return gebiet.getText().toString();
	}

	@Override
	public Integer getGipfelnummer() {
		try {
			AutoCompleteTextView gipfelnummer = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
			return Integer.parseInt(gipfelnummer.getText().toString());
		}
		catch (Exception e) {
			return 0;
		}	
	}	

	@Override
	public Integer getGipfelnummerBis() {
		try {
			AutoCompleteTextView gipfelnummerbis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
			return Integer.parseInt(gipfelnummerbis.getText().toString());
		}
		catch (Exception e) {
			return 0;
		}	
	}

	@Override
	public String getGipfel() {
		AutoCompleteTextView gipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
		return gipfel.getText().toString();
	}

	private String getWeg() {
		AutoCompleteTextView weg = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);
		return weg.getText().toString();
	}	

	@Override
	public Integer getSchwierigkeitVonInt() {
		AutoCompleteTextView schwierigkeitVon = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
		return Schwierigkeit.SchwierigkeitStrToInt(schwierigkeitVon.getText().toString());
	}	

	private String getSchwierigkeitVonString() {
		AutoCompleteTextView schwierigkeitVon = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
		return schwierigkeitVon.getText().toString();
	}	
	
	@Override
	public Integer getSchwierigkeitBisInt() {
		AutoCompleteTextView schwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
		return Schwierigkeit.SchwierigkeitStrToInt(schwierigkeitBis.getText().toString());
	}	
	
	private String getSchwierigkeitBisString() {
		AutoCompleteTextView schwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
		return schwierigkeitBis.getText().toString();
	}		

	@Override	
	public Boolean isSchonGeklettert() {
		 CheckBox checkBoxSchonGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxGeklettert);
		 return checkBoxSchonGeklettert.isChecked();
	}			

	@Override
	public Boolean isNochNichtGeklettert() {
		 CheckBox checkBoxNochNichtGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxNochNichtGeklettert);
		 return checkBoxNochNichtGeklettert.isChecked();
	}		

	private void clear() {
		 gebietBekannt=false;
		 gipfelBekannt=false;
		 maxGipfel=KleFuEntry.MAXGIPFEL;
		 setGipfel();
		 setDropDownMenuGipfel();
			
		 setGipfelnummer();
		 setDropDownMenuGipfelnummer();
			
		 AutoCompleteTextView textViewGebiet = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGebiet);
		 AutoCompleteTextView textViewGipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
		 AutoCompleteTextView textViewGipfelnummer = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
		 AutoCompleteTextView textViewSchwierigkeitVon = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
		 AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
		 
		 clearWege();
		 clearGipfelnummerBis();
		 clearSchwierigkeitBis();
		 
		 textViewGebiet.setText(""); 			 
		 textViewGipfel.setText("");
		 textViewGipfelnummer.setText("");
		 
		 textViewSchwierigkeitVon.setText("");
		 textViewSchwierigkeitBis.setText("");
		 
		 CheckBox checkBoxSchonGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxGeklettert);
		 checkBoxSchonGeklettert.setChecked(true);

		 CheckBox checkBoxNochNichtGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxNochNichtGeklettert);
		 checkBoxNochNichtGeklettert.setChecked(true);
	}
		
	public void buttonSucheClick() {		
        sucheHandler = new SucheHandler(getActivity(), this, sucheThread);	        
    	sucheThread = new SucheThread(getActivity(), this);            	
        sucheThread.start();       
	}	
	
	private void clearWege() {
		 setWege();
		 setDropDownMenuWege();
//		 weg="";
//		 beschreibung="";
		 	 
		 AutoCompleteTextView textViewWege = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);
		 textViewWege.setText("");
		 
		 
	}
	
	private void clearGipfelnummerBis() {
//   		 GipfelnummerBis={};
//   		 setDropDownMenuGipfelnummerBis();
		 AutoCompleteTextView textViewGipfelnummerBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
		 textViewGipfelnummerBis.setText("");
		 textViewGipfelnummerBis.setClickable(false);
		 textViewGipfelnummerBis.setFocusable(false);
		 textViewGipfelnummerBis.setFocusableInTouchMode(false);
		 gipfelnummerbisEnabled=false;
	}

	private void clearSchwierigkeitBis() {
//		 GipfelnummerBis={};
//		 setDropDownMenuGipfelnummerBis();
	 AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
	 textViewSchwierigkeitBis.setText("");
	 textViewSchwierigkeitBis.setClickable(false);
	 textViewSchwierigkeitBis.setFocusable(false);
	 textViewSchwierigkeitBis.setFocusableInTouchMode(false);
	 schwierigkeitBisEnabled=false;
}

	
	private void setGipfelnummerBis(String stringGipfelnummer) {
	int intGipfelNummer=Integer.parseInt(stringGipfelnummer);
	AutoCompleteTextView gipfelnummerBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);

	if (intGipfelNummer!=maxGipfel) {
		gipfelnummerBis.setClickable(true);
		gipfelnummerBis.setFocusable(true);
		gipfelnummerBis.setFocusableInTouchMode(true);
		setGipfelnummerBis((intGipfelNummer + 1));
		setDropDownMenuGipfelnummerBis();
		gipfelnummerbisEnabled=true;
	}
	if (intGipfelNummer>=getGipfelnummerBis()) {
		gipfelnummerBis.setText("");
	}
	
	}
	
	
 //???
private void setGipfel(String stringGebiet, String stringGipfelnummer) {
	gipfelBekannt=true;
	//Datenbankabfrage welcher Gipfel gehört zu der Nummer?

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection1 = {
			    KleFuEntry._ID,
			    KleFuEntry.COLUMN_NAME_GIPFEL
		};
		// String für die Where Clause
		String whereClause1 =
			    KleFuEntry.COLUMN_NAME_GIPFELNUMMER + " LIKE ?" +
			    " AND " + KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ?";
		// String für die Where Options
		String[] whereOptions1 = {
			    stringGipfelnummer,
			    stringGebiet
		};
		
		// Gebiete aus Datenbank lesen
		Cursor c1 = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection1,           // The columns to return
		    whereClause1,                                // The columns for the WHERE clause
		    whereOptions1,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );

		String gipfelId1=null;
		String gipfel1=null;
		int cursorLaenge1=c1.getCount();
			if (!(cursorLaenge1<1)) {
			// String aus Cursor lesen
			c1.moveToFirst();
	        gipfelId1=c1.getString(0);
	        gipfel1=c1.getString(1);
		}
	    c1.close();
    
	    AutoCompleteTextView textViewGipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
	    textViewGipfel.setText(gipfel1);
		setDropDownMenuGipfel();
	    
		setDropDownMenuGipfelnummer();
		
		setWege(gipfelId1); //WegeDropDownMenü aktualisieren
		setDropDownMenuWege();		
	}

private void ItemSet(String stringSet) {
    AutoCompleteTextView textViewGipfelnummer = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
    Schwierigkeit schwierigkeit;

	switch (welcheAutoCompleteTextView){
	case COMPLETETEXTVIEWGEBIETE:
		maxGipfel = KleFuContract.getMaxGipfel(stringSet);
		gebietBekannt = true;
		
//Datenbankabfrage, ob eingetragener Gipfel Element des Gebietes, wenn nicht dann löschen
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			    KleFuEntry.COLUMN_NAME_GIPFEL
		};
		// String für die Where Clause
		String whereClause =
			    KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ?" +
			    " AND " + KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ?";
		// String für die Where Options
		String[] whereOptions = {
			    getGipfel(),
			    stringSet
		};
		
		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection,           // The columns to return
		    whereClause,                                // The columns for the WHERE clause
		    whereOptions,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );

		int cursorLaenge=c.getCount();
		if (cursorLaenge<1) {
			AutoCompleteTextView gipfel = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGipfel);
			AutoCompleteTextView wege = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);				
			gipfel.setText("");
			gipfelBekannt=false;
			wege.setText("");
		}
	    c.close();

		setGipfel(stringSet);
		setDropDownMenuGipfel();

		Integer intGipfelnummer=getGipfelnummer();
		if ((intGipfelnummer <= maxGipfel) && (intGipfelnummer != 0)) {
			setGipfel(stringSet, intGipfelnummer.toString());
			AutoCompleteTextView textViewGipfelnummerBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);			
			setGipfelnummerBis(intGipfelnummer+1);
			setDropDownMenuGipfelnummerBis();
			if (maxGipfel < getGipfelnummerBis()) {
				textViewGipfelnummerBis.setText("");
			}

		} else {
			clearGipfelnummerBis();				
			textViewGipfelnummer.setText("");
			setWege();
			setDropDownMenuWege();
			setDropDownMenuGipfel();
		}
		
		setGipfelnummer();
		setDropDownMenuGipfelnummer();
		setDropDownMenuGipfel();
		setDropDownMenuWege();
		
		break;
		

	case COMPLETETEXTVIEWGIPFELNUMMER:
		if (gebietBekannt) {
			setGipfel(getGebiet(), stringSet);
		}
		
		setGipfelnummerBis(stringSet);
		
		setDropDownMenuGipfelnummer();	
		
		setDropDownMenuGipfel();
		

		break;
		
	case COMPLETETEXTVIEWGIPFELNUMMERBIS:
		setDropDownMenuGipfelnummerBis();
		break;
		
	case COMPLETETEXTVIEWGIPFEL:
		gipfelBekannt=true;

	    //Datenbankabfrage In welchem Gebiet ist Gipfel?
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection1 = {
			    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,			    
				KleFuEntry.COLUMN_NAME_GEBIET,
				KleFuEntry._ID
		};
		// String für die Where Clause
		String whereClause1 =
			    KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ?";
		// String für die Where Options
		String[] whereOptions1 = {
			    stringSet
		};
		
		// Gebiete aus Datenbank lesen
		Cursor c1 = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection1,           // The columns to return
		    whereClause1,                                // The columns for the WHERE clause
		    whereOptions1,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );

		String gebiet="";
		String gipfelnummer1="";
		String gipfelId="1";
		int cursorLaenge1=c1.getCount();
			if (!(cursorLaenge1<1)) {
			// String aus Cursor lesen
			c1.moveToFirst();
	        gipfelnummer1=c1.getString(0);
	        gebiet=c1.getString(1);
	        gipfelId=c1.getString(2);
		}
	    c1.close();
	        	    
	    //Weg löschen
	  	AutoCompleteTextView wege = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewWeg);
	  	wege.setText("");
		setWege(gipfelId);
		setDropDownMenuWege();			  			
	    
	    AutoCompleteTextView textViewGebiet = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewGebiet);
	    textViewGebiet.setText(gebiet);
		gebietBekannt=true;

	    setGipfel(gebiet);
	    setDropDownMenuGipfel();
		
	    maxGipfel = KleFuContract.getMaxGipfel(gebiet);
	    setGipfelnummer(); //???
	    
	    textViewGipfelnummer.setText(gipfelnummer1);
	    setGipfelnummerBis(gipfelnummer1);		    
	    setDropDownMenuGipfelnummer();

	    break;
	case COMPLETETEXTVIEWWEGE:
		Cursor c2=null;
		schwierigkeit = new Schwierigkeit(0);
		try {
//		weg = stringSet;
		//Datenbankabfrage welche Schwierigkeit der Weg besitzt
		//Datenbankabfrage, ob eingetragener Gipfel Element des Gebietes, wenn nicht dann löschen
		//Datenbankabfrage nach der Beschreibung des Weges
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection2 = {
			    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF,
//			    KleFuEntry.COLUMN_NAME_BESCHREIBUNG,
//			    KleFuEntry.COLUMN_NAME_ERSTBEGEHER1,
//			    KleFuEntry.COLUMN_NAME_ERSTBEGEHER2,
//			    KleFuEntry.COLUMN_NAME_ERSTBEGEHER3,
//			    KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE,
//			    KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM
		};
		// String für die Where Clause
		String whereClause2 = KleFuEntry.COLUMN_NAME_WEGNAME + " LIKE ? AND "
				+ KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ?";
		// String für die Where Options
		String[] whereOptions2 = {
			    stringSet,
			    getGipfel()
		};
		
		// Gebiete und Beschreibung aus Datenbank lesen
		c2 = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
		    projection2,           // The columns to return
		    whereClause2,                                // The columns for the WHERE clause
		    whereOptions2,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );

		c2.moveToFirst();
        schwierigkeit = new Schwierigkeit(c2.getInt(0));
//        beschreibung = c2.getString(1);
//        String erstbegeher1 = c2.getString(2);
//        String erstbegeher2 = c2.getString(3);
//        String erstbegeher3 = c2.getString(4);
//        String erstbegeherAndere = c2.getString(5);
//        erstbegehungsdatum = c2.getString(6);
//        erstbegeher = getErstbegeherString(erstbegeher1, erstbegeher2, erstbegeher3, erstbegeherAndere);
        c2.close();        
		} catch (Exception e) {
	        c2.close();
		}
	    AutoCompleteTextView textViewSchwierigkeit = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
	    textViewSchwierigkeit.setText(schwierigkeit.getSchwierigkeitString());
	    
		if (schwierigkeit.getSchwierigkeitInt() != KleFuEntry.MAXSCHWIERIGKEIT) {
			schwierigkeitBisEnabled=true;
			setSchwierigkeitBis(schwierigkeit);
			setDropDownMenuSchwierigkeitBis();
			AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
			textViewSchwierigkeitBis.setClickable(true);
			textViewSchwierigkeitBis.setFocusable(true);
			textViewSchwierigkeitBis.setFocusableInTouchMode(true);
			if (schwierigkeit.getSchwierigkeitInt() >= getSchwierigkeitBisInt()) {
				textViewSchwierigkeitBis.setText("");
			}
		} else {
			clearSchwierigkeitBis();
		}
	    
//		Button buttonKurzansicht = (Button) rootView.findViewById(R.id.ButtonWegKurzansicht);
//		buttonKurzansicht.setVisibility(View.VISIBLE);
		break;
	case COMPLETETEXTVIEWSCHWIERIGKEIT:
		schwierigkeitBisEnabled=true;
		schwierigkeit=new Schwierigkeit(stringSet);
		schwierigkeitBisEnabled=true;
		if (schwierigkeit.getSchwierigkeitInt() != KleFuEntry.MAXSCHWIERIGKEIT) {
			setSchwierigkeitBis(schwierigkeit);
			setDropDownMenuSchwierigkeitBis();
			AutoCompleteTextView textViewSchwierigkeitBis = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis);
			textViewSchwierigkeitBis.setClickable(true);
			textViewSchwierigkeitBis.setFocusable(true);
			textViewSchwierigkeitBis.setFocusableInTouchMode(true);
			if (schwierigkeit.getSchwierigkeitInt() >= getSchwierigkeitBisInt()) {
				textViewSchwierigkeitBis.setText("");
			}
		} else {
			clearSchwierigkeitBis();
		}
		break;
	case COMPLETETEXTVIEWSCHWIERIGKEITBIS:
		setDropDownMenuSchwierigkeitBis();			
		break;
	}	
}

@Override
public Boolean getGebietBekannt() {
	return gebietBekannt;
}

@Override
public Boolean getGipfelBekannt() {
	return gipfelBekannt;
}

@Override
public String getWegname() {
	return getWeg();
}

	
}


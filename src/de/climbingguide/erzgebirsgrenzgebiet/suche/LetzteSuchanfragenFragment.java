package de.climbingguide.erzgebirsgrenzgebiet.suche;

import java.util.ArrayList;

import net.sqlcipher.Cursor;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class LetzteSuchanfragenFragment extends Fragment implements Sucher {
	
	public LetzteSuchanfragenFragment () {}
	
	private Suchanfrage suchanfrage;
	private Sucher thisSucher;
	private static Activity thisActivity;
	protected static StringListAdapterLetzteSuchanfragen adapterListView;

	//Suche Zeug
	public SucheHandler sucheHandler;
	@Override 
	public SucheHandler getSucheHandler() { return sucheHandler; }	
	private SucheThread sucheThread;
	@Override
	public SucheThread getSucheThread() { return sucheThread; }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisSucher = this;
		thisActivity = getActivity();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_suche_letzte_anfragen, container, false);
        
	     //Letzte Suchanfragen anzeigen
    	//Suchanfragen aus Speicher holen
    try {
     ListView listView = (ListView) rootView.findViewById(R.id.listViewLetzteSuchanfragen);
	 ArrayList<Suchanfrage> arrayListSuchanfrage;

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

	arrayListSuchanfrage = new ArrayList<Suchanfrage>();		 
	
	if (c.getCount() > 0) {

		 c.moveToLast();
			while (c.isBeforeFirst() == false) {
				arrayListSuchanfrage.add(new Suchanfrage(
						c.getString(0),
						c.getInt(1),
						c.getInt(2),
						c.getString(3),
						c.getString(4),
						new Schwierigkeit(c.getInt(5)),
						new Schwierigkeit(c.getInt(6)),
						c.getInt(7)>0,
						c.getInt(8)>0)
				);
				
				c.moveToPrevious();
			}		 
	 	}
	c.close();
	 adapterListView = new StringListAdapterLetzteSuchanfragen(arrayListSuchanfrage);
	 listView.setAdapter(adapterListView);
	 listView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long arg3) {
			suchanfrage = (Suchanfrage) parent.getItemAtPosition(position);
	        sucheHandler = new SucheHandler(getActivity(), thisSucher, sucheThread);	        
	    	sucheThread = new SucheThread(getActivity(), thisSucher);            	
	        sucheThread.start(); 
		}
	});
    } catch (Exception e) {
//    	if (arrayListSuchanfrage.size() < 0) {
        	ListView listView = (ListView) rootView.findViewById(R.id.listViewLetzteSuchanfragen);
        	ArrayList<String> arrayListKeineSucheErfolgt = new ArrayList<String>();
        	arrayListKeineSucheErfolgt.add(getString(R.string.keine_suche_erfolgt));
        	StringListAdapterLeft adapter = new StringListAdapterLeft(getActivity().getApplicationContext(),  arrayListKeineSucheErfolgt);   		 
   		 	listView.setAdapter(adapter);  
//    	}
    }
        
        return rootView;
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
	
	public static void notifyDataSetChanged() {
		try {			
		  thisActivity.runOnUiThread(new Runnable() {
		     public void run() {
		    	if (adapterListView != null)
		    		adapterListView.notifyDataSetChanged();
		    }
		});		
		} catch (Exception e) {
			
		} 		
	}
}

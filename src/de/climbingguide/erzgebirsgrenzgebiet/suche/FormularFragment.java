package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class FormularFragment extends Fragment implements Sucher{

	public FormularFragment () {}

	protected static boolean afterPageChange=false;
	
	private final int ID_GEBIET = 0;
	private final int ID_GIPFEL = 1;
	private final int ID_HOLE_GIPFEL = 2;
	private final int ID_WEGE = 4;
	
	private AutoCompleteTextView actvGebiet;
	private AutoCompleteTextView actvGipfel;
	private AutoCompleteTextView actvGipfelnummerVon;
	private AutoCompleteTextView actvGipfelnummerBis;
	private AutoCompleteTextView actvWege;
	private AutoCompleteTextView actvSchwierigkeitVon;
	private AutoCompleteTextView actvSchwierigkeitBis;
	
	private CursorAdapterGebiete adapterGebiet;
	private CursorAdapterGipfel adapterGipfel;
	private GipfelnummerAdapter adapterGipfelnummer;
	private GipfelnummerAdapter adapterGipfelnummerBis;
	private CursorAdapterWege adapterWege;
	private AdapterSchwierigkeit adapterSchwierigkeitVon;
	private AdapterSchwierigkeit adapterSchwierigkeitBis;
	
	private CursorLoaderGipfel loaderGipfel;
	private CursorLoaderGebiet loaderGebiet;
	private CursorLoaderWege loaderWege;
	
	private LoaderCallbacks<Cursor> loaderCallbacksGipfel;
	private LoaderCallbacks<Cursor> loaderCallbacksGebiet;
	private LoaderCallbacks<Cursor> loaderCallbacksWege;
	
	private View rootView;
	
	protected boolean gebietIsKnown;
	protected boolean gipfelIsKnown;
	
	DialogInterface.OnClickListener okListener =
	new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}	
	};

	protected ActionBarAppActivity thisActivity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity=(ActionBarAppActivity)getActivity();
		gebietIsKnown=false;
		gipfelIsKnown=false;
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
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.activity_suche_formular, container, false);
	    
        initACTVs();
        initAdapters();
        initACTVGebiet();		
        initACTVGipfel();
        initACTVGipfelnummerVon();
        initACTVGipfelnummerBis();
        initACTVWege();
        initACTVSchwierigkeitVon();
        initACTVSchwierigkeitBis();
        initButtons();        
        
        return rootView;
    }

	private void initACTVs() {
		actvGipfelnummerBis=(AutoCompleteTextView)rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummerBis);
		actvGipfelnummerVon=(AutoCompleteTextView)rootView.findViewById(R.id.AutoCompleteTextViewGipfelnummer);
        actvGipfel = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextViewGipfel);
		actvGebiet = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextViewGebiet);
		actvWege = (AutoCompleteTextView)rootView.findViewById(R.id.AutoCompleteTextViewWeg);
		actvSchwierigkeitVon = (AutoCompleteTextView)rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitVon);
		actvSchwierigkeitBis = (AutoCompleteTextView)rootView.findViewById(R.id.AutoCompleteTextViewSchwierigkeitBis); 				
	}

	private void initAdapters() {
		
		adapterGipfelnummerBis=new GipfelnummerAdapter();
		adapterGipfelnummer=new GipfelnummerAdapter();
		adapterGipfel=new CursorAdapterGipfel();
        adapterGebiet=new CursorAdapterGebiete();
        adapterWege=new CursorAdapterWege();
        adapterSchwierigkeitVon=new AdapterSchwierigkeit();
        adapterSchwierigkeitBis=new AdapterSchwierigkeit();
	}

	private void initACTVSchwierigkeitBis() {
		actvSchwierigkeitBis.setAdapter(adapterSchwierigkeitBis);
		actvSchwierigkeitBis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvSchwierigkeitBis.showDropDown();
			}
		});
		actvSchwierigkeitBis.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) actvSchwierigkeitBis.showDropDown();
			}
		});
	}
	
	private void initACTVSchwierigkeitVon() {
		actvSchwierigkeitVon.setAdapter(adapterSchwierigkeitVon);
		actvSchwierigkeitVon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvSchwierigkeitVon.showDropDown();
			}
		});
		actvSchwierigkeitVon.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) actvSchwierigkeitVon.showDropDown();
			}
		});
		actvSchwierigkeitVon.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Integer schwierigkeit = Schwierigkeit.SchwierigkeitStrToInt((String)parent.getItemAtPosition(position));
				setupSchwierigkeitBis(schwierigkeit);
			}
		});
	}	

	private void initACTVWege() {
		actvWege.setAdapter(adapterWege);
		actvWege.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {actvWege.showDropDown();}
		});
		actvWege.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) actvWege.showDropDown();
			}
		});
		
		thisActivity.getSupportLoaderManager().initLoader(ID_WEGE, null, new LoaderCallbacks<Cursor>() {

			@Override
			public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
				loaderCallbacksWege=this;
				loaderWege=new CursorLoaderWege(thisActivity);
				return loaderWege;
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
				adapterWege.changeCursor(cursor);
			}

			@Override
			public void onLoaderReset(Loader<Cursor> arg0) {
				adapterWege.changeCursor(null);				
			}
		});
		
	}	
	
	private void initACTVGipfelnummerBis() {
		actvGipfelnummerBis.setAdapter(adapterGipfelnummerBis);
		actvGipfelnummerBis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvGipfelnummerBis.showDropDown();
			}
		});
		actvGipfelnummerBis.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				actvGipfelnummerBis.showDropDown();
			}
		});
	}

	private void initACTVGipfelnummerVon() {
		actvGipfelnummerVon.setAdapter(adapterGipfelnummer);
        actvGipfelnummerVon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvGipfelnummerVon.showDropDown();
			}
		}); 
        actvGipfelnummerVon.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) actvGipfelnummerVon.showDropDown();
			}
		});
		actvGipfelnummerVon.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Integer gipfelnummer = (Integer)parent.getItemAtPosition(position);
				setupGipfelnummerBis(gipfelnummer);
				if (gebietIsKnown) {
					Bundle bundle = new Bundle();
					bundle.putString(KleFuEntry.COLUMN_NAME_GEBIET, actvGebiet.getText().toString());
					bundle.putString(KleFuEntry.COLUMN_NAME_GIPFELNUMMER, gipfelnummer.toString());
					thisActivity.getSupportLoaderManager().initLoader(ID_HOLE_GIPFEL, bundle, new LoaderCallbacks<Cursor>() {

						@Override
						public Loader<Cursor> onCreateLoader(int id,
								Bundle bundle) {
							String gebiet = bundle.getString(KleFuEntry.COLUMN_NAME_GEBIET);
							String gipfelnummer = bundle.getString(KleFuEntry.COLUMN_NAME_GIPFELNUMMER);
							return new CursorLoaderHoleGipfel(ActionBarAppActivity.getInstance(), gebiet, gipfelnummer);
						}

						@Override
						public void onLoadFinished(Loader<Cursor> arg0,
								Cursor cursor) {
							cursor.moveToFirst();
							actvGipfel.setText(cursor.getString(0));
						}

						@Override
						public void onLoaderReset(Loader<Cursor> arg0) {
							
						}
					});
				}
			}
		});
	}

	private void initACTVGipfel() {
        actvGipfel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvGipfel.showDropDown();
			}
		});
        actvGipfel.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					actvGipfel.showDropDown();
				}
			}
		});
        
        actvGipfel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Cursor c = (Cursor)((CursorAdapterGipfel)parent.getAdapter()).getItem(position);
				c.moveToPosition(position);
				String gebiet = c.getString(c.getColumnIndex(KleFuEntry.COLUMN_NAME_GEBIET));
//				String gipfel = c.getString(c.getColumnIndex(KleFuEntry.COLUMN_NAME_GIPFEL));
				Integer gipfelnummer = c.getInt(c.getColumnIndex(KleFuEntry.COLUMN_NAME_GIPFELNUMMER));
				actvGebiet.setText(gebiet);
				thisActivity.getSupportLoaderManager().restartLoader(ID_GEBIET, null, loaderCallbacksGebiet);
				loaderGipfel.setGebietSelected(gebiet);
				adapterGipfel.setGebietSelected(gebiet);
				thisActivity.getSupportLoaderManager().restartLoader(ID_GIPFEL, null, loaderCallbacksGipfel);
				gebietIsKnown=true;
				gipfelIsKnown=true;
				actvGipfelnummerVon.setText(gipfelnummer.toString());
				setupGipfelnummerBis(gipfelnummer);
				
				final Integer gipfelId = c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GIPFELID));
				loaderWege.setGipfelId(gipfelId);
				adapterWege.setGipfelId(gipfelId);
				thisActivity.getSupportLoaderManager().restartLoader(ID_WEGE, null, loaderCallbacksWege);
			}
		});
                
		thisActivity.getSupportLoaderManager().initLoader(ID_GIPFEL, null, new LoaderCallbacks<Cursor>() {

			@Override
			public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {				
				loaderGipfel = new CursorLoaderGipfel(getActivity());
				loaderCallbacksGipfel=this;
				return loaderGipfel;
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
				adapterGipfel.changeCursor(cursor);
		        actvGipfel.setAdapter(adapterGipfel);
			}

			@Override
			public void onLoaderReset(Loader<Cursor> arg0) {
				adapterGipfel.changeCursor(null);
			}
		});//Loader initialisieren
	}

	private void initACTVGebiet() {
		actvGebiet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvGebiet.showDropDown();
			}
		});
		actvGebiet.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					actvGebiet.showDropDown();				
				} else {
					Cursor c = KleFuEntry.db.rawQuery("SELECT 1 FROM "+KleFuEntry.TABLE_NAME_GEBIETE+" WHERE "+KleFuEntry.COLUMN_NAME_GEBIET+"=?", new String[] { actvGebiet.getText().toString() });
					if (!c.moveToFirst()) {
						actvGebiet.setText("");
						adapterGipfelnummer.setGebiet(null);
						adapterGipfelnummerBis.setGebiet(null);
						Toast.makeText(thisActivity, R.string.fehlerhafte_eingabe, Toast.LENGTH_LONG).show();
					}
					c.close();
				}
			}
		});
        
        actvGebiet.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Cursor c = (Cursor)((CursorAdapterGebiete)parent.getAdapter()).getItem(position);
				c.moveToPosition(position);
				String gebiet = c.getString(c.getColumnIndex(KleFuEntry.COLUMN_NAME_GEBIET));
				loaderGipfel.setGebietSelected(gebiet);
				adapterGipfel.setGebietSelected(gebiet);
				thisActivity.getSupportLoaderManager().restartLoader(ID_GIPFEL, null, loaderCallbacksGipfel);
				adapterGipfelnummer.setGebiet(gebiet);
				adapterGipfelnummerBis.setGebiet(gebiet);
				gebietIsKnown=true;
			}
		});

        thisActivity.getSupportLoaderManager().initLoader(ID_GEBIET, null, new LoaderCallbacks<Cursor>() {

			@Override
			public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
				loaderCallbacksGebiet=this;
				loaderGebiet = new CursorLoaderGebiet(getActivity());
				return loaderGebiet;
			}

			@Override
			public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
				adapterGebiet.changeCursor(cursor);
		        actvGebiet.setAdapter(adapterGebiet);
			}

			@Override
			public void onLoaderReset(Loader<Cursor> arg0) {
				adapterGebiet.changeCursor(null);
			}
		});//Loader initialisieren
	}

	private void initButtons() {
		Button buttonClear = (Button)rootView.findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClearClick(v);				
			}
		});
		Button buttonSucheStarten = (Button)rootView.findViewById(R.id.ButtonStarteSuche);
		buttonSucheStarten.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonSucheClick();
			}
		});
	}	
	
private void setupGipfelnummerBis(Integer gipfelnummer) {
		if (gipfelnummer < adapterGipfelnummer.getEndNummer()) {
			adapterGipfelnummerBis.setStartNummer(gipfelnummer+1);
			if (gebietIsKnown) adapterGipfelnummerBis.setGebiet(actvGebiet.getText().toString());
			actvGipfelnummerBis.setClickable(true);
			actvGipfelnummerBis.setFocusable(true);
			actvGipfelnummerBis.setFocusableInTouchMode(true);
		}
	}

private void setupSchwierigkeitBis(Integer schwierigkeit) {
	if (schwierigkeit < KleFuEntry.MAXSCHWIERIGKEIT) {
		if (schwierigkeit==KleFuEntry.MAXSCHWIERIGKEITSPRUNG) {
			schwierigkeit+=KleFuEntry.MAXSCHWIERIGKEITLUECKE;
		} else {
			schwierigkeit++;
		}
		adapterSchwierigkeitBis.setStartSchwierigkeit(schwierigkeit);
		actvSchwierigkeitBis.setClickable(true);
		actvSchwierigkeitBis.setFocusable(true);
		actvSchwierigkeitBis.setFocusableInTouchMode(true);
	}
}

public void onClearClick(View v) {
    clear();
}

private void clear() {
	 gebietIsKnown=false;
	 gipfelIsKnown=false;

	 actvGebiet.setText("");
	 thisActivity.getSupportLoaderManager().restartLoader(ID_GEBIET, null, loaderCallbacksGebiet);
	 actvGipfel.setText("");
	 loaderGipfel.setGebietSelected(null);
	 adapterGipfel.setGebietSelected(null);
	 thisActivity.getSupportLoaderManager().restartLoader(ID_GIPFEL, null, loaderCallbacksGipfel);
	 actvGipfelnummerVon.setText("");
	 adapterGipfelnummer.setGebiet(null);
	 actvGipfelnummerBis.setText("");
	 adapterGipfelnummerBis.setGebiet(null);
	 actvGipfelnummerBis.setClickable(false);
	 actvGipfelnummerBis.setFocusable(false);
	 actvGipfelnummerBis.setFocusableInTouchMode(false);
	 actvSchwierigkeitBis.setText("");
	 actvSchwierigkeitBis.setClickable(false);
	 actvSchwierigkeitBis.setFocusable(false);
	 actvSchwierigkeitBis.setFocusableInTouchMode(false);
	 actvWege.setText("");
	 loaderWege.setGipfelId(null);
	 adapterWege.setGipfelId(null);
	 thisActivity.getSupportLoaderManager().restartLoader(ID_WEGE, null, loaderCallbacksWege);
	 
	 CheckBox checkBoxSchonGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxGeklettert);
	 checkBoxSchonGeklettert.setChecked(true);

	 CheckBox checkBoxNochNichtGeklettert = (CheckBox) rootView.findViewById(R.id.checkBoxNochNichtGeklettert);
	 checkBoxNochNichtGeklettert.setChecked(true);
}

static class CursorLoaderGipfel extends CursorLoader{
	
	private static String gebietSelected;
	
    public CursorLoaderGipfel(Context context) {
        super(context);
     }
     
     public void setGebietSelected(String gebiet) {
		gebietSelected=gebiet;		
	}

	@Override
     public Cursor loadInBackground() {
 		String sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_GIPFEL;
 		sqlQuery += ", "+KleFuEntry.COLUMN_NAME_GIPFELNUMMER;
 		sqlQuery += ", "+KleFuEntry.COLUMN_NAME_GEBIET;
 		sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_GIPFEL;
 		if (gebietSelected!=null) {
 			sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GEBIET+" LIKE '"+gebietSelected+"'";
 		} 		
 		sqlQuery += " ORDER BY ";
 		if (gebietSelected!=null) {
 			sqlQuery += KleFuEntry.COLUMN_NAME_GIPFELNUMMER;
 		} else { 
 			sqlQuery += KleFuEntry.COLUMN_NAME_GIPFEL;
 		}
 		
 		return KleFuEntry.db.rawQuery(sqlQuery, null);
     }            
    }

static class CursorLoaderGebiet extends CursorLoader{
	   
    public CursorLoaderGebiet(Context context) {
        super(context);
     }
     
     @Override
     public Cursor loadInBackground() {
 		String sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_GEBIET;
 		sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_GEBIETE;
 		sqlQuery += " ORDER BY "+KleFuEntry.COLUMN_NAME_GEBIET;
 		           
 		return KleFuEntry.db.rawQuery(sqlQuery, null);
     }            
    }

static class CursorLoaderHoleGipfel extends CursorLoader{
	
	private String gebiet;
	private String gipfelnummer;
	
    public CursorLoaderHoleGipfel(Context context, String gebiet, String gipfelnummer) {
        super(context);
        this.gebiet=gebiet;
        this.gipfelnummer=gipfelnummer;
     }
     
     @Override
     public Cursor loadInBackground() {
 		String sqlQuery  = " SELECT "+KleFuEntry.COLUMN_NAME_GIPFEL;
 		sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_GIPFEL;
 		sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GEBIET+" LIKE ?";
 		sqlQuery += " AND "+KleFuEntry.COLUMN_NAME_GIPFELNUMMER+" LIKE ?";
 		
 		String[] args = { gebiet, gipfelnummer };
 		return KleFuEntry.db.rawQuery(sqlQuery, args);
     }            
    }

static class CursorLoaderWege extends CursorLoader{
	
	private static Integer gipfelId;
	
    public CursorLoaderWege(Context context) {
        super(context);
     }
     
     @SuppressWarnings("static-access")
	public void setGipfelId(Integer gipfelId) {
    	 this.gipfelId=gipfelId;		
	}

	@Override
     public Cursor loadInBackground() {
 		String sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_WEGNAME+", "
 			+KleFuEntry.COLUMN_NAME_GEKLETTERT+", "
 			+KleFuEntry.COLUMN_NAME_STERN+", "
 			+KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP+", "
 			+KleFuEntry.COLUMN_NAME_OU+", "
 			+KleFuEntry.COLUMN_NAME_RP+", "
 			+KleFuEntry.COLUMN_NAME_GIPFELID; 			
 		sqlQuery +="COUNT("+KleFuEntry.COLUMN_NAME_WEGNAME+") AS "+KleFuEntry.NUMBERS;
 		if (gipfelId!=null) {
 			sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GIPFELID+" LIKE "+gipfelId;
 		} 		
 		sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_WEGE;
 		sqlQuery += " GROUP BY "+KleFuEntry.COLUMN_NAME_WEGNAME;
 		sqlQuery += " ORDER BY "+KleFuEntry.COLUMN_NAME_WEGNAME;
 		
 		return KleFuEntry.db.rawQuery(sqlQuery, null);
     }            
    }

//Suche Zeug
public SucheHandler sucheHandler;
@Override 
public SucheHandler getSucheHandler() { return sucheHandler; }	
private SucheThread sucheThread;
@Override
public SucheThread getSucheThread() { return sucheThread; }

public void buttonSucheClick() {		
    sucheHandler = new SucheHandler(getActivity(), this, sucheThread);	        
	sucheThread = new SucheThread(getActivity(), this);            	
    sucheThread.start();
}	

@Override
public Boolean getGebietBekannt() {
	return gebietIsKnown;
}

@Override
public String getGebiet() {
	return actvGebiet.getText().toString();
}

@Override
public String getGipfel() {
	return actvGebiet.getText().toString();
}

@Override
public String getWegname() {
	return actvWege.getText().toString();
}

@Override
public Integer getGipfelnummer() {
	Integer gipfelnummer;
	try {
		gipfelnummer=Integer.parseInt(actvGipfelnummerVon.getText().toString());
	} catch (Exception e) {
		gipfelnummer=0;		
	}
	return gipfelnummer;
}

@Override
public Integer getGipfelnummerBis() {
	try {
		return Integer.parseInt(actvSchwierigkeitBis.getText().toString());
	}
	catch (Exception e) {
		return 0;
	}
}

@Override
public Boolean getGipfelBekannt() {
	return gipfelIsKnown;
}

@Override
public Integer getSchwierigkeitBisInt() {
	return Schwierigkeit.SchwierigkeitStrToInt(actvSchwierigkeitBis.getText().toString());
}

@Override
public Integer getSchwierigkeitVonInt() {
	return Schwierigkeit.SchwierigkeitStrToInt(actvSchwierigkeitVon.getText().toString());
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
}


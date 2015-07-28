package de.climbingguide.erzgebirsgrenzgebiet;

import java.util.ArrayList;

import net.sqlcipher.Cursor;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;

public class EigenerWeg extends Weg {
	private Integer eigeneWegId;
	private boolean rp;
	private boolean vorstieg;
	private boolean oU;
	private long datum;
	private String geklettertMit;
	private String bemerkungen;
	private boolean isExpanded;
	
    private ArrayList<OnRemoveItemListener> removeListeners = new ArrayList<OnRemoveItemListener> ();	
	
//	public EigenerWeg(int eigeneWegId, int wegeId) {
//		this.eigeneWegId = eigeneWegId;
//		setWegeid(wegeId);
//	}
	
	public EigenerWeg(int eigeneWegId, int wegId, boolean vorstieg, boolean rp, boolean oU,
			long datum,	String geklettertMit, String bemerkungen,
			boolean isExpanded) {
		super(wegId);
		this.setEigeneWegId(eigeneWegId);
		this.vorstieg = vorstieg;
		this.rp = rp;
		this.oU = oU;
		this.datum = datum;
		this.geklettertMit = geklettertMit;
		this.bemerkungen = bemerkungen;
		this.isExpanded = isExpanded;
	}

	public EigenerWeg(Weg childWeg, Integer id, boolean isVorgestiegen,
			boolean isRP, boolean isoU, Boolean isExpanded2,
			String geklettertMit, String bemerkungen,
			long currenttimemillis) {
		super(childWeg.getWegeid(), childWeg.getGipfelId(), childWeg.getWegname(),
				childWeg.getBeschreibung(), childWeg.getSchwierigkeit_af(), childWeg.getSchwierigkeit_oU(),
				childWeg.getSchwierigkeit_RP(), childWeg.getErstbegeher1(), childWeg.getErstbegeher2(),
				childWeg.getErstbegeher3(), childWeg.getErstbegeherandere(), childWeg.getErstbegehungsdatum(),
				childWeg.getStern(), childWeg.getAusrufezeichen(), childWeg.isGeklettert());
		this.eigeneWegId=id;
		this.vorstieg = isVorgestiegen;
		this.rp = isRP;
		this.oU = isoU;
		this.datum = currenttimemillis;
		this.geklettertMit = geklettertMit;
		this.bemerkungen = bemerkungen;
		this.isExpanded=isExpanded2;
		super.oU = isoU;
		super.rp = isRP;
		super.vorgestiegen = isVorgestiegen;
	}
	
	public EigenerWeg() {}

	public Integer getEigeneWegId() {
		return eigeneWegId;
	}

	public void setEigeneWegId(int eigeneWegId) {
		this.eigeneWegId = eigeneWegId;
	}

	public boolean isRP() {
		return rp;
	}

	public void setRP(boolean rp) {
		this.rp = rp;
		
		//Datenbankupdate der eigenen Wegeliste
		ContentValues values = new ContentValues(1);
		String whereClause = KleFuEntry._ID + " LIKE ?";
		String[] whereArgs = { eigeneWegId.toString() };
		values.put(KleFuEntry.COLUMN_NAME_RP, rp);
		KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				values, whereClause, whereArgs);
		
		//Datenbankupdate der Wegedatenbank
		if (rp) {
			super.setRP(true, false);
			setVorstieg(true);
			setoU(true);
		} else {
			//Überprüfen, ob in eigener Wegeliste Weg RP worden ist,
			//wenn ja RP argument entfernen
			String[] projection2 = { KleFuEntry._ID };
			whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND " +
					KleFuEntry.COLUMN_NAME_RP + " LIKE ?";
			String[] whereArgs2 = { getWegeid().toString(), "1" };
			Cursor c = KleFuEntry.db.query(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
					projection2,
					whereClause, whereArgs2, null, null, null);
			boolean wegIsNotRP;
			wegIsNotRP = c.getCount()<=0;
			c.close();
			if (wegIsNotRP) super.setRP(false, false);			
		}
	}

	public boolean isVorstieg() {
		return vorstieg;
	}

	public void setVorstieg(boolean vorstieg) {
		this.vorstieg = vorstieg;
		
		//Datenbankupdate der eigenen Wegeliste
		ContentValues values = new ContentValues(1);
		String whereClause = KleFuEntry._ID + " LIKE ?";
		String[] whereArgs = { eigeneWegId.toString() };
		values.put(KleFuEntry.COLUMN_NAME_VORSTIEG, vorstieg);
		KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				values, whereClause, whereArgs);
		
		//Datenbankupdate der Wegedatenbank
		if (vorstieg) {
			setVorgestiegen(vorstieg, false);
		} else {
			//Überprüfen, ob in eigener Wegeliste Weg vorgestiegen worden ist,
			//wenn ja vorstiegsargument entfernen und Gipfel auf Vorgestiegen prüfen
			String[] projection2 = { KleFuEntry._ID };
			whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND " +
					KleFuEntry.COLUMN_NAME_VORSTIEG + " LIKE ?";
			String[] whereArgs2 = { getWegeid().toString(), "1" };
			Cursor c = KleFuEntry.db.query(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
					projection2,
					whereClause, whereArgs2, null, null, null);
			boolean wegIsNotVorgestiegen;
			wegIsNotVorgestiegen = c.getCount()<=0;
			c.close();
			if (wegIsNotVorgestiegen) setVorgestiegen(false, false);
			
			setRP(false);
		}
	}
	
	public boolean isoU() {
		return oU;
	}

	public void setoU(boolean oU) {
		this.oU = oU;
		
		//Datenbankupdate der eigenen Wegeliste
		ContentValues values = new ContentValues(1);
		String whereClause = KleFuEntry._ID + " LIKE ?";
		String[] whereArgs = { eigeneWegId.toString() };
		values.put(KleFuEntry.COLUMN_NAME_OU, oU);
		KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				values, whereClause, whereArgs);
		
		//Datenbankupdate der Wegedatenbank
		if (oU) {
			super.setoU(true, false);
		} else {
			//Überprüfen, ob in eigener Wegeliste Weg oU worden ist,
			//wenn ja oU argument entfernen
			String[] projection2 = { KleFuEntry._ID };
			whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND " +
					KleFuEntry.COLUMN_NAME_OU + " LIKE ?";
			String[] whereArgs2 = { getWegeid().toString(), "1" };
			Cursor c = KleFuEntry.db.query(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
					projection2,
					whereClause, whereArgs2, null, null, null);
			boolean wegIsNotOU;
			wegIsNotOU = c.getCount()<=0;
			c.close();
			if (wegIsNotOU) super.setoU(false, false);			
		}
	}

	public long getDatum() {
		return datum;
	}
	
	public String getDatumString() {
		return ClimbingGuideApplication.getDate(datum);		
	}

	public void setDatum(long datum) {
		this.datum = datum;
	}

	public String getGeklettertMit() {
		return geklettertMit;
	}

	public void setGeklettertMit(String geklettertMit) {
		this.geklettertMit = geklettertMit;
	}

	public String getBemerkungen() {
		return bemerkungen;
	}

	public void setBemerkungen(String bemerkungen) {
		this.bemerkungen = bemerkungen;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		if (this.isExpanded != isExpanded) {
			this.isExpanded = isExpanded;
			ContentValues values = new ContentValues();
			values.put(KleFuEntry.COLUMN_NAME_IS_EXPANDED, isExpanded());
			String whereClause=KleFuEntry._ID + " LIKE ?";
			String[] whereArgs = { (getEigeneWegId().toString() ) }; 
			KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
					values,
					whereClause,
					whereArgs);
		}
	}
	
	public void swapExpanded() {
		setExpanded(!isExpanded);
	}
	
	public void remove(final BaseExpandableListAdapter adapter) {
		final Context context = ActionBarAppActivity.getInstance();
	   	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setTitle(context.getString(R.string.eintrag_loeschen));
		String messageString=context.getString(R.string.eintrag_loeschen_long);
		builder.setMessage(messageString);
		builder.setIcon(R.drawable.ic_action_questionmark);
		builder.setPositiveButton(R.string.ja,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					String whereClause = KleFuEntry._ID + " LIKE ?";
					String[] whereArgs = { getEigeneWegId().toString() };
					KleFuEntry.db.delete(
							KleFuEntry.TABLE_NAME_EIGENE_WEGE,
							whereClause ,
							whereArgs);
					Toast.makeText(context, context.getString(R.string.eintrag) + " " + context.getString(R.string.eintraege_geloescht), Toast.LENGTH_LONG).show();
					for (OnRemoveItemListener listener : removeListeners) 
					{
					    listener.onRemoveOnwListItem();
					}
				}
			}
		);
		builder.setNegativeButton(R.string.nein,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
    		}
		);
		builder.show();
	}
	
	public void showTextEditor(final BaseExpandableListAdapter adapter) {
		final Integer id = getEigeneWegId();
		final String personen = getGeklettertMit();
		final String bemerkungen = getBemerkungen();
		final Context context = ActionBarAppActivity.getInstance();
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialogeigenewegedetails,null);
		alert.setView(layout);
		final EditText inputGeklettertMit=(EditText)layout.findViewById(R.id.edit_text_geklettert_mit);
		final EditText inputBemerkungen=(EditText)layout.findViewById(R.id.edit_text_bemerkungen);
		inputGeklettertMit.setText(personen);
		inputBemerkungen.setText(bemerkungen);
//		alert.setTitle(thisActivity.getString(R.string.eigene_wegeliste_aktualisieren));
		alert.setMessage(context.getString(R.string.eigene_wegeliste_aktualisieren));
		alert.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String geklettertMit = inputGeklettertMit.getText().toString();
			String bemerkungen = inputBemerkungen.getText().toString();
			
			ContentValues values = new ContentValues();
			values.put(KleFuEntry.COLUMN_NAME_PERSONEN, geklettertMit);
			values.put(KleFuEntry.COLUMN_NAME_BEMERKUNGEN, bemerkungen);
			String whereClause = KleFuEntry._ID + " LIKE ?";
			String[] whereArgs = { id.toString() };												
			KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
					values, whereClause, whereArgs);
			adapter.notifyDataSetChanged();
			Toast.makeText(context, context.getString(R.string.eintrag_aktualisiert), Toast.LENGTH_LONG).show();
		}
		});
		alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog alertToShow = alert.create();
		alertToShow.getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertToShow.show();		
	}
	
    public interface OnRemoveItemListener 
    {
        void onRemoveOnwListItem();
    }
    
    public void setOnRemoveOwnListItem (OnRemoveItemListener listener) 
    {
        // Store the listener object
        if (!this.removeListeners.contains(listener)) this.removeListeners.add(listener);
    }
	
}

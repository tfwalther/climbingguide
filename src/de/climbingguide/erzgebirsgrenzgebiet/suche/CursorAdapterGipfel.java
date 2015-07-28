package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class CursorAdapterGipfel extends CursorAdapter{

	final private LayoutInflater inflater = LayoutInflater.from(ActionBarAppActivity.getInstance());	
	private String gebietSelected;
	
	public CursorAdapterGipfel() {
		super(ActionBarAppActivity.getInstance(), null, false);	        
	}

	@Override
	public void bindView(View view, Context contex, Cursor c) {
		
		AdapterGipfelHolder adapterGipfelHolder = (AdapterGipfelHolder) view.getTag();
		
		adapterGipfelHolder.gipfelnummer.setText(((Integer)c.getInt(2)).toString());
		adapterGipfelHolder.gipfel.setText(c.getString(1));
        // Gebietname auf 5 Zeichen begrenten
		String gebiet=c.getString(3);
        if (gebiet.length() > 5) {
        	gebiet=gebiet.subSequence(0, 4).toString() + ".";
        }
		adapterGipfelHolder.gebiet.setText(gebiet);		
	}

	@Override
	public View newView(Context context, Cursor c,
			ViewGroup parent) {
		AdapterGipfelHolder adapterGipfelHolder;
		
        View convertView = (View) inflater.inflate(R.layout.drop_down_layout_gipfel, parent, false);
		        
        adapterGipfelHolder = new AdapterGipfelHolder();
        adapterGipfelHolder.gipfel = (TextView) convertView.findViewById(R.id.dropdown_gipfel);
        adapterGipfelHolder.gipfel.setTypeface(null, Typeface.BOLD);		
        adapterGipfelHolder.gebiet = (TextView) convertView.findViewById(R.id.dropdown_gebiet);
        adapterGipfelHolder.gipfelnummer = (TextView) convertView.findViewById(R.id.dropdown_gipfelnummer);
        
        convertView.setTag(adapterGipfelHolder);
        
        bindView(convertView, context, c);
        return convertView;
	}
	
	@Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        Cursor currentCursor = null;
        
        if (getFilterQueryProvider() != null)
        {
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        String args = "";
        
        if (constraint != null)
        {
            args = constraint.toString();      
        }
 
        currentCursor = getStationCursor(args);
 
        return currentCursor;
    }

    @Override
    public String convertToString(Cursor cursor) {
        //returns string inserted into textview after item from drop-down list is selected.
        return cursor.getString(cursor.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GIPFEL));
    }
	
	public Cursor getStationCursor(String args)	{
		String sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_GIPFEL;
		sqlQuery += ", "+KleFuEntry.COLUMN_NAME_GIPFELNUMMER;
		sqlQuery += ", "+KleFuEntry.COLUMN_NAME_GEBIET;
		sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_GIPFEL;
		sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GIPFEL+" LIKE '%" + args + "%' ";
		if (gebietSelected != null) sqlQuery += " AND "+KleFuEntry.COLUMN_NAME_GEBIET+" LIKE ?";
		sqlQuery += " ORDER BY "+KleFuEntry.COLUMN_NAME_GIPFEL;
		
		String[] whereArgs=null;
		if (gebietSelected != null) {
			whereArgs = new String[1];
			whereArgs[0]=gebietSelected;
		}
		return KleFuEntry.db.rawQuery(sqlQuery, whereArgs);
	}

	public String getGebietSelected() {
		return gebietSelected;
	}

	public void setGebietSelected(String gebietSelected) {
		this.gebietSelected = gebietSelected;
	}
}

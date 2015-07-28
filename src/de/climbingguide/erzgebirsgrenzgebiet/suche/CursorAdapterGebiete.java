package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class CursorAdapterGebiete extends CursorAdapter{

	final private LayoutInflater inflater = LayoutInflater.from(ActionBarAppActivity.getInstance());		
	
	public CursorAdapterGebiete() {
		super(ActionBarAppActivity.getInstance(), null, false);	        
	}

	@Override
	public void bindView(View view, Context contex, Cursor c) {
		
		Holder holder = (Holder)view.getTag();
		
		holder.textView.setText(c.getString(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GEBIET)));
	}

	@Override
	public View newView(Context context, Cursor c,
			ViewGroup parent) {		
        View convertView = (View) inflater.inflate(R.layout.drop_down_layout_left, parent, false);
	        
        Holder holder = new Holder();
        holder.textView = (TextView) convertView.findViewById(R.id.textViewProzent);
        
        convertView.setTag(holder);
        
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
        return cursor.getString(cursor.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GEBIET));
    }
	
	public Cursor getStationCursor(String args)
	{      
	 String sqlQuery = "";
	 Cursor result = null;
	    
	 sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_GEBIET;
	 sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_GEBIETE;
	 sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GEBIET+" LIKE '%" + args + "%' ";
	 sqlQuery += " ORDER BY "+KleFuEntry.COLUMN_NAME_GEBIET;
	           
	 result = KleFuEntry.db.rawQuery(sqlQuery, null);
 	 return result;
	}
	
	static class Holder {
		protected TextView textView;
	}
}

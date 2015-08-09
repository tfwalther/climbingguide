package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;

public class CursorAdapterWege extends CursorAdapter {

	final private LayoutInflater inflater = LayoutInflater.from(ActionBarAppActivity.getInstance());
	private Integer gipfelId=null;
	
	public CursorAdapterWege() {
		super(ActionBarAppActivity.getInstance(), null, false);	        
	}

	@Override
	public void bindView(View view, Context arg1, Cursor c) {
		Holder holder = (Holder)view.getTag();
		holder.textViewLeft.setText(Html.fromHtml(Weg.cursorToString(c, true)));
		if (c.getInt(c.getColumnIndexOrThrow(KleFuEntry.NUMBERS))<=1) {
			holder.textViewRight.setText(Html.fromHtml(Weg.cursorToSchwierigkeit(c, true)));
		} else {
			holder.textViewRight.setText(Html.fromHtml("<i>"+c.getString(c.getColumnIndexOrThrow(KleFuEntry.NUMBERS))+"x</i>"));
		}
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
        View convertView = (View) inflater.inflate(R.layout.drop_down_layout_wege, parent, false);
        
        Holder holder = new Holder();
        holder.textViewLeft = (TextView) convertView.findViewById(R.id.textViewLeft);
        holder.textViewRight = (TextView) convertView.findViewById(R.id.textViewRight);
        
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
        return Weg.cursorToString(cursor, false);
    }
	
	public Cursor getStationCursor(String args)
	{      
	 String sqlQuery = "";
	 Cursor result = null;
	 sqlQuery  = " SELECT _id" + ", "+KleFuEntry.COLUMN_NAME_WEGNAME+", "
 			+KleFuEntry.COLUMN_NAME_GEKLETTERT+", "
 			+KleFuEntry.COLUMN_NAME_STERN+", "
 			+KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU+", "
 			+KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP+", "
 			+KleFuEntry.COLUMN_NAME_OU+", "
 			+KleFuEntry.COLUMN_NAME_RP+", "
 			+KleFuEntry.COLUMN_NAME_GIPFELID+", ";
	 sqlQuery +="COUNT("+KleFuEntry.COLUMN_NAME_WEGNAME+") AS "+KleFuEntry.NUMBERS;

 	 sqlQuery += " FROM "+KleFuEntry.TABLE_NAME_WEGE;
	 sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_WEGNAME+" LIKE '%" + args + "%' ";
	 if (gipfelId!=null) {
 		sqlQuery += " WHERE "+KleFuEntry.COLUMN_NAME_GIPFELID+" LIKE "+gipfelId;
 	 }
     sqlQuery += " GROUP BY "+KleFuEntry.COLUMN_NAME_WEGNAME;
	 sqlQuery += " ORDER BY "+KleFuEntry.COLUMN_NAME_WEGNAME;
	           
	 result = KleFuEntry.db.rawQuery(sqlQuery, null);
 	 return result;
	}
	
	public void setGipfelId(Integer gipfelId) {
		this.gipfelId = gipfelId;
	}
	
	static class Holder {
		protected TextView textViewLeft;
		protected TextView textViewRight;
	}
	
}

package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import java.util.ArrayList;

import net.sqlcipher.Cursor;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class StringListAdapterLetzteSuchanfragen extends BaseAdapter {
 
	private final LayoutInflater mLayoutInflater;	
	private ArrayList<Suchanfrage> mData = new ArrayList<Suchanfrage>();
	
	public StringListAdapterLetzteSuchanfragen(ArrayList<Suchanfrage> pData) {
        mData = pData;
        for (Suchanfrage s : mData) {
        	if (!s.getGebiet().equals("")) s.setGebietBekannt(true);
        	if (!s.getGipfel().equals("")) s.setGipfelBekannt(true);
        }
		mLayoutInflater = (LayoutInflater) ClimbingGuideApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	@Override
	public View getView(int pPosition, View convertView, ViewGroup parent) {
        
		AdapterLetzteSuchanfrageHolder adapterLetzteSuchanfrageHolder;
		
		if (convertView == null) {
		        convertView = mLayoutInflater.inflate(R.layout.suchanzeige, null);
		        
		        adapterLetzteSuchanfrageHolder = new AdapterLetzteSuchanfrageHolder();
		        adapterLetzteSuchanfrageHolder.textViewString = (TextView) convertView.findViewById(R.id.textViewProzent);
		        
		       convertView.setTag(adapterLetzteSuchanfrageHolder);
		} else {
			adapterLetzteSuchanfrageHolder = (AdapterLetzteSuchanfrageHolder) convertView.getTag();
		}
		
		// String basteln, der angezeigt werden soll: Gipfel, Weg, (Schwierigkeit)
		
//		TextView textViewString = (TextView) convertView.findViewById(R.id.textView1); 
	
		Suchanfrage suchanfrage = (Suchanfrage) getItem(pPosition);
		String s=suchanfrage.getHtmlString();
		
		adapterLetzteSuchanfrageHolder.textViewString.setText(Html.fromHtml(s));
		return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
			
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

	   		 mData = new ArrayList<Suchanfrage>();		 
	   			
	   		 c.moveToLast();
	   			while (c.isBeforeFirst() == false) {
	   				mData.add(new Suchanfrage(
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
	   	c.close();
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
        return mData.size();
	}
	@Override
	public Object getItem(int position) {		
        return mData.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}

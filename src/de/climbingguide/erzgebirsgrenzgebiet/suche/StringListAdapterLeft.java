package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class StringListAdapterLeft extends ArrayAdapter<String>{
  
//	private final LayoutInflater mLayoutInflater;	
	private Context thisContext;
	
	public StringListAdapterLeft(Context pContext, int resource, ArrayList<String> s) {
		super(pContext, resource, s);	
		this.thisContext = pContext;
//        mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public StringListAdapterLeft(Context pContext, ArrayList<String> s) {
		super(pContext, R.layout.drop_down_layout_left, s);
		this.thisContext=pContext;
	}

	@Override
	public View getView(int pPosition, View convertView, ViewGroup parent) {
        
		AdapterLeftHolder adapterLeftHolder;
		
		if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) this.thisContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
		        convertView = mLayoutInflater.inflate(R.layout.drop_down_layout_left, null);
		        
		        adapterLeftHolder = new AdapterLeftHolder();
		        adapterLeftHolder.textViewString = (TextView) convertView.findViewById(R.id.textViewProzent);
		        
		       convertView.setTag(adapterLeftHolder);
		} else {
			adapterLeftHolder = (AdapterLeftHolder) convertView.getTag();
		}
		
		// String basteln, der angezeigt werden soll: Gipfel, Weg, (Schwierigkeit)
		
//		TextView textViewString = (TextView) convertView.findViewById(R.id.textView1);
		adapterLeftHolder.textViewString.setText(getItem(pPosition));
		        
		return convertView;
	}	
}

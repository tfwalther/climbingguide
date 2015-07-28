package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class StringListAdapterGipfel extends ArrayAdapter<Gipfel>{
  
	private final LayoutInflater mLayoutInflater;	
	
	public StringListAdapterGipfel(Context pContext, int resource, ArrayList<Gipfel> gipfel) {
		super(pContext, resource, gipfel);	
        mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	@Override
	public View getView(int pPosition, View convertView, ViewGroup parent) {
        
		AdapterGipfelHolder adapterGipfelHolder;
		
		if (convertView == null) {
		        convertView = mLayoutInflater.inflate(R.layout.drop_down_layout_gipfel, null);
		        
		        adapterGipfelHolder = new AdapterGipfelHolder();
		        adapterGipfelHolder.gipfel = (TextView) convertView.findViewById(R.id.dropdown_gipfel);
		        adapterGipfelHolder.gipfel.setTypeface(null, Typeface.BOLD);		
		        adapterGipfelHolder.gebiet = (TextView) convertView.findViewById(R.id.dropdown_gebiet);
		        adapterGipfelHolder.gipfelnummer = (TextView) convertView.findViewById(R.id.dropdown_gipfelnummer);
		        
		       convertView.setTag(adapterGipfelHolder);
		} else {
			adapterGipfelHolder = (AdapterGipfelHolder) convertView.getTag();
		}
		
		// String basteln, der angezeigt werden soll: Gipfel, Weg, (Schwierigkeit)
		
//		TextView gipfel = (TextView) convertView.findViewById(R.id.dropdown_gipfel);
//		TextView gebiet = (TextView) convertView.findViewById(R.id.dropdown_gebiet);
//		TextView gipfelnummer = (TextView) convertView.findViewById(R.id.dropdown_gipfelnummer);

		Gipfel gipfelobject = getItem(pPosition);
		adapterGipfelHolder.gipfel.setText(Html.fromHtml(gipfelobject.getGipfelHtml()));
		adapterGipfelHolder.gipfelnummer.setText(gipfelobject.getGipfelnummer().toString());
        // Gebietname auf 5 Zeichen begrenten
        if (gipfelobject.getGebiet().length() > 5) {
        	gipfelobject.setGebiet(gipfelobject.getGebiet().subSequence(0, 4).toString() + ".");
        }
		adapterGipfelHolder.gebiet.setText(gipfelobject.getGebiet());
		        
		return convertView;
	}	
}

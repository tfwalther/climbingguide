package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;

public class StringListAdapterWege extends ArrayAdapter<Weg>{
  
//	private final LayoutInflater mLayoutInflater;	
	private Context thisContext;
	
	public StringListAdapterWege(int resource, ArrayList<Weg> s) {
		super(ActionBarAppActivity.getInstance(), resource, s);	
		this.thisContext = ActionBarAppActivity.getInstance();
//        mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public StringListAdapterWege(ArrayList<Weg> s) {
		super(ActionBarAppActivity.getInstance(), R.layout.drop_down_layout_left, s);
		this.thisContext=ActionBarAppActivity.getInstance();
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
		Weg weg = getItem(pPosition);
		adapterLeftHolder.textViewString.setText(Html.fromHtml(weg.getHtmlWegname()));
		        
		return convertView;
		
	}	
}

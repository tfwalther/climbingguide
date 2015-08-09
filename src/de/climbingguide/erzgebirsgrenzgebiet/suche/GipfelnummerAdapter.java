package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class GipfelnummerAdapter extends ArrayAdapter<String> {

	private final LayoutInflater mLayoutInflater;	
	private int startNummer;
	private int endNummer;
	
	public GipfelnummerAdapter() {
		super(ActionBarAppActivity.getInstance(), R.layout.drop_down_layout_centered);
        mLayoutInflater = (LayoutInflater) ActionBarAppActivity.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        startNummer=1;
        setGebiet(null);
	}	
	
	@Override
	public int getCount() {
		return endNummer;
	}

	@Override
	public String getItem(int position) {
		return ((Integer)(position+startNummer)).toString();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
		AdapterCenterHolder adapterCenterHolder;
		
		if (convertView == null) {
		       convertView = mLayoutInflater.inflate(R.layout.drop_down_layout_centered, null);		        
		       adapterCenterHolder = new AdapterCenterHolder();
		       adapterCenterHolder.textViewString = (TextView) convertView.findViewById(R.id.textViewProzent);		        
		       convertView.setTag(adapterCenterHolder);
		} else {
			adapterCenterHolder = (AdapterCenterHolder) convertView.getTag();
		}				
		adapterCenterHolder.textViewString.setText(getItem(position).toString());
		        
		return convertView;
	}

//    public String convertToString(int position) {
//        return getItem(position).toString();
//    }	
	
	public int getStartNummer() {
		return startNummer;
	}

	public void setStartNummer(int startNummer) {
		this.startNummer = startNummer;
	}

	public void setGebiet(String gebiet) {
		endNummer=KleFuContract.getMaxGipfel(gebiet);
	}
	
	public Integer getEndNummer() {
		return endNummer;
	}

}

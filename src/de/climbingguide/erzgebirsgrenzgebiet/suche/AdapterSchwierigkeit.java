package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class AdapterSchwierigkeit extends ArrayAdapter<String> {

	private final LayoutInflater mLayoutInflater;	
	private int startSchwierigkeit;
	
	public AdapterSchwierigkeit() {
		super(ActionBarAppActivity.getInstance(), R.layout.drop_down_layout_centered);
        mLayoutInflater = (LayoutInflater) ActionBarAppActivity.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        startSchwierigkeit=1;
	}	
	
	@Override
	public int getCount() {
		int count;
		count = KleFuEntry.MAXSCHWIERIGKEIT-getStartSchwierigkeit();
		if (startSchwierigkeit<=KleFuEntry.MAXSCHWIERIGKEITSPRUNG)
			count -= KleFuEntry.MAXSCHWIERIGKEITLUECKE;
		return count;
	}

	@Override
	public String getItem(int position) {
		int schwierigkeit;
		schwierigkeit=position+startSchwierigkeit;
		if (schwierigkeit>KleFuEntry.MAXSCHWIERIGKEITSPRUNG)
			schwierigkeit+=KleFuEntry.MAXSCHWIERIGKEITLUECKE;
		
		return Schwierigkeit.SchwierigkeitIntToString(schwierigkeit);
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

	public int getStartSchwierigkeit() {
		return startSchwierigkeit;
	}

	public void setStartSchwierigkeit(int startSchwierigkeit) {
		this.startSchwierigkeit = startSchwierigkeit;
	}
}

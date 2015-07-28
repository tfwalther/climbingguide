package de.climbingguide.erzgebirsgrenzgebiet.suche;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class StringListAdapterCenter extends ArrayAdapter<String> {
 
	private final LayoutInflater mLayoutInflater;	
	
	public StringListAdapterCenter(Context pContext, int resource, ArrayList<String> s) {
		super(pContext, resource, s);
        mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}
//	
//	private ArrayList<String> mData = new ArrayList<String>();
//    
//	private final LayoutInflater mLayoutInflater;
//
//	public StringListAdapter(Context pContext, int viewResourceId, ArrayList<String> pData){
//	                mData = pData;
//	                
//	                mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}	
//	
//	@Override
//	public int getCount() {
//        return mData.size();
//	}
//
//	@Override
//	public Object getItem(int pPosition) {
//        return mData.get(pPosition);
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
	@Override
	public View getView(int pPosition, View convertView, ViewGroup parent) {
        
		AdapterCenterHolder adapterCenterHolder;
		
		if (convertView == null) {
		        convertView = mLayoutInflater.inflate(R.layout.drop_down_layout_centered, null);
		        
		        adapterCenterHolder = new AdapterCenterHolder();
		        adapterCenterHolder.textViewString = (TextView) convertView.findViewById(R.id.textViewProzent);
		        
		       convertView.setTag(adapterCenterHolder);
		} else {
			adapterCenterHolder = (AdapterCenterHolder) convertView.getTag();
		}
		
		// String basteln, der angezeigt werden soll: Gipfel, Weg, (Schwierigkeit)
		
//		TextView textViewString = (TextView) convertView.findViewById(R.id.textView1); 
		adapterCenterHolder.textViewString.setText(getItem(pPosition));
		        
		return convertView;
	}
}

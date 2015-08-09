package de.climbingguide.erzgebirsgrenzgebiet.list;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannedString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;

public class ListFragment extends Fragment implements OnChildClickListener {

	private static Weg weg;
	public static Weg getWeg() { return weg; };
	public ExpandableListAdapter adapter;
	private static FloatingGroupExpandableListView lv;
	protected View rootView;
	private Integer anzahlTreffer;

	public ListFragment() {};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		anzahlTreffer=KleFuContract.anzahltreffer;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.activity_list_expand, container, false);
		adapter = new ExpandableListAdapter(getActivity());	
		WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
		lv = (FloatingGroupExpandableListView) rootView.findViewById(R.id.lvExp);
		// Even though the child divider has already been set on the layout file, we have to set it again here
		// This prevents a bug where the background turns to the color of the child divider when the list is expanded
		lv.setChildDivider(new ColorDrawable(Color.DKGRAY));
		lv.setAdapter(wrapperAdapter);
		lv.setOnChildClickListener(this);
		lv.setItemsCanFocus(true);
		lv.setOnScrollFloatingGroupListener(new FloatingGroupExpandableListView.OnScrollFloatingGroupListener() {
		
		@Override
		public void onScrollFloatingGroupListener(View floatingGroupView, int scrollY) {
			float interpolation = - scrollY / (float) floatingGroupView.getHeight();
			
			boolean isGipfelBestiegen;
			TextView gipfelTextView = (TextView)floatingGroupView.findViewById(R.id.list_header_gipfel);
			SpannedString spann = (SpannedString)gipfelTextView.getText();
			UnderlineSpan[] anyUnderlineSpans = spann.getSpans(0, spann.length(), UnderlineSpan.class);
	        isGipfelBestiegen = anyUnderlineSpans.length>0;	        
			
	        ListViewParentHolder listViewParentHolder = (ListViewParentHolder) floatingGroupView.getTag();
	        
			final RelativeLayout background = listViewParentHolder.parent_layout;		
			if (isGipfelBestiegen) {
				final int green = getResources().getColor(R.color.green);
				final int greenRed = Color.red(green);
				final int greenGreen = Color.green(green);
				final int greenBlue = Color.blue(green);
				final int greenToWhiteRed = (int) (greenRed + (255-greenRed) * interpolation);
				final int greenToWhiteGreen = (int) (greenGreen + (255-greenGreen) * interpolation);
				final int greenToWhiteBlue = (int) (greenBlue + (255-greenBlue) * interpolation);
				final int greenToWhiteColor = Color.argb(255, greenToWhiteRed, greenToWhiteGreen, greenToWhiteBlue);
				background.setBackgroundColor(greenToWhiteColor);
			} else {
				final int lightblue = getResources().getColor(R.color.lightblue);
				final int lightblueRed = Color.red(lightblue);
				final int lightbluelightblue = Color.green(lightblue);
				final int lightblueBlue = Color.blue(lightblue);
				final int lightblueToWhiteRed = (int) (lightblueRed + (255-lightblueRed) * interpolation);
				final int lightblueToWhitelightblue = (int) (lightbluelightblue + (255-lightbluelightblue) * interpolation);
				final int lightblueToWhiteBlue = (int) (lightblueBlue + (255-lightblueBlue) * interpolation);
				final int lightblueToWhiteColor = Color.argb(255, lightblueToWhiteRed, lightblueToWhitelightblue, lightblueToWhiteBlue);
				background.setBackgroundColor(lightblueToWhiteColor);				
			}
			
			final TextView textViewGipfel = listViewParentHolder.list_header_gipfel;
			final TextView textViewGebiet = listViewParentHolder.list_header_gebiet;
			final int textColor = textViewGipfel.getTextColors(). getDefaultColor();
			final int textColorRed = Color.red(textColor);
			final int textColortextColor = Color.green(textColor);
			final int textColorBlue = Color.blue(textColor);
			final int textColorToWhiteRed = (int) (textColorRed + (255-textColorRed) * interpolation);
			final int textColorToWhiteGreen = (int) (textColortextColor + (255-textColortextColor) * interpolation);
			final int textColorToWhiteBlue = (int) (textColorBlue + (255-textColorBlue) * interpolation);
			final int textColorToWhiteColor = Color.argb(255, textColorToWhiteRed, textColorToWhiteGreen, textColorToWhiteBlue);
			textViewGipfel.setTextColor(textColorToWhiteColor);
			textViewGebiet.setTextColor(textColorToWhiteColor);
			
			// Changing from RGB(255,255,255) to RGB(0,0,0)
			final int whiteTotransparent = (int) (255 - 255 * interpolation);
		
			final Drawable imageDrawable = listViewParentHolder.buttonKarte.getBackground().mutate();
			final Drawable imageDrawableZwei = listViewParentHolder.buttonKarte.getCompoundDrawables()[0].mutate();			
			
			imageDrawable.setAlpha(whiteTotransparent);
			imageDrawableZwei.setAlpha(whiteTotransparent);

		}
	});


		expandAll();    	

        return rootView;
    }    
    
    @Override
    public void onResume() {
    	super.onResume();
		ActionBar ab = ((ActionBarActivity)getActivity()).getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setSubtitle(anzahlTreffer.toString() + " " + 
				getString(R.string.treffer));
    }
    
	public void expandAll() {
		for (int i=0; i<adapter.getGroupCount(); i++)
		{
			lv.expandGroup(i);			
		}		
	}

	public void collapseAll() {
		for (int i=0; i<adapter.getGroupCount(); i++)
		{
			lv.collapseGroup(i);			
		}		
	}	

@Override
public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
		int childPosition, long id) {

	WrapperExpandableListAdapter wrapper_adapter=((WrapperExpandableListAdapter)parent.getExpandableListAdapter());
	
	Weg childWeg=(Weg)wrapper_adapter.getChild(groupPosition, childPosition);
	childWeg.setShownDetailed(!childWeg.isShownDetailed());
	adapter.notifyDataSetChanged();
	return true;
}
	
}

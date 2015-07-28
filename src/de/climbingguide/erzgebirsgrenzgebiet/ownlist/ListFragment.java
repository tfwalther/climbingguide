package de.climbingguide.erzgebirsgrenzgebiet.ownlist;

import net.sqlcipher.DatabaseUtils;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;


public class ListFragment extends Fragment implements
OnChildClickListener,
LoaderCallbacks<Cursor> {

	private static Weg weg;
	public static Weg getWeg() { return weg; };
//	private TextToSpeechWeg tts;
//	private boolean ttsBereit;
//	private Builder builder;
	public CustomCursorTreeAdapter adapter;
	private static FloatingGroupExpandableListView lv;
	protected View rootView;
	private Integer anzahlWege;
	private MyCursorLoader loader;

	public ListFragment() {};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActivity().getSupportLoaderManager().initLoader(0, null, this);//Loader initialisieren			
		
		 // Set up the action bar to show a dropdown list.
	    final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); 

	    final String[] dropdownValues = getResources().getStringArray(R.array.sortierungen);

	    // Specify a SpinnerAdapter to populate the dropdown list.
	    ArrayAdapter<String> adapterDropDown = new ArrayAdapter<String>(actionBar.getThemedContext(),
	        android.R.layout.simple_spinner_item, android.R.id.text1,
	        dropdownValues);

	    adapterDropDown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    // Set up the dropdown list navigation in the action bar.
	    actionBar.setListNavigationCallbacks(adapterDropDown, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int position, long id) {
				switch (position) {
				case 0: loader.setSortOrder(KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 1: loader.setSortOrder(KleFuEntry.COLUMN_NAME_DATUM + " ASC");
					break;
				case 2: loader.setSortOrder(KleFuEntry.COLUMN_NAME_GIPFEL + " ASC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 3: loader.setSortOrder(KleFuEntry.COLUMN_NAME_WEGNAME + " ASC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 4: loader.setSortOrder(KleFuEntry.COLUMN_NAME_GEBIET + " ASC, " + KleFuEntry.COLUMN_NAME_GIPFELNUMMER + " ASC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 5: loader.setSortOrder(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU + " DESC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 6: loader.setSortOrder("a."+KleFuEntry.COLUMN_NAME_RP + " DESC, " + "a."+KleFuEntry.COLUMN_NAME_VORSTIEG + " DESC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 7: loader.setSortOrder("a."+KleFuEntry.COLUMN_NAME_VORSTIEG + " DESC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 8: loader.setSortOrder("a."+KleFuEntry.COLUMN_NAME_VORSTIEG + " ASC, " + "a."+KleFuEntry.COLUMN_NAME_RP + " ASC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;					
				case 9: loader.setSortOrder(KleFuEntry.COLUMN_NAME_BEMERKUNGEN + " DESC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				case 10: loader.setSortOrder(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF + " DESC, " + KleFuEntry.COLUMN_NAME_DATUM + " DESC");
					break;
				default: return false;
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	anzahlWege = (int)DatabaseUtils.queryNumEntries(KleFuEntry.db, KleFuEntry.TABLE_NAME_EIGENE_WEGE);  	   	
    	((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(anzahlWege.toString());
    	
    	rootView = inflater.inflate(R.layout.activity_list_expand, container, false);
		 
		lv = (FloatingGroupExpandableListView) rootView.findViewById(R.id.lvExp);
		adapter = new CustomCursorTreeAdapter(getActivity(), lv, anzahlWege, loader, this);	

		
		WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
//		// Even though the child divider has already been set on the layout file, we have to set it again here
//		// This prevents a bug where the background turns to the color of the child divider when the list is expanded
		lv.setChildDivider(new ColorDrawable(getResources().getColor(R.color.dividergrey)));
		lv.setAdapter(wrapperAdapter);
		lv.setOnChildClickListener(this);
		lv.setItemsCanFocus(true);
		lv.setOnScrollFloatingGroupListener(new FloatingGroupExpandableListView.OnScrollFloatingGroupListener() {
	
		@Override
		public void onScrollFloatingGroupListener(View floatingGroupView, int scrollY) {
			float interpolation = - scrollY / (float) floatingGroupView.getHeight();
			
	        ListViewParentHolder listViewParentHolder = (ListViewParentHolder) floatingGroupView.getTag();
	        
			final LinearLayout background = listViewParentHolder.parent_layout;
			int color = (Integer)listViewParentHolder.list_header_gipfel.getTag();
			background.setBackgroundColor(interpolateColor(interpolation, color));
					
			final TextView textViewGipfel = listViewParentHolder.list_header_gipfel;
			final TextView textViewWegname = listViewParentHolder.list_header_wegname;
			final TextView textViewSchwierigkeit = listViewParentHolder.list_header_schwierigkeit;
			final int textColor = textViewGipfel.getTextColors().getDefaultColor();
			final int textColorToWhiteColor = interpolateColor(interpolation,textColor);
			textViewGipfel.setTextColor(textColorToWhiteColor);
			textViewWegname.setTextColor(textColorToWhiteColor);
			textViewSchwierigkeit.setTextColor(textColorToWhiteColor);
			listViewParentHolder.buttonDatum.setTextColor(textColorToWhiteColor);
			
			final int schwierColor = textViewGipfel.getTextColors().getDefaultColor();
			final int schwierColorToWhiteColor = interpolateColor(interpolation, schwierColor);

			final TextView textViewHinweis = listViewParentHolder.list_header_hinweis;			
			textViewHinweis.setTextColor(schwierColorToWhiteColor);
			
			// Changing from RGB(255,255,255) to RGB(0,0,0)
			final int whiteTotransparent = (int) (255 - 255 * interpolation);
			
			final Drawable imageDrawable = 	listViewParentHolder.imageViewWorld.getDrawable().mutate();
			final Drawable imageDrawableDrei = listViewParentHolder.buttonDatum.getBackground().mutate();
						
			imageDrawable.setAlpha(whiteTotransparent);
			imageDrawableDrei.setAlpha(whiteTotransparent);
		}

		private int interpolateColor(float interpolation, final int textColor) {
			final int textColorRed = Color.red(textColor);
			final int textColorGreen = Color.green(textColor);
			final int textColorBlue = Color.blue(textColor);
			final int textColorToWhiteRed = (int) (textColorRed + (255-textColorRed) * interpolation);
			final int textColorToWhiteGreen = (int) (textColorGreen + (255-textColorGreen) * interpolation);
			final int textColorToWhiteBlue = (int) (textColorBlue + (255-textColorBlue) * interpolation);
			final int textColorToWhiteColor = Color.argb(255, textColorToWhiteRed, textColorToWhiteGreen, textColorToWhiteBlue);
			return textColorToWhiteColor;
		}
	});
        return rootView;
    }    
    
    @Override
    public void onResume() {
    	super.onResume();
		ActionBar ab = ((ActionBarActivity)getActivity()).getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setSubtitle(anzahlWege.toString() + " " + 
				getString(R.string.geklettert));
    }   
    
@Override
public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
		int childPosition, long id) {

	parent.collapseGroup(groupPosition);
	return true;
}

@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	loader = new MyCursorLoader(getActivity());
	return loader;
}

@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	adapter.setGroupCursor(cursor);	
}

@Override
public void onLoaderReset(Loader<Cursor> loader) {
	adapter.setGroupCursor(null);
	
}

static class MyCursorLoader extends CursorLoader{
	
	private static String sortOrder=KleFuEntry.COLUMN_NAME_DATUM + " DESC";
	
    public MyCursorLoader(Context context) {
       super(context);
    }
    
    @SuppressWarnings("static-access")
	@Override
    public void setSortOrder(String sortOrder) {
    	this.sortOrder = sortOrder;
    }
    
    @Override
    public String getSortOrder() {
    	return sortOrder;
    }
    
    @Override
       public Cursor loadInBackground() {
    	String[] projectionEigeneWege = { KleFuEntry._ID,//0 
    			KleFuEntry.COLUMN_NAME_WEGEID,//1
    			KleFuEntry.COLUMN_NAME_VORSTIEG,//2
    			KleFuEntry.COLUMN_NAME_RP,//3
    			KleFuEntry.COLUMN_NAME_OU,//4
    			KleFuEntry.COLUMN_NAME_DATUM,//5
    			KleFuEntry.COLUMN_NAME_PERSONEN,//6
    			KleFuEntry.COLUMN_NAME_BEMERKUNGEN,//7
    			KleFuEntry.COLUMN_NAME_IS_EXPANDED//8 
    	};

    	String[] projectionWege = { KleFuEntry.COLUMN_NAME_GIPFELID,//0
    			KleFuEntry.COLUMN_NAME_WEGNAME,//1
    			KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU
    	};    	

    	String[] projectionGipfel = { KleFuEntry.COLUMN_NAME_GEBIET,//0
    			KleFuEntry.COLUMN_NAME_GIPFELNUMMER,//1
    			KleFuEntry.COLUMN_NAME_GIPFEL
    	};    	
    	
    	String sortOrder = getSortOrder();
    	String sqlStatement;
    	sqlStatement = "SELECT ";
    	for (String s : projectionEigeneWege) {
    		sqlStatement += "a." + s + ", ";
//    		sqlStatement += KleFuEntry.TABLE_NAME_EIGENE_WEGE + "." + s + ", ";
    	}
    	for (String s : projectionWege) {
    		sqlStatement += "b." + s + ", ";
    	}
    	sqlStatement += "c." + projectionGipfel[0] + ", ";
    	sqlStatement += "c." + projectionGipfel[1] + ", ";
    	sqlStatement += "c." + projectionGipfel[2];
    	sqlStatement += " FROM " + KleFuEntry.TABLE_NAME_EIGENE_WEGE + " a "; 
    	sqlStatement += "LEFT OUTER JOIN " + KleFuEntry.TABLE_NAME_WEGE
    			+ " b ON b."+KleFuEntry._ID+"=a."+KleFuEntry.COLUMN_NAME_WEGEID+" ";
    	sqlStatement += "LEFT OUTER JOIN "+KleFuEntry.TABLE_NAME_GIPFEL
    			+ " c ON c."+KleFuEntry._ID+"=b."+KleFuEntry.COLUMN_NAME_GIPFELID; 
    	sqlStatement += " ORDER BY " + sortOrder;
    	
    	Cursor c = KleFuEntry.db.rawQuery(sqlStatement, null);
    	
//    	Cursor c = KleFuEntry.db.query(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
//    			projectionEigeneWege,	null, null,	null, null,
//    			sortOrder);

          return c;
       }            
    }
}

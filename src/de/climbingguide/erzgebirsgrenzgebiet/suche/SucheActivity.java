package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.astuetz.PagerSlidingTabStrip;

import de.climbingguide.erzgebirsgrenzgebiet.ActiveActivity;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
 
public class SucheActivity extends ActiveActivity 
	implements OnPageChangeListener {

	//Filedownload Zeug
	public DownloadHandler activityHandler;
	public DownloadHandler getDownloadHandler() { return activityHandler; }	
	private DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }	
		
//	private static String weg;
//	private static String beschreibung;
//	private static String erstbegeher;
//	private static String erstbegehungsdatum;
	
	
	
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar ab;
	
//private String[] Gebiete = {"Erzgebirsgrenzgebiet", "Schmilka", "Schrammsteine"};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			openSettings();
//			finish();			
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
    
	/*
	 * styled die ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.suche, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suche);
        
		// Show the Up button in the action bar.
        // Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		ab = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        final PagerSlidingTabStrip tabs;
		
        viewPager.setAdapter(mAdapter);
		ab.setDisplayHomeAsUpEnabled(true);
//		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		Tab tab1 = ab.newTab();
//		tab1.setText(getString(R.string.suchformular));
//		tab1.setTabListener(this);
//		ab.addTab(tab1);
//		
//		Tab tab2 = ab.newTab();
//		tab2.setText(getString(R.string.letzte_anfragen));
//		tab2.setTabListener(this);
//		ab.addTab(tab2);
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        ab.setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});
		
		tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		viewPager.setPageMargin(pageMargin);
		
		tabs.setOnPageChangeListener(this);
		tabs.setViewPager(viewPager);
		ClimbingGuideApplication.setTabsSettings(tabs);
	
	}		

@Override
public void onPageScrollStateChanged(int arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPageScrolled(int arg0, float arg1, int arg2) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPageSelected(int position) {
	switch (position) {
	case 0:
		FormularFragment.afterPageChange=true;
		break;
	case 1:
		InputMethodManager imm = (InputMethodManager) this.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		break;
	}
}
}

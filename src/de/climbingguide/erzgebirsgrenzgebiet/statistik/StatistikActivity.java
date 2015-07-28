package de.climbingguide.erzgebirsgrenzgebiet.statistik;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Spinner;

import com.astuetz.PagerSlidingTabStrip;

import de.climbingguide.erzgebirsgrenzgebiet.ActiveActivity;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
 
public class StatistikActivity extends ActiveActivity 
	implements TabListener {

	//Filedownload Zeug
	public DownloadHandler activityHandler;
	public DownloadHandler getDownloadHandler() { return activityHandler; }	
	private DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }	
			    
    private ViewPager viewPager;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.statistik, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suche);
        
        final TabsPagerAdapter mAdapter;
        final PagerSlidingTabStrip tabs;
        final ActionBar ab;      
                        
		// Show the Up button in the action bar.
        // Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		ab = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager()); 				
		
		viewPager.setAdapter(mAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
           @Override
           public void onPageSelected(int position)
           {
              ab.getTabAt(position).select();
              ViewParent root = findViewById(android.R.id.content).getParent();
              findAndUpdateSpinner(root, position);
           }

           /**
            * Searches the view hierarchy excluding the content view 
            * for a possible Spinner in the ActionBar. 
            * 
            * @param root The parent of the content view
            * @param position The position that should be selected
            * @return if the spinner was found and adjusted
            */
           private boolean findAndUpdateSpinner(Object root, int position)
           {
              if (root instanceof android.widget.Spinner)
              {
                 // Found the Spinner
                 Spinner spinner = (Spinner) root;
                 spinner.setSelection(position);
                 return true;
              }
              else if (root instanceof ViewGroup)
              {
                 ViewGroup group = (ViewGroup) root;
                 if (group.getId() != android.R.id.content)
                 {
                    // Found a container that isn't the container holding our screen layout
                    for (int i = 0; i < group.getChildCount(); i++)
                    {
                       if (findAndUpdateSpinner(group.getChildAt(i), position))
                       {
                          // Found and done searching the View tree
                          return true;
                       }
                    }
                 }
              }
              // Nothing found
              return false;
           }
        });

		ab.setDisplayHomeAsUpEnabled(true);
		tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			tabs.setVisibility(View.GONE);
			viewPager.setPageMargin(0);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			Tab tab1 = ab.newTab();
			tab1.setText(mAdapter.getPageTitle(0));
			tab1.setTabListener(this);
			ab.addTab(tab1);	
	
			Tab tab3 = ab.newTab();
			tab3.setText(mAdapter.getPageTitle(1));
			tab3.setTabListener(this);
			ab.addTab(tab3);
			
			Tab tab2 = ab.newTab();
			tab2.setText(mAdapter.getPageTitle(2));
			tab2.setTabListener(this);
			ab.addTab(tab2);															

		} else {
			tabs.setVisibility(View.VISIBLE);			
			//sliding tabs kram
			final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
					.getDisplayMetrics());
			viewPager.setPageMargin(pageMargin);
	
			tabs.setViewPager(viewPager);
			ClimbingGuideApplication.setTabsSettings(tabs);
		}		
	}	
		
@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
    // on tab selected
    // show respected fragment view
    viewPager.setCurrentItem(tab.getPosition());
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
}

}
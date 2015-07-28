package de.climbingguide.erzgebirsgrenzgebiet.statistik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {	
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
	@Override
	public CharSequence getPageTitle(int position) {
        switch (position) {
        case 0:
            return ClimbingGuideApplication.getInstance().getString(R.string.schwierigkeitsstatistik);
        case 1:
            return ClimbingGuideApplication.getInstance().getString(R.string.gipfelkuchen);
        case 2:
            return ClimbingGuideApplication.getInstance().getString(R.string.gipfelstatistik);
        }
        return "";
	}    
    
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new SchwierigkeitsstatistikFragment();
        case 1:
            return new GipfelstatistikPieFragment();
        case 2:
            return new GipfelstatistikFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}

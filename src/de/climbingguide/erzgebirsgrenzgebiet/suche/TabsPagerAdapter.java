package de.climbingguide.erzgebirsgrenzgebiet.suche;

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
            return ClimbingGuideApplication.getInstance().getString(R.string.suchformular);
        case 1:
            return ClimbingGuideApplication.getInstance().getString(R.string.letzte_anfragen);
        }
        return "";
	}       
    
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new FormularFragment();
        case 1:
            // Games fragment activity
            return new LetzteSuchanfragenFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}

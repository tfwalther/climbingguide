package de.climbingguide.erzgebirsgrenzgebiet.ownlist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.climbingguide.erzgebirsgrenzgebiet.ActiveActivity;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class ListActivity extends ActiveActivity {
	 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ListFragment listFragment;
		
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.activity_list);
		
		//ListFragment hinzufügen
		FragmentManager fragmentManager = getSupportFragmentManager();
		listFragment = new ListFragment();	
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.linear_layout_list, listFragment);
		fragmentTransaction.commit();
//		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.weganzeige, arrayListResults);
//		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayListResults);
//		WegeListAdapter adapter = new WegeListAdapter(this, KleFuContract.arrayListSearchResult);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list, menu);
	    return super.onCreateOptionsMenu(menu);
	}

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
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_map:
			openLiveKarte();
			return true;			
		default: 
			return super.onOptionsItemSelected(item);
	}
	}
			
}

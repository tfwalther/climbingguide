package de.climbingguide.erzgebirsgrenzgebiet.db;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

public class AsyncTaskGetReadableDatabase extends AsyncTask<Void, Void, Void> {

	private ProgressDialog progressDialog;
	private Activity thisActivity;
	
    public AsyncTaskGetReadableDatabase(Activity thisActivity) {
        this.thisActivity = thisActivity;
    }	
	
	@Override
	protected void onPreExecute() {     
		thisActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);						
        progressDialog = new ProgressDialog(thisActivity);
        progressDialog.setTitle(thisActivity.getString(R.string.progress_dialog_title_searching));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisActivity.getApplicationContext());
        if (preferences.getBoolean("firstTime", false)) {
            progressDialog.setMessage(thisActivity.getString(R.string.progress_dialog_database_reading_desc));        	
        } else {
            progressDialog.setMessage(thisActivity.getString(R.string.progress_dialog_database_reading_desc_firstTime));        	
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		SQLiteDatabase.loadLibs(thisActivity);
	    KleFuEntry.db = KleFuEntry.Zugriff.getWritableDatabase("GRHsfsf6356§$%&erEIOHoih3456eoiribIOEhrb10hne902454830990(=/$=/=§NDKFN");
	    return null;
	}
	
	@Override
	protected void onPostExecute(Void v) {
		thisActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);			
		progressDialog.dismiss();
		 String[] projection = { 
					KleFuEntry.COLUMN_NAME_GEBIET//0
		 };
		Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_SUCHANFRAGEN,  // The table to query
			    projection,           // The columns to return
			    null,                                // The columns for the WHERE clause
			    null,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
		Button buttonLetzteAnfrage=(Button) thisActivity.findViewById(R.id.buttonletzteSuche);
		if (c.getCount()<=0) {
			buttonLetzteAnfrage.setVisibility(View.GONE);
		} else {
			buttonLetzteAnfrage.setVisibility(View.VISIBLE);			
		}
		c.close();
	}
}

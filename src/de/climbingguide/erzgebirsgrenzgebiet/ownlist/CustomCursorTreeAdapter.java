package de.climbingguide.erzgebirsgrenzgebiet.ownlist;

import java.util.Calendar;

import org.mapsforge.core.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorTreeAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.EigenerWeg;
import de.climbingguide.erzgebirsgrenzgebiet.EigenerWeg.OnRemoveItemListener;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.Downloader;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
import de.climbingguide.erzgebirsgrenzgebiet.maps.LiveKarteActivity;
import de.climbingguide.erzgebirsgrenzgebiet.ownlist.ListFragment.MyCursorLoader;

public class CustomCursorTreeAdapter extends CursorTreeAdapter implements
Downloader {
	
	private Activity thisActivity;
	private LayoutInflater inflater;
	
	//für die Datumsknöpfe 
	protected static int year, month, day;
	protected static Button dateButton; 
    protected static CustomCursorTreeAdapter thisadapter;
    protected ExpandableListView listView;
	
	//Filedownload Zeug
	public DownloadHandler activityHandler;
	public DownloadHandler getDownloadHandler() { return activityHandler; }	
	private DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }
	
	private LoaderCallbacks<Cursor> loaderCallbacks;
		
	public CustomCursorTreeAdapter(Activity thisActivity, ExpandableListView listView, int anzahlWege, MyCursorLoader loader, LoaderCallbacks<Cursor> loaderCallbacks) {
		super(null, thisActivity);
		this.thisActivity=thisActivity;
		thisadapter=this;
		this.listView=listView;
		inflater = (LayoutInflater)thisActivity.getSystemService("layout_inflater");
		this.loaderCallbacks=loaderCallbacks;
		
	}

	@Override
	protected void bindChildView(View view, Context context,
			android.database.Cursor cursor, boolean isLastChild) {
		//do nothing
	}

	@Override
	protected void bindGroupView(View view, Context context,
			android.database.Cursor cursor, boolean isExpanded) {
		final ListViewParentHolder holder = (ListViewParentHolder)view.getTag();
		
		if (isExpanded)	{
			holder.expandedImage.setImageResource(R.drawable.minus);
		} else {
			holder.expandedImage.setImageResource(R.drawable.plus);
		}
		
		// Weg aus Datenbank anhand der WegeID abfragen

		final EigenerWeg childWeg=getEigenerWeg(cursor);
		
		holder.parent_layout.setBackgroundColor(thisActivity.getResources().getColor(R.color.lightblue));
		holder.list_header_gipfel.setTag(thisActivity.getResources().getColor(R.color.lightblue));
		
		holder.list_header_gipfel.setText(Html.fromHtml(childWeg.getHtmlGipfelnummerPlusGipfel()));
		holder.list_header_schwierigkeit.setText(Html.fromHtml(childWeg.getHtmlSchwierigkeit()));
		holder.list_header_wegname.setText(childWeg.getWegname());
		ClimbingGuideApplication.setEigeneWegeHinweis(
						holder.list_header_hinweis,
						childWeg.isVorstieg(),
						childWeg.isRP(),
						childWeg.isoU()
		);
		
		holder.imageViewWorld.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		LiveKarteActivity.setMapWasCentered(false);
				openLiveKarte(childWeg.getGeopoint(), childWeg.getGipfel());	
			}
		});		
		holder.imageViewWorld.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						v.setBackgroundColor(Color.WHITE);
						break;
					case MotionEvent.ACTION_UP:
						v.setBackgroundColor(Color.TRANSPARENT);
						break;
					case MotionEvent.ACTION_CANCEL:
						v.setBackgroundColor(Color.TRANSPARENT);
						break;
					case MotionEvent.ACTION_OUTSIDE:
						v.setBackgroundColor(Color.TRANSPARENT);
						break;	
				}
				return false;
			}
		});
		
		holder.buttonDatum.setText(ClimbingGuideApplication.getDate(childWeg.getDatum()));

		holder.buttonDatum.setOnClickListener(new OnClickListener() {

			private long time = childWeg.getDatum();
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(time);
				day = calendar.get(Calendar.DAY_OF_MONTH);
				month = calendar.get(Calendar.MONTH);
				year = calendar.get(Calendar.YEAR);
				holder.buttonDatum.setTag(childWeg.getEigeneWegId());
				showDatePickerDialog(holder.buttonDatum);
			}
		});
		
	}

	private EigenerWeg getEigenerWeg(android.database.Cursor cursor) {
		EigenerWeg childWeg;
		childWeg = new EigenerWeg(cursor.getInt(0),
				cursor.getInt(1),
				cursor.getInt(2)>0,
				cursor.getInt(3)>0,
				cursor.getInt(4)>0,
				cursor.getLong(5),
				cursor.getString(6),
				cursor.getString(7),
				cursor.getInt(8)>0
		);
		return childWeg;
	}

	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    dateButton = (Button)v;
	    newFragment.show(((FragmentActivity)thisActivity).getSupportFragmentManager(), "datePicker");
	}	
	
	@Override
	protected android.database.Cursor getChildrenCursor(
			android.database.Cursor groupCursor) {
		return null;
	}

	@Override
	protected View newChildView(Context context,
			android.database.Cursor cursor, boolean isLastChild,
			ViewGroup parent) {
		//do nothing Aufgabe wird von getChildView übernommen
		return null;
	}

	@Override
	protected View newGroupView(Context context,
			android.database.Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View groupView = inflater.inflate(R.layout.own_list_group_item, parent, false);
        ListViewParentHolder holder = new ListViewParentHolder();
        holder.parent_layout = (LinearLayout)groupView.findViewById(R.id.linearlayoutheader);
        holder.list_header_gipfel = (TextView)groupView.findViewById(R.id.list_header_gipfel);
        holder.list_header_schwierigkeit = (TextView)groupView.findViewById(R.id.list_header_schwierigkeit);
        holder.list_header_wegname = (TextView)groupView.findViewById(R.id.list_header_wegname);
        holder.list_header_hinweis = (TextView)groupView.findViewById(R.id.list_header_hinweis);
        holder.buttonDatum = (Button)groupView.findViewById(R.id.button_datum);
        holder.imageViewWorld = (ImageView)groupView.findViewById(R.id.image_view_world);
        holder.expandedImage = (ImageView)groupView.findViewById(R.id.group_expanded_image);
        groupView.setTag(holder);
        return groupView;
	}
		
	@Override
	public View getChildView (int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final ListViewChildHolder holder;
		
		if (convertView==null) {
	        convertView = inflater.inflate(R.layout.own_list_details , parent, false);
	        holder = new ListViewChildHolder();
	        holder.checkBoxoU = (CheckBox)convertView.findViewById(R.id.checkBoxoU);
	        holder.checkBoxRP = (CheckBox)convertView.findViewById(R.id.checkBoxRP);
	        holder.checkBoxVorstieg = (CheckBox)convertView.findViewById(R.id.checkBoxVorstieg);
	        holder.imageButtonEintragLoeschen = (ImageButton)convertView.findViewById(R.id.imageButtonEintragLoeschen);
	        holder.editTextBemerkungen = (EditText)convertView.findViewById(R.id.editTextBemerkungen);
	        holder.editTextGeklettertMit = (EditText)convertView.findViewById(R.id.editTextGeklettertMit);
	        convertView.setTag(holder);
		} else {
			holder = (ListViewChildHolder)convertView.getTag();
		}
		
		final EigenerWeg eigenerWeg = getEigenerWeg(getGroup(groupPosition));
		holder.checkBoxVorstieg.setChecked(eigenerWeg.isVorstieg());
    	holder.checkBoxVorstieg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isChecked = holder.checkBoxVorstieg.isChecked();
				eigenerWeg.setVorstieg(isChecked);
				notifyDataSetChanged();
			}
		});
    	
    	holder.checkBoxRP.setChecked(eigenerWeg.isRP());
    	holder.checkBoxRP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isChecked = holder.checkBoxRP.isChecked();
				eigenerWeg.setRP(isChecked);
				notifyDataSetChanged();				
			}
		});
    	
    	if (!eigenerWeg.hasoU()) {
    		holder.checkBoxoU.setVisibility(View.GONE);
    	} else {
    		holder.checkBoxoU.setVisibility(View.VISIBLE);
    		holder.checkBoxoU.setChecked(eigenerWeg.isoU());
    		holder.checkBoxoU.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked = holder.checkBoxoU.isChecked();
					eigenerWeg.setoU(isChecked);
					notifyDataSetChanged();	
				}
			});
    	}    	
    	
		holder.editTextGeklettertMit.setText(eigenerWeg.getGeklettertMit());
		holder.editTextGeklettertMit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eigenerWeg.showTextEditor(thisadapter);
			}
		});
		holder.editTextBemerkungen.setText(eigenerWeg.getBemerkungen());
		holder.editTextBemerkungen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eigenerWeg.showTextEditor(thisadapter);
			}
		});
		
		holder.imageButtonEintragLoeschen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eigenerWeg.setOnRemoveOwnListItem(new OnRemoveItemListener() {
					
					@Override
					public void onRemoveOnwListItem() {
						ActionBar ab = ((ActionBarActivity)thisActivity).getSupportActionBar();
						String subtitle = (String)ab.getSubtitle();
						String[] anzahlWege = subtitle.split("\\s");
						Integer anzahl = Integer.parseInt(anzahlWege[0])-1;
						ab.setSubtitle(anzahl.toString() + " " + 
						thisActivity.getString(R.string.geklettert));
						notifyDataSetChanged();
					}
				});
				eigenerWeg.remove(thisadapter);
			}
		});
		
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}
	
//-----------------------------------------------------------------------------------------
//	
//			Nicht Adapterspezifisches Zeuch
//	
//-----------------------------------------------------------------------------------------

	public void openLiveKarte() {
		if (KleFuContract.mapFileExists()) {
			Intent intent = new Intent(thisActivity, LiveKarteActivity.class);
			thisActivity.startActivity(intent);
		} else {
			karteHerunterladenClick();
		}		
	}		
	
	public void openLiveKarte(GeoPoint center, String gipfel) {
		if (KleFuContract.mapFileExists()) {
			Intent intent = new Intent (thisActivity, LiveKarteActivity.class);				
			intent.putExtra(KleFuEntry.COLUMN_NAME_GIPFEL, gipfel);
			intent.putExtra(KleFuEntry.BREITE, center.getLatitude());
			intent.putExtra(KleFuEntry.HOHE, center.getLongitude());
			thisActivity.startActivity(intent);			
		} else {
			karteHerunterladenClick();
		}		
	}	

	
	public void karteHerunterladenClick() {
    	ActionBarAppActivity.builder = new AlertDialog.Builder(thisActivity);
    	ActionBarAppActivity.builder.setTitle(thisActivity.getString(R.string.download_kartendownload));
		String messageString=thisActivity.getString(R.string.download_kartendownload_desc);
		ActionBarAppActivity.builder.setMessage(Html.fromHtml(messageString));
		ActionBarAppActivity.builder.setIcon(R.drawable.ic_action_questionmark);
		ActionBarAppActivity.builder.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					karteHerunterladen(); 
				}
			}
		);
		ActionBarAppActivity.builder.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
    		}
		);
		ActionBarAppActivity.builder.show();
	}
	
	public void karteHerunterladen() {
        activityHandler = new DownloadHandler(thisActivity, this, downloaderThread);	        
    	downloaderThread = new DownloaderThread(thisActivity, this);
        downloaderThread.start();
	}
	
	public static class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
			
	public DatePickerFragment() {}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
	// Create a new instance of DatePickerDialog and return it
	return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {    
//		Datenbankupdate auf neues Datum
		Integer id = (Integer)dateButton.getTag();//id der eigenen Wegeliste holen
    	Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
              
		long timestamp =  calendar.getTimeInMillis();
		ContentValues values = new ContentValues();
		values.put(KleFuEntry.COLUMN_NAME_DATUM, timestamp);
		String whereClause=KleFuEntry._ID + " LIKE ?";
		String[] whereArgs={(id.toString())};
		KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				values, whereClause, whereArgs);
		CustomCursorTreeAdapter.thisadapter.notifyDataSetChanged();
	}
	}
	
	
	@Override
	public void notifyDataSetChanged() {
		((ActionBarActivity)thisActivity).getSupportLoaderManager().restartLoader(0, null, loaderCallbacks);				
	}
}

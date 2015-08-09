package de.climbingguide.erzgebirsgrenzgebiet.list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.mapsforge.core.model.LatLong;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.ActionBarAppActivity;
import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.EigenerWeg;
import de.climbingguide.erzgebirsgrenzgebiet.EigenerWeg.OnRemoveItemListener;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.TextToSpeechWeg;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloadHandler;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.Downloader;
import de.climbingguide.erzgebirsgrenzgebiet.downloader.DownloaderThread;
import de.climbingguide.erzgebirsgrenzgebiet.maps.LiveKarteActivity;
import net.sqlcipher.Cursor;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements
	Downloader, 
	OnInitListener{
	
	protected static int year, month, day;
	protected static Button dateButton; 
	
	private static class WegGeklettertHolder {
		protected LinearLayout linearLayoutEigenerEintrag;
		protected TextView textViewDatum;
		protected TextView textViewHinweis;
		protected LinearLayout linearLayoutEigenerEintragDetails;
		protected Button buttonDatum;
		protected CheckBox checkBoxVorstieg;
		protected CheckBox checkBoxRP;
		protected CheckBox checkBoxoU;
		protected EditText editTextGeklettertMit;
		protected EditText editTextBemerkungen;
		protected ImageButton imageButtonEintragLoeschen;
		protected TextView textViewHinweisZwei;
	}
	
	private TextToSpeechWeg tts;
	private ArrayList<ImageButton> buttonArrayList;	
	private boolean isTTSinited;
	
	//Filedownload Zeug
	public DownloadHandler activityHandler;
	public DownloadHandler getDownloadHandler() { return activityHandler; }	
	private DownloaderThread downloaderThread;
	public DownloaderThread getDownloaderThread() { return downloaderThread; }
	
//    private Context context;
    private Activity thisActivity;
       
    private ArrayList<Gipfel> listDataHeader; // header titles
    // child data in format of header title, child title
    private ArrayList<ArrayList<Weg>> listDataChild;
    protected static Gipfel headerGipfel;
    
    private LayoutInflater infalInflater;
    protected static ExpandableListAdapter thisadapter;
    
    private SparseArray<EigenerWeg> eigeneWegHolder = new SparseArray<EigenerWeg>();
    
    @SuppressWarnings("static-access")
	public ExpandableListAdapter(Activity activity) {
        this.thisActivity = activity;
//    	this.context = activity.getApplicationContext(); 
        this.listDataHeader = KleFuContract.listDataHeader;
        this.listDataChild = KleFuContract.listDataChild;
        this.thisadapter=this;
		tts = new TextToSpeechWeg(thisActivity, this);
		buttonArrayList = new ArrayList<ImageButton>();
		
        infalInflater = (LayoutInflater) thisActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }   
    
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(groupPosition).get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, final ViewGroup parent) {
    	
    	final ListViewChildHolder listViewHolder;
    	
        final Weg childWeg = (Weg) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            convertView = infalInflater.inflate(R.layout.explistview_items, null);
            
            listViewHolder = new ListViewChildHolder();
            listViewHolder.textViewWegname = (TextView) convertView
                    .findViewById(R.id.list_item_weg);
            listViewHolder.textViewSchwierigkeit = (TextView) convertView
                    .findViewById(R.id.list_item_schwierigkeit);
            listViewHolder.checkBoxGeklettert = (CheckBox) convertView
            		.findViewById(R.id.checkBoxWegGeklettert);
            listViewHolder.nonDetailsLayout = (RelativeLayout) convertView
            		.findViewById(R.id.nonDetailsLayout);
            listViewHolder.detailsLayout = (LinearLayout) convertView
            		.findViewById(R.id.detailsLayout);

            listViewHolder.buttonDetVorlesen = (ImageButton) convertView
            		.findViewById(R.id.buttonvorlesendet);              
            listViewHolder.textViewDetWeg = (TextView) convertView
            		.findViewById(R.id.wegdet);            
            listViewHolder.textViewDetSchwierigkeit = (TextView) convertView
            		.findViewById(R.id.schwierigkeitdet);  
            listViewHolder.textViewDetSchwierigkeit = (TextView) convertView
            		.findViewById(R.id.schwierigkeitdet);
            listViewHolder.textViewDetBeschreibung = (TextView) convertView
            		.findViewById(R.id.beschreibungdet);  
            listViewHolder.checkBoxDetBereitsGeklettert = (CheckBox) convertView
            		.findViewById(R.id.checkBoxBereitsGeklettertdet);  
            listViewHolder.checkBoxDetVorstieg = (CheckBox) convertView
            		.findViewById(R.id.checkBoxVorstiegdet);  
            listViewHolder.checkBoxDetRP = (CheckBox) convertView
            		.findViewById(R.id.checkBoxRPdet);
            listViewHolder.checkBoxoU = (CheckBox) convertView
            		.findViewById(R.id.checkBoxoOdet);

            listViewHolder.linearLayoutGeklettert = (LinearLayout) convertView
            		.findViewById(R.id.linearlayoutgeklettertam);
            
            convertView.setTag(listViewHolder);
        } else {
        	listViewHolder = (ListViewChildHolder) convertView.getTag();
        }
                
        if (!childWeg.isShownDetailed()) {
	        listViewHolder.detailsLayout.setVisibility(View.GONE);
	        listViewHolder.nonDetailsLayout.setVisibility(View.VISIBLE);
	        listViewHolder.textViewWegname.setText(Html.fromHtml(childWeg.getHtmlWegname()));
	        listViewHolder.textViewSchwierigkeit.setText(Html.fromHtml(childWeg.getHtmlSchwierigkeit()));
	        
        	listViewHolder.checkBoxGeklettert.setChecked(childWeg.isGeklettert());
	        listViewHolder.checkBoxGeklettert.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked = listViewHolder.checkBoxGeklettert.isChecked();
					removeWeg(listViewHolder, childWeg, isChecked);
				}
			});
        } else {
	        listViewHolder.nonDetailsLayout.setVisibility(View.GONE);       
	        listViewHolder.detailsLayout.setVisibility(View.VISIBLE);        		        
        	listViewHolder.textViewDetWeg.setText(Html.fromHtml(childWeg.getHtmlWegname()));        	
        	listViewHolder.textViewDetBeschreibung.setText(Html.fromHtml(childWeg.getPostBeschreibung()));        	
        	listViewHolder.textViewDetSchwierigkeit.setText(Html.fromHtml(childWeg.getHtmlSchwierigkeit()));        	
        	listViewHolder.checkBoxDetBereitsGeklettert.setChecked(childWeg.isGeklettert());
        	listViewHolder.checkBoxDetBereitsGeklettert.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked = listViewHolder.checkBoxDetBereitsGeklettert.isChecked();
					removeWeg(listViewHolder, childWeg, isChecked);
				}
			});

        	listViewHolder.checkBoxDetVorstieg.setChecked(childWeg.isVorgestiegen());
        	listViewHolder.checkBoxDetVorstieg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked = listViewHolder.checkBoxDetVorstieg.isChecked();
					childWeg.setVorgestiegen(isChecked);
					notifyDataSetChanged();
				}
			});

        	listViewHolder.checkBoxDetRP.setChecked(childWeg.isRP());
        	listViewHolder.checkBoxDetRP.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked = listViewHolder.checkBoxDetRP.isChecked();					
					childWeg.setRP(isChecked);
					notifyDataSetChanged();						
				}
			});
        	
        	if (!childWeg.hasoU()) {
        		listViewHolder.checkBoxoU.setVisibility(View.GONE);
        	} else {
        		listViewHolder.checkBoxoU.setChecked(childWeg.isoU());
        		listViewHolder.checkBoxoU.setVisibility(View.VISIBLE);
        		listViewHolder.checkBoxoU.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean isChecked = listViewHolder.checkBoxoU.isChecked();											
						childWeg.setoU(isChecked);
						notifyDataSetChanged();
					}
        		});
        	}
        	
        	if (!isTTSinited) {
	    		listViewHolder.buttonDetVorlesen.setClickable(false);
	    		listViewHolder.buttonDetVorlesen.setEnabled(false);
	    		listViewHolder.buttonDetVorlesen.setImageDrawable(thisActivity.getResources().getDrawable(R.drawable.ic_action_play_deactivated));
	    		if (buttonArrayList!=null) buttonArrayList.add(listViewHolder.buttonDetVorlesen);
        	} else {
        		listViewHolder.buttonDetVorlesen.setClickable(true);
        		listViewHolder.buttonDetVorlesen.setEnabled(true);  
	    		listViewHolder.buttonDetVorlesen.setImageDrawable(thisActivity.getResources().getDrawable(R.drawable.ic_action_play));
        	}
    		listViewHolder.buttonDetVorlesen.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				tts.speak(childWeg);
    			}
    		});
    		
    		//Zeuch zur geklettert am Liste
    		if (childWeg.isGeklettert()) {
    			listViewHolder.linearLayoutGeklettert.setVisibility(View.VISIBLE);
    			//Datenbankabfrage nach Details der gekletterten Wege
    			String[] projection = {
    				    KleFuEntry.COLUMN_NAME_DATUM,//0
    				    KleFuEntry.COLUMN_NAME_VORSTIEG,//1
    				    KleFuEntry.COLUMN_NAME_RP,//2
    				    KleFuEntry.COLUMN_NAME_OU,//3
    				    KleFuEntry._ID,//4
    				    KleFuEntry.COLUMN_NAME_PERSONEN,//5
    				    KleFuEntry.COLUMN_NAME_BEMERKUNGEN,//6
    				    KleFuEntry.COLUMN_NAME_IS_EXPANDED//7
    				};
    				
    				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID  + " LIKE ?";
    				
    				String[] whereArgs = { childWeg.getWegeid().toString() };
    				
    				String orderBy = KleFuEntry.COLUMN_NAME_DATUM;
    				
    				// Gebiete aus Datenbank lesen
    				Cursor c = KleFuEntry.db.query(
    				    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
    				    projection,                               // The columns to return
    				    whereClause,                                // The columns for the WHERE clause
    				    whereArgs,                            // The values for the WHERE clause
    				    null,                                     // don't group the rows
    				    null,                                     // don't filter by row groups
    				    orderBy                                 // The sort order
    				    );

    				c.moveToFirst();
    				
    				int cursorPosition = -1;
    				
    				while (!c.isAfterLast()) {
    					int eigeneWegeId = c.getInt(4);
        				if (eigeneWegHolder.get(eigeneWegeId) == null) {
        					eigeneWegHolder.put(eigeneWegeId,  setupEigeneWegeHolder(c, childWeg));
        				}
        				final EigenerWeg eigenerWeg = eigeneWegHolder.get(eigeneWegeId);
    					final WegGeklettertHolder wegGeklettertHolder;    	    			 				    					    					
    					cursorPosition = c.getPosition();
    					View v;
    					
						v = (View)listViewHolder.linearLayoutGeklettert.findViewById(cursorPosition);
						if (v == null) {
    	    				wegGeklettertHolder = new WegGeklettertHolder();
        					v = infalInflater.inflate(R.layout.list_wanngeklettert, null);
        					wegGeklettertHolder.linearLayoutEigenerEintrag = (LinearLayout)v.findViewById(R.id.linearLayoutEigenerEintrag);
        					wegGeklettertHolder.linearLayoutEigenerEintragDetails = (LinearLayout)v.findViewById(R.id.linearLayoutEigenerEintragDetails);
        					if (!eigenerWeg.isExpanded()) {
        						setupWegGeklettertHolder(wegGeklettertHolder, v);
        					} else {
        						setupWegGeklettertHolderDetails(
										wegGeklettertHolder, v);
        					}
        					listViewHolder.linearLayoutGeklettert.addView(v);
        					v.setId(cursorPosition);
        					v.setTag(wegGeklettertHolder);
	    	    		} else {
    						v.setVisibility(View.VISIBLE);		    	    			
    	    				wegGeklettertHolder = (WegGeklettertHolder)v.getTag();
        					if (!eigenerWeg.isExpanded() && wegGeklettertHolder.textViewDatum == null) {
        						setupWegGeklettertHolder(wegGeklettertHolder, v);
        					} else if (wegGeklettertHolder.buttonDatum == null) {
        						setupWegGeklettertHolderDetails(
										wegGeklettertHolder, v);
        					}
    					}  					    					
    					v.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {								
								eigenerWeg.swapExpanded();
								v.setBackgroundColor(Color.TRANSPARENT);
								notifyDataSetChanged();
							}
						});
    					
    					v.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								switch (event.getAction()) {
									case MotionEvent.ACTION_DOWN:
										v.setBackgroundColor(Color.YELLOW);
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
						
    					
    					
//    						Zeuch zu den normalen Weg geklettert
    					if (!eigenerWeg.isExpanded()) {
    						wegGeklettertHolder.linearLayoutEigenerEintrag.setVisibility(View.VISIBLE);
    						wegGeklettertHolder.linearLayoutEigenerEintragDetails.setVisibility(View.GONE);    						    						
	
        					wegGeklettertHolder.textViewDatum.setText(
        							thisActivity.getString(R.string.am) + " " +
        									eigenerWeg.getDatumString());
        					
        					setTextHinweis(wegGeklettertHolder.textViewHinweis, eigenerWeg.isVorstieg(),
        							eigenerWeg.isRP(), eigenerWeg.isoU());        					
    					
        					//Zeuch zu den Weg geklettertDetails
    					} else {        					
       					
    						wegGeklettertHolder.linearLayoutEigenerEintrag.setVisibility(View.GONE);
    						wegGeklettertHolder.linearLayoutEigenerEintragDetails.setVisibility(View.VISIBLE);    						

        					setTextHinweis(wegGeklettertHolder.textViewHinweisZwei, eigenerWeg.isVorstieg(),
        							eigenerWeg.isRP(), eigenerWeg.isoU());
        					
        					wegGeklettertHolder.imageButtonEintragLoeschen.setOnClickListener(new OnClickListener() {
        						
								@Override
								public void onClick(View v) {
									eigenerWeg.setOnRemoveOwnListItem(new OnRemoveItemListener() {
										
										@Override
										public void onRemoveOnwListItem() {
											childWeg.sync();
											notifyDataSetChanged();											
										}
									});
									eigenerWeg.remove(thisadapter);
									
								}
							});

        					wegGeklettertHolder.buttonDatum.setText(eigenerWeg.getDatumString());

							wegGeklettertHolder.buttonDatum.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Calendar calendar = Calendar.getInstance();
							        calendar.setTimeInMillis(eigenerWeg.getDatum());
									day = calendar.get(Calendar.DAY_OF_MONTH);
									month = calendar.get(Calendar.MONTH);
									year = calendar.get(Calendar.YEAR);
									wegGeklettertHolder.buttonDatum.setTag(eigenerWeg.getEigeneWegId());
									showDatePickerDialog(wegGeklettertHolder.buttonDatum);
								}
							});
							
							wegGeklettertHolder.checkBoxVorstieg.setChecked(eigenerWeg.isVorstieg());
							wegGeklettertHolder.checkBoxVorstieg.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									boolean isChecked = wegGeklettertHolder.checkBoxVorstieg.isChecked();					
									eigenerWeg.setVorstieg(isChecked);
									childWeg.sync();
									notifyDataSetChanged();									
								}
							});

							wegGeklettertHolder.checkBoxRP.setChecked(eigenerWeg.isRP());
							wegGeklettertHolder.checkBoxRP.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									boolean isChecked = wegGeklettertHolder.checkBoxRP.isChecked();					
									eigenerWeg.setRP(isChecked);
									childWeg.sync();
									notifyDataSetChanged();									
								}
							});
							
							if (childWeg.hasoU()) {
								wegGeklettertHolder.checkBoxoU.setVisibility(View.VISIBLE);
								wegGeklettertHolder.checkBoxoU.setChecked(eigenerWeg.isoU());
								wegGeklettertHolder.checkBoxoU.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										boolean isChecked = wegGeklettertHolder.checkBoxoU.isChecked();					
										eigenerWeg.setoU(isChecked);
										childWeg.sync();
										notifyDataSetChanged();										
									}
								});
							} else {
								wegGeklettertHolder.checkBoxoU.setVisibility(View.GONE);
							}
							
							wegGeklettertHolder.editTextGeklettertMit.setText(eigenerWeg.getGeklettertMit());						
							wegGeklettertHolder.editTextGeklettertMit.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									eigenerWeg.showTextEditor(thisadapter);									
								}
							});
							
							wegGeklettertHolder.editTextBemerkungen.setText(eigenerWeg.getBemerkungen());
							wegGeklettertHolder.editTextBemerkungen.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									eigenerWeg.showTextEditor(thisadapter);									
								}
							});
						}									    					
    					c.moveToNext();
    				}
					for (int i=cursorPosition+1; i<listViewHolder.linearLayoutGeklettert.getChildCount(); i++) {
						listViewHolder.linearLayoutGeklettert.getChildAt(i).setVisibility(View.GONE);
					}
    				c.close(); 
    		} else {
    			listViewHolder.linearLayoutGeklettert.setVisibility(View.GONE);
    		}

        }                       
        return convertView; 
    }

	private void setTextHinweis(final TextView textViewHinweis,
			final boolean isVorgestiegen, final boolean isRP, final boolean isoU) {
		ClimbingGuideApplication.setEigeneWegeHinweis(textViewHinweis, isVorgestiegen, isRP, isoU);
	}

	private EigenerWeg setupEigeneWegeHolder(Cursor c, Weg childWeg) {
		final Integer id = c.getInt(4);
		final boolean isVorgestiegen = c.getInt(1)>0;
		final boolean isRP = c.getInt(2)>0;
		final boolean isoU = c.getInt(3)>0;
		final Boolean isExpanded = c.getInt(7)>0;
		final long currenttimemillis=c.getLong(0);
		final String personen = c.getString(5);
		final String bemerkungen = c.getString(6);   
		return new EigenerWeg(childWeg, id, isVorgestiegen, isRP, isoU, isExpanded, personen, bemerkungen, currenttimemillis);
	}	
	
	private void setupWegGeklettertHolderDetails(
			final WegGeklettertHolder wegGeklettertHolder, View v) {
		wegGeklettertHolder.buttonDatum = (Button)v.findViewById(R.id.buttonDatum);     			
		wegGeklettertHolder.checkBoxVorstieg = (CheckBox)v.findViewById(R.id.checkBoxVorstieg);
		wegGeklettertHolder.checkBoxRP = (CheckBox)v.findViewById(R.id.checkBoxRP);
		wegGeklettertHolder.checkBoxoU = (CheckBox)v.findViewById(R.id.checkBoxoU);
		wegGeklettertHolder.editTextGeklettertMit = (EditText)v.findViewById(R.id.editTextGeklettertMit);
		wegGeklettertHolder.editTextBemerkungen = (EditText)v.findViewById(R.id.editTextBemerkungen);
		wegGeklettertHolder.imageButtonEintragLoeschen = (ImageButton)v.findViewById(R.id.imageButtonEintragLoeschen);
		wegGeklettertHolder.textViewHinweisZwei = (TextView)v.findViewById(R.id.textViewHinweisZwei);
		
	}

	private void setupWegGeklettertHolder(
			final WegGeklettertHolder wegGeklettertHolder, View v) {
		wegGeklettertHolder.textViewDatum = (TextView)v.findViewById(R.id.textViewdatum);
		wegGeklettertHolder.textViewHinweis = (TextView)v.findViewById(R.id.textViewHinweis);
	}
	
	protected void removeWeg(
			final ListViewChildHolder listViewHolder,
			final Weg childWeg,final boolean isChecked) {
		if (!isChecked) {
	    	ActionBarAppActivity.builder = new AlertDialog.Builder(thisActivity);
	    	ActionBarAppActivity.builder.setTitle(thisActivity.getString(R.string.nicht_geklettert_markieren_title));
			String messageString=thisActivity.getString(R.string.nicht_geklettert_markieren_desc);
			ActionBarAppActivity.builder.setMessage(Html.fromHtml(messageString));
			ActionBarAppActivity.builder.setIcon(R.drawable.ic_action_questionmark);
			ActionBarAppActivity.builder.setPositiveButton(R.string.ja,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						childWeg.setGeklettert(false);
						notifyDataSetChanged();
					}
				}
			);
			ActionBarAppActivity.builder.setNegativeButton(R.string.nein,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						listViewHolder.checkBoxGeklettert.setChecked(true);
						listViewHolder.checkBoxDetBereitsGeklettert.setChecked(true);
					}
	    		}
			);
			ActionBarAppActivity.builder.show();
		} else {
			childWeg.setGeklettert(true);
			notifyDataSetChanged();
		}
	}	
    
    @Override
    public int getChildrenCount(int groupPosition) {
    	ArrayList<Weg> arrayListWeg = this.listDataChild.get(groupPosition);
    	int size = arrayListWeg.size();
        return size;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	
        headerGipfel = (Gipfel) getGroup(groupPosition);
        ListViewParentHolder listViewParentHolder;
        
        if (convertView == null) {
            convertView = infalInflater.inflate(R.layout.explistview_headers, null);
            
            listViewParentHolder = new ListViewParentHolder();
            listViewParentHolder.list_header_gipfel = (TextView) convertView
                    .findViewById(R.id.list_header_gipfel);
            listViewParentHolder.list_header_gebiet = (TextView) convertView
                    .findViewById(R.id.list_header_gebiet);
            listViewParentHolder.list_header_gebiet.setTypeface(null, Typeface.ITALIC);

            listViewParentHolder.buttonKarte = (Button) convertView.findViewById(R.id.buttonShowGipfel);
            listViewParentHolder.parent_layout = (RelativeLayout) convertView.findViewById(R.id.explist_parent);
            listViewParentHolder.expandedImage = (ImageView) convertView.findViewById(R.id.group_expanded_image);       
            
            convertView.setTag(listViewParentHolder);
        } else {
        	listViewParentHolder = (ListViewParentHolder) convertView.getTag();
        }
 
        listViewParentHolder.list_header_gipfel.setText(Html.fromHtml(headerGipfel.getHtmlGipfelnummerPlusGipfel()));
        
        String gebiet = headerGipfel.getGebiet();
        // Gebietname auf 14 Zeichen begrenten
        if (gebiet.length() > 14) {
        	gebiet=gebiet.subSequence(0, 12).toString() + ".";
        }               
        listViewParentHolder.list_header_gebiet.setText(gebiet);
        
        listViewParentHolder.buttonKarte.setOnClickListener(new OnClickListener() {        				
			private Gipfel gipfel=headerGipfel;
        	
        	@Override
			public void onClick(View v) {
        		LiveKarteActivity.setMapWasCentered(false);
				openLiveKarte(gipfel.getLatLong(), gipfel.getGipfel());				
			}						
		}        		
        );
        
        if (headerGipfel.isBestiegen()) {
        	listViewParentHolder.parent_layout.setBackgroundColor(thisActivity.getResources().getColor(R.color.green));
        	int white = thisActivity.getResources().getColor(R.color.white);
        	listViewParentHolder.list_header_gebiet.setTextColor(white);
        	listViewParentHolder.list_header_gipfel.setTextColor(white);
        } else {
        	listViewParentHolder.parent_layout.setBackgroundColor(thisActivity.getResources().getColor(R.color.lightblue));
        	int black = thisActivity.getResources().getColor(R.color.black);
        	listViewParentHolder.list_header_gebiet.setTextColor(black);
        	listViewParentHolder.list_header_gipfel.setTextColor(black);
        }
        
		final int resId = isExpanded ? R.drawable.minus : R.drawable.plus;
		listViewParentHolder.expandedImage.setImageResource(resId);     
				
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    @Override
    public void notifyDataSetChanged() {
    	eigeneWegHolder.clear();
//    	v.notifyDataSetChanged();    	
    	super.notifyDataSetChanged();    	
    }
	
	public void openLiveKarte() {
		if (KleFuContract.mapFileExists()) {
			Intent intent = new Intent(thisActivity, LiveKarteActivity.class);
			thisActivity.startActivity(intent);
		} else {
			karteHerunterladenClick();
		}		
	}		
	
	public void openLiveKarte(LatLong center, String gipfel) {
		if (KleFuContract.mapFileExists()) {
			Intent intent = new Intent (thisActivity, LiveKarteActivity.class);				
			intent.putExtra(KleFuEntry.COLUMN_NAME_GIPFEL, gipfel);
			intent.putExtra(KleFuEntry.BREITE, center.latitude);
			intent.putExtra(KleFuEntry.HOHE, center.longitude);
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

	@Override
	public void onInit(int status) {
		int languageAvailable = tts.setLanguage(Locale.GERMAN);
		if (
			(languageAvailable == TextToSpeech.LANG_AVAILABLE) ||
			(languageAvailable == TextToSpeech.LANG_COUNTRY_AVAILABLE) ||
			(languageAvailable == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE)
		) {
			for (ImageButton button : buttonArrayList) {
				button.setClickable(true);
        		button.setEnabled(true);
	    		button.setImageDrawable(thisActivity.getResources().getDrawable(R.drawable.ic_action_play));
			}
			buttonArrayList=null;
			if (status==TextToSpeech.SUCCESS) isTTSinited=true;
		} else {
			Toast.makeText(thisActivity, thisActivity.getString(R.string.tts_not_installed), Toast.LENGTH_SHORT).show();
		}
	}

	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    dateButton = (Button)v;
	    newFragment.show(((FragmentActivity)thisActivity).getSupportFragmentManager(), "datePicker");
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
		ExpandableListAdapter.thisadapter.notifyDataSetChanged();
	}
	}	
	
}



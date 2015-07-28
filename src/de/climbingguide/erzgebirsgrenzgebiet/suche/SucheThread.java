package de.climbingguide.erzgebirsgrenzgebiet.suche;

/**
 * Copyright (c) 2011 Mujtaba Hassanpur.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.ArrayList;

import net.sqlcipher.Cursor;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Message;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;
import de.climbingguide.erzgebirsgrenzgebiet.Weg;

/**
 * Downloads a file in a thread. Will send messages to the
 * MainActivity activity to update the progress bar.
 */
public class SucheThread extends Thread
{		      
        // instance variables
        private Activity parentActivity;
        private SucheHandler sucheHandler;
        private Sucher thisSucher;
        
        private ArrayList<Weg> arrayListSearchResult;
        private ArrayList<Gipfel> listDataHeader; // header titles
        // child data in format of header title, child title
        private ArrayList<ArrayList<Weg>> listDataChild;
       
        /**
         * Instantiates a new DownloaderThread object.
         * @param parentActivity Reference to MainActivity activity.
         * @param inUrl String representing the URL of the file to be downloaded.
         */
        public SucheThread(Activity inParentActivity, Sucher thisSucher)
        {
                parentActivity = inParentActivity;
                sucheHandler = thisSucher.getSucheHandler();
                this.thisSucher = thisSucher;
        }      

		/**
         * Connects to the URL of the file, begins the download, and notifies the
         * MainActivity activity of changes in state. Writes the file to
         * the root of the SD card.
         */
        @Override
        public void run()
        {        	       	
                Message msg;
                Cursor c=null;
               
                // we're going to search now
                msg = Message.obtain(sucheHandler,
                                KleFuEntry.MESSAGE_SEARCH_STARTED,
                                0, 0, "");
                sucheHandler.sendMessage(msg);
               
                try
                {
                	//Infragekommende Gipfel suchen
    				String[] projection = {
    							KleFuEntry._ID
    				};
    				// String für die Where Options
    				ArrayList<String> arrayListWhereOptions=new ArrayList<String>();
    				
    				// String für die Where Clause				
    				String whereClause="";
    				if (thisSucher.getGebietBekannt()) {
    					whereClause=KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ?";
    					arrayListWhereOptions.add(thisSucher.getGebiet());
    				}
    				
    				Integer gipfelnummerBis = thisSucher.getGipfelnummerBis();
    				if (gipfelnummerBis == 0) {
    					if (thisSucher.getGipfelBekannt()) {
    						if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";
    						whereClause = whereClause + KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ?";
    						arrayListWhereOptions.add(thisSucher.getGipfel());
    					} else {
    						if (!(thisSucher.getGipfelnummer() == 0)) {
    							if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";
    							whereClause = whereClause + KleFuEntry.COLUMN_NAME_GIPFELNUMMER + " LIKE ?";
    							arrayListWhereOptions.add(thisSucher.getGipfelnummer().toString());
    						}
    					}
    				} else { 
    					// von bis Abfrage nach Gipfelnummern
    					if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";
    					whereClause = whereClause + KleFuEntry.COLUMN_NAME_GIPFELNUMMER + " BETWEEN ?" + " AND ?";
    					arrayListWhereOptions.add(thisSucher.getGipfelnummer().toString());
    					arrayListWhereOptions.add(gipfelnummerBis.toString());	
    				}
    				    				    				    				    				
    				String[] whereOptions = new String[arrayListWhereOptions.size()];
    				arrayListWhereOptions.toArray(whereOptions);
    				
    				if (whereClause == "") {
    					whereClause = null;
    					whereOptions = null;
    				}
    				
    				// Gebiete aus Datenbank lesen
    				c = KleFuEntry.db.query(
    				    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
    				    projection,           // The columns to return
    				    whereClause,                                // The columns for the WHERE clause
    				    whereOptions,                            // The values for the WHERE clause
    				    null,                                     // don't group the rows
    				    null,                                     // don't filter by row groups
    				    null                                 // The sort order
    				    );

    				// String aus Cursor lesen

    				ArrayList<Integer> arrayListSearchResultGipfel = new ArrayList<Integer>(); 

    				c.moveToFirst();    				    				
    				while (c.isAfterLast() == false) {
    					arrayListSearchResultGipfel.add(c.getInt(0));
    					c.moveToNext();
    				}                	                	                	                	
    				c.close();
    				
                	//Datenbankabfrage, ob eingetragener Gipfel Element des Gebietes, wenn nicht dann löschen
    				// Define a projection that specifies which columns from the database
    				// you will actually use after this query.    				
    				String[] projection2 = {
    							KleFuEntry._ID,//0
    					        KleFuEntry.COLUMN_NAME_GIPFELID,//1
    					        KleFuEntry.COLUMN_NAME_WEGNAME,//2
    					        KleFuEntry.COLUMN_NAME_BESCHREIBUNG,//3
    					        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF,//4
    					        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU,//5
    					        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP,//6
    					        KleFuEntry.COLUMN_NAME_ERSTBEGEHER1,//7
    					        KleFuEntry.COLUMN_NAME_ERSTBEGEHER2,//8
    					        KleFuEntry.COLUMN_NAME_ERSTBEGEHER3,//9
    					        KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE,//10
    					        KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM,//11
    					        KleFuEntry.COLUMN_NAME_STERN,//12
    					        KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN,//13
    					        KleFuEntry.COLUMN_NAME_GEKLETTERT,//14
    					        KleFuEntry.COLUMN_NAME_IS_EXPANDED//15    					        /14
    				};
    				// String für die Where Options
    				arrayListWhereOptions.clear();
    				
    				// String für die Where Clause				
    				whereClause="(";
    				for (Integer gipfelId : arrayListSearchResultGipfel) {
						if (!(whereClause.equals("("))) whereClause = whereClause + " OR ";
    					whereClause=whereClause + KleFuEntry.COLUMN_NAME_GIPFELID + " LIKE ?";
    					arrayListWhereOptions.add(gipfelId.toString());
    				}
    				whereClause = whereClause+")";
    				    				
    				Integer schwierigkeitBis = thisSucher.getSchwierigkeitBisInt();
    				Integer schwierigkeitVon = thisSucher.getSchwierigkeitVonInt();
    				if (schwierigkeitBis == 0) {
    					if (!(schwierigkeitVon == 0)) {
    						if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";						
    						whereClause = whereClause + "(" +
    								KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF + " LIKE ?" +
    								" OR " + KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU + " LIKE ?" + 
    								" OR " + KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP + " LIKE ?" +  
    								")";
    						arrayListWhereOptions.add(schwierigkeitVon.toString());
    						arrayListWhereOptions.add(schwierigkeitVon.toString());
    						arrayListWhereOptions.add(schwierigkeitVon.toString());
    					}
    				} else {
    					if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";					
    					whereClause = whereClause + "(" +
    							KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF + " BETWEEN ?" + " AND ?" +
    							" OR " + KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU + " BETWEEN ?" + " AND ?" +
    							" OR " + KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP + " BETWEEN ?" + " AND ?" + 
    							")";					
    					arrayListWhereOptions.add(schwierigkeitVon.toString());
    					arrayListWhereOptions.add(schwierigkeitBis.toString());
    					arrayListWhereOptions.add(schwierigkeitVon.toString());
    					arrayListWhereOptions.add(schwierigkeitBis.toString());
    					arrayListWhereOptions.add(schwierigkeitVon.toString());
    					arrayListWhereOptions.add(schwierigkeitBis.toString());
    				}
    				
    				
    				if (!thisSucher.getWegname().equals("")) {
    					if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";					
    					whereClause = whereClause + KleFuEntry.COLUMN_NAME_WEGNAME + " LIKE ?";
    					arrayListWhereOptions.add(thisSucher.getWegname());
    				}
    				
    				if (thisSucher.isSchonGeklettert() && !thisSucher.isNochNichtGeklettert()) {
    					if (!(whereClause.equals(""))) whereClause = whereClause + " AND ";					
    					whereClause = whereClause + 
    							KleFuEntry.COLUMN_NAME_GEKLETTERT + " LIKE ?";
    					arrayListWhereOptions.add("1");
    				} else if (!thisSucher.isSchonGeklettert() && thisSucher.isNochNichtGeklettert()) {
    					whereClause = whereClause + 
    							KleFuEntry.COLUMN_NAME_GEKLETTERT + " LIKE ?"
    							+ " OR " + KleFuEntry.COLUMN_NAME_GEKLETTERT + " IS NULL";
    					arrayListWhereOptions.add("0");
    				}
    				
    				String[] whereOptions2 = new String[arrayListWhereOptions.size()];
    				arrayListWhereOptions.toArray(whereOptions2);
    				
    				if (whereClause == "()") {
    					whereClause = null;
    					whereOptions2 = null;
    				}
    				
    				// Gebiete aus Datenbank lesen
    				c = KleFuEntry.db.query(
    				    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
    				    projection2,           // The columns to return
    				    whereClause,                                // The columns for the WHERE clause
    				    whereOptions2,                            // The values for the WHERE clause
    				    null,                                     // don't group the rows
    				    null,                                     // don't filter by row groups
    				    null                                 // The sort order
    				    );

    				// String aus Cursor lesen

    				arrayListSearchResult = new ArrayList<Weg>(); 
    		        this.listDataHeader = new ArrayList<Gipfel>();
    		        this.listDataChild = new ArrayList<ArrayList<Weg>>();
    		        
    				c.moveToFirst();
    				    				
    				while (c.isAfterLast() == false) {
    					Weg weg = new Weg(
    						c.getInt(0),
    						c.getInt(1),
    						c.getString(2),
    						c.getString(3),
    						new Schwierigkeit(c.getInt(4)),
    						new Schwierigkeit(c.getInt(5)),
    						new Schwierigkeit(c.getInt(6)),
    						c.getString(7),
    						c.getString(8),
    						c.getString(9),
    						c.getString(10),
    						c.getString(11),
    						c.getInt(12),
    						c.getInt(13),
    						c.getInt(14)>0,
    						c.getInt(15)>0
    					);
    					
    					arrayListSearchResult.add(weg);
    					weg = null;
    					
//    					int test = c.getInt(13);
    					
    					c.moveToNext();
    				}
    				
    				datenAufarbeiten();
    				KleFuContract.anzahltreffer = arrayListSearchResult.size();
    				KleFuContract.listDataChild = listDataChild;
    				KleFuContract.listDataHeader = listDataHeader;
    				
    				//Bisherige gleiche Suchanfrage löschen
    				String[] projection3 = { 
    						KleFuEntry.COLUMN_NAME_GEBIET,//0
    						KleFuEntry.COLUMN_NAME_GIPFELNUMMER_VON,//1
					        KleFuEntry.COLUMN_NAME_GIPFELNUMMER_BIS,//2
					        KleFuEntry.COLUMN_NAME_GIPFEL,//3
					        KleFuEntry.COLUMN_NAME_WEGNAME,
					        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_VON,//4
					        KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_BIS,//5
					        KleFuEntry.COLUMN_NAME_GEKLETTERT,//6
					        KleFuEntry.COLUMN_NAME_NOCH_NICHT_GEKLETTERT
    				};
    				
    				whereClause = KleFuEntry.COLUMN_NAME_GEBIET + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_GIPFELNUMMER_VON + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_GIPFELNUMMER_BIS + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_WEGNAME + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_VON + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_BIS + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_GEKLETTERT + " LIKE ? AND " +
							KleFuEntry.COLUMN_NAME_NOCH_NICHT_GEKLETTERT + " LIKE ?";
    				
    				String[] whereArgs = {
    						thisSucher.getGebiet(),
    						thisSucher.getGipfelnummer().toString(),
    						thisSucher.getGipfelnummerBis().toString(),
    						thisSucher.getGipfel(),
    						thisSucher.getWegname(),
    						thisSucher.getSchwierigkeitVonInt().toString(),
    						thisSucher.getSchwierigkeitBisInt().toString(),
    						fromBooleanToString(thisSucher.isSchonGeklettert()),
    						fromBooleanToString(thisSucher.isNochNichtGeklettert())
    				};    				
    				
    				KleFuEntry.db.delete(KleFuEntry.TABLE_NAME_SUCHANFRAGEN, whereClause, whereArgs);

    				
    				// bisher erfolgte Suchanfragen abspeichern
    		 	    ContentValues values = new ContentValues();
    			    values.put(KleFuEntry.COLUMN_NAME_GEBIET, thisSucher.getGebiet());				 				
    			    values.put(KleFuEntry.COLUMN_NAME_GIPFELNUMMER_VON, thisSucher.getGipfelnummer());
    			    values.put(KleFuEntry.COLUMN_NAME_GIPFELNUMMER_BIS, thisSucher.getGipfelnummerBis());
    			    values.put(KleFuEntry.COLUMN_NAME_GIPFEL, thisSucher.getGipfel());
    			    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, thisSucher.getWegname());
    			    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_VON, thisSucher.getSchwierigkeitVonInt());
    			    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_BIS, thisSucher.getSchwierigkeitBisInt());
    			    values.put(KleFuEntry.COLUMN_NAME_GEKLETTERT, fromBooleanToString(thisSucher.isSchonGeklettert()));
    			    values.put(KleFuEntry.COLUMN_NAME_NOCH_NICHT_GEKLETTERT, fromBooleanToString(thisSucher.isNochNichtGeklettert()));
    			    
    				KleFuEntry.db.insert(
    						KleFuEntry.TABLE_NAME_SUCHANFRAGEN,
    						"null",
    						values);    			
    				c.close();
    				
    				c = KleFuEntry.db.query(
        				    KleFuEntry.TABLE_NAME_SUCHANFRAGEN,  // The table to query
        				    projection3,           // The columns to return
        				    null,                                // The columns for the WHERE clause
        				    null,                            // The values for the WHERE clause
        				    null,                                     // don't group the rows
        				    null,                                     // don't filter by row groups
        				    null                                 // The sort order
        				    );
    				
    				//Bei zu vielen verschiedenen Suchanfragen letzte erfolgte Suchanfrage löschen
    				if (c.getCount() >= 201) {    					
    					c.moveToFirst();
    					String[] whereArgs2 = {
    							c.getString(0),
    							c.getString(1),
    							c.getString(2),
    							c.getString(3),
    							c.getString(4),
    							c.getString(5),
    							c.getString(6),
    							c.getString(7),
    							c.getString(8)
    					};  		
    					KleFuEntry.db.delete(KleFuEntry.TABLE_NAME_SUCHANFRAGEN, whereClause, whereArgs2);
    				}
    				
    				LetzteSuchanfragenFragment.notifyDataSetChanged();
    				
                	msg = Message.obtain(sucheHandler,
                			KleFuEntry.MESSAGE_SEARCH_COMPLETED,
                			0, 0, "");
                	sucheHandler.sendMessage(msg);    				
                 } catch(Exception e) {
                	String errMsg = parentActivity.getString(R.string.error_message_general);
                	msg = Message.obtain(sucheHandler,
                			KleFuEntry.MESSAGE_SEARCH_CANCELED,
                			0, 0, errMsg);
                	sucheHandler.sendMessage(msg);
                } 
                c.close();
        }

        private void datenAufarbeiten() {
            
            if (arrayListSearchResult.size() > 0) {
    	        Gipfel gipfel = arrayListSearchResult.get(0).getGipfelObject();

            	ArrayList<Weg> wegelist = new ArrayList<Weg>();
    	        //Wegedaten aufarbeiten zum anschauen
    	        for (int i=0; i<arrayListSearchResult.size(); i++) {
    	        	// Gipfelliste füllen
    	        	if (!gipfel.getGipfel().equals(arrayListSearchResult.get(i).getGipfel())) {
    	        		listDataHeader.add(gipfel);
    	    	        listDataChild.add(wegelist);
    	            	wegelist=null;
    	            	wegelist=new ArrayList<Weg>();
    	        		gipfel = arrayListSearchResult.get(i).getGipfelObject();
    	        	} 
    	        	wegelist.add(arrayListSearchResult.get(i));       		
    	        }
    	        Gipfel lastGipfel = arrayListSearchResult.get(arrayListSearchResult.size()-1).getGipfelObject();
    	        listDataHeader.add(lastGipfel);
            	listDataChild.add(wegelist);	        	        
            }
        }        
        
        private String fromBooleanToString(boolean bool) {
        	if (bool == true) return "1";
        	return "0";
        }
}

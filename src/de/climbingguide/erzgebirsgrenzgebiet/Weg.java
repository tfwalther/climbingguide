package de.climbingguide.erzgebirsgrenzgebiet;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;

public class Weg extends Gipfel {

	private Integer wegeid;
	private String wegname;
	private String beschreibung;  
	protected Schwierigkeit schwierigkeit_af;
	protected Schwierigkeit schwierigkeit_oU;
	protected Schwierigkeit schwierigkeit_RP;
	private String erstbegeher1;
	private String erstbegeher2;
	private String erstbegeher3;
	private String erstbegeherandere;
	private String erstbegehungsdatum;
	private int stern;
	private int ausrufezeichen;
	protected Boolean geklettert;
	protected Boolean vorgestiegen;
	protected Boolean rp;
	private Boolean shownDetailed;
	protected Boolean oU;
	
	public Weg() {
		
	};
	
	public Weg(
			String gipfel,
			String wegname,
			String beschreibung,  
			Schwierigkeit schwierigkeit_af,
			Schwierigkeit schwierigkeit_oU,
			Schwierigkeit schwierigkeit_RP,
			String erstbegeher1,
			String erstbegeher2,
			String erstbegeher3,
			String erstbegeherandere,
			String erstbegehungsdatum,
			int stern,
			int ausrufezeichen
			) {
			super(gipfel);			
			this.wegname = wegname;
			this.beschreibung = beschreibung;
			this.schwierigkeit_af = schwierigkeit_af;
			this.schwierigkeit_oU = schwierigkeit_oU;
			this.schwierigkeit_RP = schwierigkeit_RP;
			this.erstbegeher1 = erstbegeher1;
			this.erstbegeher2 = erstbegeher2;
			this.erstbegeher3 = erstbegeher3;
			this.erstbegeherandere = erstbegeherandere;
			this.stern = stern;
			this.ausrufezeichen = ausrufezeichen;
	}

	public Weg(
			String gipfel,
			String wegname,
			String beschreibung,  
			Schwierigkeit schwierigkeit_af,
			Schwierigkeit schwierigkeit_oU,
			Schwierigkeit schwierigkeit_RP,
			String erstbegeher1,
			String erstbegeher2,
			String erstbegeher3,
			String erstbegeherandere,
			String erstbegehungsdatum,
			int stern,
			int ausrufezeichen,
			boolean geklettert
			) {
			super(gipfel);			
			this.wegname = wegname;
			this.beschreibung = beschreibung;
			this.schwierigkeit_af = schwierigkeit_af;
			this.schwierigkeit_oU = schwierigkeit_oU;
			this.schwierigkeit_RP = schwierigkeit_RP;
			this.erstbegeher1 = erstbegeher1;
			this.erstbegeher2 = erstbegeher2;
			this.erstbegeher3 = erstbegeher3;
			this.erstbegeherandere = erstbegeherandere;
			this.erstbegehungsdatum = erstbegehungsdatum;
			this.stern = stern;
			this.ausrufezeichen = ausrufezeichen;
			this.geklettert = geklettert;
	}	

	public Weg(
			Integer wegeid,
			String gipfel,
			String wegname,
			String beschreibung,  
			Schwierigkeit schwierigkeit_af,
			Schwierigkeit schwierigkeit_oU,
			Schwierigkeit schwierigkeit_RP,
			String erstbegeher1,
			String erstbegeher2,
			String erstbegeher3,
			String erstbegeherandere,
			String erstbegehungsdatum,
			int stern,
			int ausrufezeichen,
			boolean geklettert
			) {
			super(gipfel);
			this.wegeid = wegeid;
			this.wegname = wegname;
			this.beschreibung = beschreibung;
			this.schwierigkeit_af = schwierigkeit_af;
			this.schwierigkeit_oU = schwierigkeit_oU;
			this.schwierigkeit_RP = schwierigkeit_RP;
			this.erstbegeher1 = erstbegeher1;
			this.erstbegeher2 = erstbegeher2;
			this.erstbegeher3 = erstbegeher3;
			this.erstbegeherandere = erstbegeherandere;
			this.erstbegehungsdatum = erstbegehungsdatum;
			this.stern = stern;
			this.ausrufezeichen = ausrufezeichen;
			this.geklettert = geklettert;
	}
	
	public Weg(
			Integer wegeid,
			Integer gipfelId,
			String wegname,
			String beschreibung,  
			Schwierigkeit schwierigkeit_af,
			Schwierigkeit schwierigkeit_oU,
			Schwierigkeit schwierigkeit_RP,
			String erstbegeher1,
			String erstbegeher2,
			String erstbegeher3,
			String erstbegeherandere,
			String erstbegehungsdatum,
			int stern,
			int ausrufezeichen,
			boolean geklettert
			) {
			super(gipfelId);
			this.wegeid = wegeid;
			this.wegname = wegname;
			this.beschreibung = beschreibung;
			this.schwierigkeit_af = schwierigkeit_af;
			this.schwierigkeit_oU = schwierigkeit_oU;
			this.schwierigkeit_RP = schwierigkeit_RP;
			this.erstbegeher1 = erstbegeher1;
			this.erstbegeher2 = erstbegeher2;
			this.erstbegeher3 = erstbegeher3;
			this.erstbegeherandere = erstbegeherandere;
			this.erstbegehungsdatum = erstbegehungsdatum;
			this.stern = stern;
			this.ausrufezeichen = ausrufezeichen;
			this.geklettert = geklettert;
	}	

	public Weg(
			Integer wegeid,
			Integer gipfelId,
			String wegname,
			String beschreibung,  
			Schwierigkeit schwierigkeit_af,
			Schwierigkeit schwierigkeit_oU,
			Schwierigkeit schwierigkeit_RP,
			String erstbegeher1,
			String erstbegeher2,
			String erstbegeher3,
			String erstbegeherandere,
			String erstbegehungsdatum,
			int stern,
			int ausrufezeichen,
			boolean geklettert,
			boolean isExpanded
			) {
			super(gipfelId);
			this.wegeid = wegeid;
			this.wegname = wegname;
			this.beschreibung = beschreibung;
			this.schwierigkeit_af = schwierigkeit_af;
			this.schwierigkeit_oU = schwierigkeit_oU;
			this.schwierigkeit_RP = schwierigkeit_RP;
			this.erstbegeher1 = erstbegeher1;
			this.erstbegeher2 = erstbegeher2;
			this.erstbegeher3 = erstbegeher3;
			this.erstbegeherandere = erstbegeherandere;
			this.erstbegehungsdatum = erstbegehungsdatum;
			this.stern = stern;
			this.ausrufezeichen = ausrufezeichen;
			this.geklettert = geklettert;
			this.shownDetailed=isExpanded;
	}		
	
	public Weg(Integer wegeid) {
		this.wegeid = wegeid;
		// Datenbankabfrage ob bereits vor kurzem derselbe Weg als geklettert eingetragen worden ist 
		String[] projection = {
		    KleFuEntry.COLUMN_NAME_WEGNAME,
		    KleFuEntry.COLUMN_NAME_GEKLETTERT,
		    KleFuEntry.COLUMN_NAME_GIPFELID,
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF,
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU,
		    KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP,
		    KleFuEntry.COLUMN_NAME_RP,
		    KleFuEntry.COLUMN_NAME_OU,
		    KleFuEntry.COLUMN_NAME_VORSTIEG,
		};
		
		String whereClause = KleFuEntry._ID + " LIKE ?";
		
		String[] whereArgs = { wegeid.toString() };
		
		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
		    projection,                               // The columns to return
		    whereClause,                                // The columns for the WHERE clause
		    whereArgs,                            // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );

		c.moveToFirst();
		this.wegname=c.getString(0);
		this.geklettert=c.getInt(1)>0;
		super.setGipfelWithGipfelId(c.getInt(2));
		this.schwierigkeit_af = new Schwierigkeit(c.getInt(3));
		this.schwierigkeit_oU = new Schwierigkeit(c.getInt(4));
		this.schwierigkeit_RP = new Schwierigkeit(c.getInt(5));
		this.rp = c.getInt(6)>0;
		this.oU = c.getInt(7)>0;
		this.vorgestiegen = c.getInt(8)>0;
		c.close();
	}

	public String getWegname() { return wegname; }
	public String getHtmlWegname() { return getHtmlWegname(true); }
	public String getHtmlWegname(boolean underline) {
		String htmlWegname;
		if (underline) {
			if (isGeklettert()) {
				htmlWegname = "<u>"+wegname+"</u>";
			} else htmlWegname = wegname;
		} else htmlWegname = wegname;
		
		for (int i=0;i<getStern();i++) {
			htmlWegname = "*" + htmlWegname;
		}
		for (int i=0;i<getAusrufezeichen();i++) {
			htmlWegname = "!" + htmlWegname;
		}
		return htmlWegname;
	}	
	public String getBeschreibung() { return beschreibung; }  
	public Schwierigkeit getSchwierigkeit_af() { return schwierigkeit_af; }
	public Schwierigkeit getSchwierigkeit_oU() { return schwierigkeit_oU; }
	public boolean hasoU() { return schwierigkeit_oU.getSchwierigkeitInt()!=0; }
	public Schwierigkeit getSchwierigkeit_RP() { return schwierigkeit_RP; }
	public String getErstbegeher1 () { return erstbegeher1; }
	public String getErstbegeher2 () { return erstbegeher2; }
	public String getErstbegeher3 () { return erstbegeher3; }
	public String getErstbegeherandere () { return erstbegeherandere; }
	public String getErstbegehungsdatum () { return erstbegehungsdatum; }
	public Gipfel getGipfelObject() { return new Gipfel(getGipfelId()); }
	
	public String getSchwierigkeit() {
		String s = schwierigkeit_af.getSchwierigkeitString();
		if (schwierigkeit_oU.isEnabled()) {
			s = s +	"(" + schwierigkeit_oU.getSchwierigkeitString() + ")";
		}
		if (schwierigkeit_RP.isEnabled()) {
			s = s + " RP " + schwierigkeit_RP.getSchwierigkeitString();
		}
		return s;
	}
	
	public String getHtmlSchwierigkeit() {
		String s = schwierigkeit_af.getSchwierigkeitString();
		if (isGeklettert()) s = "<u>"+schwierigkeit_af.getSchwierigkeitString()+"</u>";
		if (hasoU()) {
			if (isGeklettert()) {
				if (isoU()) {
					s = schwierigkeit_af.getSchwierigkeitString() + " (<u>" + schwierigkeit_oU.getSchwierigkeitString() + "</u>)"; 
				} else {
					s =  s +"(" + schwierigkeit_oU.getSchwierigkeitString()+ ")"; 					
				}
			} else {
				s = s + "(" + schwierigkeit_oU.getSchwierigkeitString()+ ")"; 									
			}
		}
		if (schwierigkeit_RP.isEnabled()) {
			if (isRP()) {
				if (hasoU()) {
					s = schwierigkeit_af.getSchwierigkeitString() + "(" + schwierigkeit_oU.getSchwierigkeitString()+ ")"; 					
				} else {
					s = schwierigkeit_af.getSchwierigkeitString();
				}
				s = s + " RP <u>" + schwierigkeit_RP.getSchwierigkeitString() + "</u>";
			} else {
				s = s + " RP " + schwierigkeit_RP.getSchwierigkeitString();
			}
		}
		return s;
	}	
	
	public String getErstbegeher() {
		String erstbegeher="";
		if (!(erstbegeher1.equals("0")))	erstbegeher = erstbegeher1;
		if (!(erstbegeher2.equals("0")))	erstbegeher = erstbegeher + ", " + erstbegeher2;
		if (!(erstbegeher3.equals("0")))	erstbegeher = erstbegeher + ", " + erstbegeher3;
		if (!(erstbegeherandere.equals("0")))	erstbegeher = erstbegeher + ", " + erstbegeherandere;
		
		return erstbegeher;
	}
	
	public String getPostBeschreibung() {
		String messageString="<i>";
		if (!(getErstbegeher().equals(""))) messageString = messageString + getErstbegeher() + ", ";
		if (!(getErstbegehungsdatum().equals("0"))) messageString = messageString + getErstbegehungsdatum() + "</i> - ";
		else messageString = messageString + "</i>";
		messageString = messageString + getBeschreibung();
		return messageString;
	}

	public boolean isGeklettert() {
		return geklettert;
	}

	public void setGeklettert(boolean geklettert) {
		setGeklettert(geklettert, true);
	}

	public void setGeklettert(Boolean geklettert, boolean neuerWegEintragen) {
		if (this.geklettert != geklettert) {
			this.geklettert = geklettert;
			//Weg geklettert in die Datenbank eintragen
	 	    ContentValues values = new ContentValues(); 	    
		    String selection = KleFuEntry._ID + " LIKE ?";
		    String[] selelectionArgs = { getWegeid().toString() };
		    values.put(KleFuEntry.COLUMN_NAME_GEKLETTERT, geklettert);				 				
		    KleFuEntry.db.update(
			        KleFuEntry.TABLE_NAME_WEGE,
			        values,
			        selection,
			        selelectionArgs);
		}
		    
	    // Wurde der Gipfel bestiegen? - in eigene Liste eintragen
		if (geklettert == true) { 
			setBestiegen(true);			
	 	    
			if (neuerWegEintragen) {
				// Datenbankabfrage ob bereits vor kurzem derselbe Weg als geklettert eingetragen worden ist 
				String[] projection = {
				    KleFuEntry.COLUMN_NAME_DATUM
				};
				
				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND "
						+ KleFuEntry.COLUMN_NAME_DATUM  + " BETWEEN ? AND ?";
				
				Long currentTime = java.lang.System.currentTimeMillis();
				String[] whereArgs2 = {
						getWegeid().toString(),
						((Long)(currentTime-60000)).toString(),
						currentTime.toString() };					
				
				// Gebiete aus Datenbank lesen
				Cursor c = KleFuEntry.db.query(
				    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
				    projection,                               // The columns to return
				    whereClause,                                // The columns for the WHERE clause
				    whereArgs2,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    null                                 // The sort order
				    );
	
				if (c.getCount()<=0) {							
			    	ContentValues cv = new ContentValues();
			    	cv.put(KleFuEntry.COLUMN_NAME_WEGEID, getWegeid().toString());
			    	cv.put(KleFuEntry.COLUMN_NAME_DATUM, java.lang.System.currentTimeMillis());
			    	KleFuContract.KleFuEntry.db.insert(
				 	        KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				 	        "null",
				 	        cv);
				}
				c.close();
			}
		} else {
			setVorgestiegen(false, false);
			if (hasoU()) setoU(false, false);
			
			// Datenbankabfrage ob Weg geklettert worden ist, wenn ja aus eigener Datenbank löschen
			
			String whereClause = KleFuEntry.COLUMN_NAME_WEGEID  + " LIKE ?";
		
			String[] whereArgs = { getWegeid().toString() };					
			
			// Gebiete aus Datenbank lesen
			Integer cursorLaenge = KleFuEntry.db.delete(
			    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs                           // The values for the WHERE clause
			    );
			
		    if (cursorLaenge>0) {
		    	String toastText=cursorLaenge.toString() + " ";
		    	if (cursorLaenge==1) {
		    		toastText=toastText + " " + ClimbingGuideApplication.getInstance().getString(R.string.eintrag);
		    	} else {
		    		toastText=toastText + " " + ClimbingGuideApplication.getInstance().getString(R.string.eintraege);		    		
		    	}
	    		toastText=toastText + " " + ClimbingGuideApplication.getInstance().getString(R.string.eintraege_geloescht);

		    	Toast.makeText(ClimbingGuideApplication.getInstance().getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
		    }
		    
			// Datenbankabfrage ob ein anderer Weg an diesem Gipfel geklettert worden
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry._ID
			};
			
			whereClause = KleFuEntry.COLUMN_NAME_GIPFELID  + " LIKE ? AND " +
					KleFuEntry.COLUMN_NAME_GEKLETTERT + " LIKE ?";
		
			String[] whereArgs2 = { getGipfelId().toString(), "1" };					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs2,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			setBestiegen(c.getCount()>0);
			c.close();
		}
		// Gipfel nicht bestiegen bzw. bestiegen in SQLite DB eintragen
 	   	ContentValues values = new ContentValues();
	    String selection = KleFuEntry._ID + " LIKE ?";
	    String[] selelectionArgs2 = { getGipfelId().toString() };
	    values.put(KleFuEntry.COLUMN_NAME_BESTIEGEN, isBestiegen());				 				
	    KleFuContract.KleFuEntry.db.update(
		        KleFuEntry.TABLE_NAME_GIPFEL,
		        values,
		        selection,
		        selelectionArgs2);
	}
	
	public int getStern() {
		return stern;
	}

	public void setStern(int stern) {
		this.stern = stern;
	}

	public int getAusrufezeichen() {
		return ausrufezeichen;
	}

	public void setAusrufezeichen(int ausrufezeichen) {
		this.ausrufezeichen = ausrufezeichen;
	}

	public Integer getWegeid() {
		if (wegeid == null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry._ID
			};
			
			String whereClause = KleFuEntry.COLUMN_NAME_GIPFELID  + " LIKE ?" + " AND " +
					KleFuEntry.COLUMN_NAME_WEGNAME + " LIKE ?";
		
			String[] whereArgs = { getGipfelId().toString(), getWegname() };					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			wegeid = c.getInt(0);
			c.close();
		}
		return wegeid;			
	}

	public void setWegeid(Integer wegeid) {
		this.wegeid = wegeid;
	}
	
	public boolean isVorgestiegen() {
		
		if (vorgestiegen == null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry.COLUMN_NAME_VORSTIEG
			};
			
			String whereClause = KleFuEntry._ID  + " LIKE ? AND " + 
				    KleFuEntry.COLUMN_NAME_VORSTIEG + " LIKE ?";
		
			String[] whereArgs = { getWegeid().toString(),  "1"};					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			int cursorLaenge=c.getCount();
			vorgestiegen = (cursorLaenge>0);
		    c.close();
		}
		return vorgestiegen;
	}
		
	public void setVorgestiegen(boolean vorgestiegen) {
		setVorgestiegen(vorgestiegen, true);
	}
	
	public void setVorgestiegen(Boolean vorgestiegen, boolean neuerWegEintragen) {
		if (this.vorgestiegen != vorgestiegen) {
			if (vorgestiegen) { setGeklettert(true); }
			else { 
				setRP(false);
			}
			this.vorgestiegen = vorgestiegen;
			
			//Weg vorgestiegen in die Datenbank eintragen
	 	    ContentValues values = new ContentValues();
		    String selection = KleFuEntry._ID + " LIKE ?";
		    String[] selelectionArgs = { getWegeid().toString() };
		    values.put(KleFuEntry.COLUMN_NAME_VORSTIEG, vorgestiegen);				 				
		    KleFuContract.KleFuEntry.db.update(
			        KleFuEntry.TABLE_NAME_WEGE,
			        values,
			        selection,
			        selelectionArgs);
		    
		    if (vorgestiegen) {
		    	setImVorstieg(vorgestiegen);
		    	
		    	if(neuerWegEintragen) {
					//Datenbankabfrage nach dem Größten Datum dieses Weges in der eigenen Wegeliste
					String[] projection = {
					    KleFuEntry.COLUMN_NAME_DATUM
					};
					
					String whereClause = KleFuEntry.COLUMN_NAME_WEGEID  + " LIKE ?";
				
					String[] whereArgs = { getWegeid().toString() };					
					
					// Gebiete aus Datenbank lesen
					Cursor c = KleFuEntry.db.query(
					    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
					    projection,                               // The columns to return
					    whereClause,                                // The columns for the WHERE clause
					    whereArgs,                            // The values for the WHERE clause
					    null,                                     // don't group the rows
					    null,                                     // don't filter by row groups
					    null                                 // The sort order
					    );
					
					c.moveToLast();
					Long lastTimeGeklettert = c.getLong(0);
				    c.close();
			    	
					// Datenbankupdate der eigenen Wegelise => Vorstieg beim letzten Weg eintragen
				    values.clear();
				    values.put(KleFuEntry.COLUMN_NAME_VORSTIEG, vorgestiegen);
		
					whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND "
							+ KleFuEntry.COLUMN_NAME_DATUM  + " LIKE ?";
			    	
					String[] whereArgs2 = { getWegeid().toString(),  lastTimeGeklettert.toString()};
					
			    	KleFuEntry.db.update(
							KleFuEntry.TABLE_NAME_EIGENE_WEGE,
							values,
							whereClause,
							whereArgs2);
		    	}
		    } else {				
	    	//Datenbankabfrage ob kein anderer Weg am Gipfel vorgestiegen worden ist
			String[] projection = {
				    KleFuEntry._ID
				};
				
				String whereClause = KleFuEntry.COLUMN_NAME_GIPFELID  + " LIKE ? AND " +
						KleFuEntry.COLUMN_NAME_VORSTIEG + " LIKE ?";		
				String[] whereArgs3 = { getGipfelId().toString(), "1" };					
				
				// Wege des Gipfels aus Datenbank lesen
				Cursor c = KleFuEntry.db.query(
				    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
				    projection,                               // The columns to return
				    whereClause,                                // The columns for the WHERE clause
				    whereArgs3,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    null                                 // The sort order
				    );
				
				boolean imVorstieg=c.getCount()>0;
				if (!imVorstieg) setImVorstieg(false);
				c.close();
				
				//Eigene Wegeliste aktualisieren und Einträge auf nicht vorgestiegen ändern
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(KleFuEntry.COLUMN_NAME_VORSTIEG, false);
				whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ?";
				String[] whereArgs = { getWegeid().toString() };
				KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE, contentValues, whereClause, whereArgs);
			}
		}		   
    }		

	public boolean isRP() {
		
		if (rp == null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry.COLUMN_NAME_RP
			};
			
			String whereClause = KleFuEntry._ID  + " LIKE ? AND " + 
				    KleFuEntry.COLUMN_NAME_RP + " LIKE ?";
		
			String[] whereArgs = { getWegeid().toString(),  "1"};					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			int cursorLaenge=c.getCount();
			rp=cursorLaenge>0;
		    c.close();
		}
		return rp;
	}
		
	public void setRP(boolean rp) {
		setRP(rp, true);
	}

	public void setRP(Boolean rp, boolean neuerWegEintragen) {
	    if (this.rp != rp) {//wenn keine Änderung nichts machen
			ContentValues values = new ContentValues();
			if (rp) {
				setGeklettert(true);
				setVorgestiegen(true);
				if (hasoU()) setoU(true);
			}
			this.rp = rp;
			//Weg vorgestiegen in die Datenbank eintragen
	 
		    String selection = KleFuEntry._ID + " LIKE ?";
		    String[] selelectionArgs = { getWegeid().toString() };
		    values.put(KleFuEntry.COLUMN_NAME_RP, rp);				 				
		    KleFuContract.KleFuEntry.db.update(
			        KleFuEntry.TABLE_NAME_WEGE,
			        values,
			        selection,
			        selelectionArgs);
		    if (rp && neuerWegEintragen) {
				//Datenbankabfrage nach dem Größten Datum dieses Weges in der eigenen Wegeliste
				String[] projection = {
				    KleFuEntry.COLUMN_NAME_DATUM
				};
				
				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID  + " LIKE ?";
			
				String[] whereArgs = { getWegeid().toString() };					
				
				// Gebiete aus Datenbank lesen
				Cursor c = KleFuEntry.db.query(
				    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
				    projection,                               // The columns to return
				    whereClause,                                // The columns for the WHERE clause
				    whereArgs,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    null                                 // The sort order
				    );
				
				c.moveToLast();
				Long lastTimeGeklettert = c.getLong(0);
			    c.close();
		    	
				// Datenbankupdate der eigenen Wegeliste => RP beim letzten Weg eintragen
		    	values.put(KleFuEntry.COLUMN_NAME_RP, rp);
	
				whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND "
						+ KleFuEntry.COLUMN_NAME_DATUM  + " LIKE ?";
		    	
				String[] whereArgs2 = { getWegeid().toString(),  lastTimeGeklettert.toString()};
				
		    	KleFuEntry.db.update(
						KleFuEntry.TABLE_NAME_EIGENE_WEGE,
						values,
						whereClause,
						whereArgs2);
			} else if (!rp) {
				//Überprüfen, ob in eigener Wegeliste Weg RP ist,
				//wenn ja RP entfernen
				values.clear();
				values.put(KleFuEntry.COLUMN_NAME_RP, false);
				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ?";
				String[] whereArgs = { getWegeid().toString() };
				KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
						values,
						whereClause,
						whereArgs);
			}
	    }
	}
	
	public boolean isoU() {
		if (oU == null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry.COLUMN_NAME_OU
			};
			
			String whereClause = KleFuEntry._ID  + " LIKE ? AND " + 
				    KleFuEntry.COLUMN_NAME_OU + " LIKE ?";
		
			String[] whereArgs = { getWegeid().toString(),  "1"};					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			int cursorLaenge=c.getCount();
			oU=cursorLaenge>0;
		    c.close();
		}
		return oU;
	}

	public void setoU(boolean oU) {
		setoU(oU, true);
	}
	
	public void setoU(Boolean oU, boolean eigeneWegelisteAktualisieren) {
		if (this.oU != oU) {
			if (oU) { setGeklettert(true); }
			else { setRP(false); }
			this.oU = oU;
			//Weg oU in die Datenbank eintragen
	 	    ContentValues values = new ContentValues();
		    String selection = KleFuEntry._ID + " LIKE ?";
		    String[] selelectionArgs = { getWegeid().toString() };
		    values.put(KleFuEntry.COLUMN_NAME_OU, oU);				 				
		    KleFuContract.KleFuEntry.db.update(
			        KleFuEntry.TABLE_NAME_WEGE,
			        values,
			        selection,
			        selelectionArgs);
		    if (oU && hasoU() && eigeneWegelisteAktualisieren ) {
				//Datenbankabfrage nach dem Größten Datum dieses Weges in der eigenen Wegeliste
				String[] projection = {
				    KleFuEntry.COLUMN_NAME_DATUM
				};
				
				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID  + " LIKE ?";
			
				String[] whereArgs = { getWegeid().toString() };					
				
				// Gebiete aus Datenbank lesen
				Cursor c = KleFuEntry.db.query(
				    KleFuEntry.TABLE_NAME_EIGENE_WEGE,  // The table to query
				    projection,                               // The columns to return
				    whereClause,                                // The columns for the WHERE clause
				    whereArgs,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    null                                 // The sort order
				    );
				
				c.moveToLast();
				Long lastTimeGeklettert = c.getLong(0);
			    c.close();
		    	
				// Datenbankupdate der eigenen Wegeliste => oU beim letzten Weg eintragen
		    	values.put(KleFuEntry.COLUMN_NAME_OU, oU);
	
				whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ? AND "
						+ KleFuEntry.COLUMN_NAME_DATUM  + " LIKE ?";
		    	
				String[] whereArgs2 = { getWegeid().toString(),  lastTimeGeklettert.toString()};
				
		    	KleFuEntry.db.update(
						KleFuEntry.TABLE_NAME_EIGENE_WEGE,
						values,
						whereClause,
						whereArgs2);
			} else if (!oU) {
				//Überprüfen, ob in eigener Wegeliste Weg vorgestiegen worden ist,
				//wenn ja vorstiegsargument entfernen
				values.clear();
				values.put(KleFuEntry.COLUMN_NAME_OU, false);
				String whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ?";
				String[] whereArgs = { getWegeid().toString() };
				KleFuEntry.db.update(KleFuEntry.TABLE_NAME_EIGENE_WEGE,
						values,
						whereClause,
						whereArgs);
			}
		}
	}

	public boolean isShownDetailed() {
		if (shownDetailed==null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry.COLUMN_NAME_IS_EXPANDED
			};
			
			String whereClause = KleFuEntry._ID  + " LIKE ? AND " + 
				    KleFuEntry.COLUMN_NAME_IS_EXPANDED + " LIKE ?";
		
			String[] whereArgs = { getWegeid().toString(),  "1"};					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_WEGE,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			
			int cursorLaenge=c.getCount();
			shownDetailed=cursorLaenge>0;
		    c.close();
		}
		return shownDetailed;
	}

	public void setShownDetailed(Boolean shownDetailed) {
		this.shownDetailed = shownDetailed;
		//WegIsShown vorgestiegen in die Datenbank eintragen
 	    ContentValues values = new ContentValues();
	    String selection = KleFuEntry._ID + " LIKE ?";
	    String[] selelectionArgs = { getWegeid().toString() };
	    values.put(KleFuEntry.COLUMN_NAME_IS_EXPANDED, shownDetailed);				 				
	    KleFuContract.KleFuEntry.db.update(
		        KleFuEntry.TABLE_NAME_WEGE,
		        values,
		        selection,
		        selelectionArgs);
	}
	
	public void setWegname(String wegname) {
		this.wegname=wegname;
	}
	
	/*
	 * Weg auf den Stand der SQLite Datenbank aktualisieren
	 */
	public void sync() {
		String whereClause;
		//Wenn Weg einziger gekletterter war, Weg geklettert entfernen
		String[] projection = { 
				KleFuEntry.COLUMN_NAME_VORSTIEG,//0
				KleFuEntry.COLUMN_NAME_RP,//1
				KleFuEntry.COLUMN_NAME_OU//2						
				};
		whereClause = KleFuEntry.COLUMN_NAME_WEGEID + " LIKE ?";
		String[] whereArgs2 = { this.getWegeid().toString() };
		Cursor c = KleFuEntry.db.query(
				KleFuEntry.TABLE_NAME_EIGENE_WEGE,
				projection,
				whereClause,
				whereArgs2, 
				null,
				null,
				null);

		boolean isGeklettert=false;
		boolean isVorstieg=false;
		boolean isRP = false;
		boolean isoU = false;
		c.moveToFirst();
		while(!c.isAfterLast()) {
			isGeklettert=true;
			if (c.getInt(0)>0) isVorstieg=true;
			if (c.getInt(1)>0) isRP=true;
			if (c.getInt(2)>0) isoU=true;
			c.moveToNext();
		}
		c.close();
		
		this.setoU(isoU, false);
		this.setRP(isRP, false);
		this.setVorgestiegen(isVorstieg, false);
		this.setGeklettert(isGeklettert, false);
	}
	
	@Override
	public String toString() {
		return getWegname();
	}
	
	public static String cursorToString(Cursor c, boolean underline){
		Weg weg = new Weg();
		weg.wegname=c.getString(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_WEGNAME));
		weg.stern=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_STERN));
		weg.ausrufezeichen=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN));
		if (c.getInt(c.getColumnIndexOrThrow(KleFuEntry.NUMBERS))>1) {
			return weg.getHtmlWegname(false);
		} else {
			weg.geklettert=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GEKLETTERT))>0;
			return weg.getHtmlWegname(underline);			
		}
	}

	public static String cursorToSchwierigkeit(Cursor c, boolean underline) {
		Weg weg = new Weg();
		weg.geklettert=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_GEKLETTERT))>0;
		weg.schwierigkeit_af=new Schwierigkeit(c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF)));
		weg.schwierigkeit_oU=new Schwierigkeit(c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU)));
		weg.schwierigkeit_RP=new Schwierigkeit(c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP)));
		weg.oU=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_OU))>0;
		weg.rp=c.getInt(c.getColumnIndexOrThrow(KleFuEntry.COLUMN_NAME_RP))>0;
		return weg.getHtmlSchwierigkeit();
	}
}

package de.climbingguide.erzgebirsgrenzgebiet;

import net.sqlcipher.Cursor;

import org.mapsforge.core.GeoPoint;

import android.content.ContentValues;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;

public class Gipfel {
	private String gebiet;
	private String untergebiet;
	private String gipfel;
	private Integer gipfelnummer;
	private GeoPoint geopoint;
	private boolean bestiegen;
	private boolean imVorstieg;
	private Integer gipfelId=null;
	
	public Gipfel() {};
	
	public Gipfel(String gipfel) {
		this.gipfel = gipfel;
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    KleFuEntry._ID,//
		    KleFuEntry.COLUMN_NAME_GEBIET,//1
		    KleFuEntry.COLUMN_NAME_UNTERGEBIET,//2
		    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,//3
		    KleFuEntry.COLUMN_NAME_NORTH_COORDINATE,//4
		    KleFuEntry.COLUMN_NAME_EAST_COORDINATE,//5
		    KleFuEntry.COLUMN_NAME_BESTIEGEN//6
		};


		// String für die Where Clause
		String whereClause =
			    KleFuEntry.COLUMN_NAME_GIPFEL + " LIKE ?";
		// String für die Where Options
		String[] whereOptions = {
			    gipfel
		};	
		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection,                               // The columns to return
		    whereClause,                              // The columns for the WHERE clause
		    whereOptions,                             // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
		c.moveToFirst();
		this.gebiet = c.getString(1);
		this.gipfelnummer = c.getInt(3);
		this.untergebiet = c.getString(2);
		this.geopoint = new GeoPoint(c.getDouble(4), c.getDouble(5));
		this.bestiegen = c.getInt(6)>0;
		this.gipfelId = c.getInt(0);
	    c.close();		
	}
	
	public Gipfel(Integer gipfelId) {
		setGipfelWithGipfelId(gipfelId);			
	}

	protected void setGipfelWithGipfelId(Integer gipfelId) {
		this.gipfelId = gipfelId;

		// you will actually use after this query.
		String[] projection = {				
		    KleFuEntry.COLUMN_NAME_GEBIET,//0
		    KleFuEntry.COLUMN_NAME_UNTERGEBIET,//1
		    KleFuEntry.COLUMN_NAME_GIPFELNUMMER,//2
		    KleFuEntry.COLUMN_NAME_NORTH_COORDINATE,//3
		    KleFuEntry.COLUMN_NAME_EAST_COORDINATE,//4
		    KleFuEntry.COLUMN_NAME_BESTIEGEN,//5
		    KleFuEntry.COLUMN_NAME_GIPFEL,//6
		    KleFuEntry.COLUMN_NAME_VORSTIEG
		};


		// String für die Where Clause
		String whereClause =
			    KleFuEntry._ID + " LIKE ?";
		// String für die Where Options
		String[] whereOptions = {
			    this.gipfelId.toString()
		};	
		// Gebiete aus Datenbank lesen
		Cursor c = KleFuEntry.db.query(
		    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
		    projection,                               // The columns to return
		    whereClause,                              // The columns for the WHERE clause
		    whereOptions,                             // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    null                                 // The sort order
		    );
		
		c.moveToFirst();
		this.gebiet = c.getString(0);
		this.gipfelnummer = c.getInt(2);
		this.untergebiet = c.getString(1);
		this.geopoint = new GeoPoint(c.getDouble(3), c.getDouble(4));
		this.bestiegen = c.getInt(5)>0;
		this.gipfel = c.getString(6);
		this.imVorstieg = c.getInt(7)>0;
	    c.close();
	}
	
	public Gipfel(String gipfel, int gipfelnummer, String gebiet, boolean bestiegen) {
		this.gipfel = gipfel;
		this.gipfelnummer = gipfelnummer;
		this.gebiet = gebiet;
		this.bestiegen = bestiegen;
	}
	
	public String getGebiet() {
		return gebiet;
	}
	public void setGebiet(String gebiet) {
		this.gebiet = gebiet;
	}
	public String getGipfel() {
		return gipfel;
	}
	public String getGipfelHtml() {
		if (isBestiegen()) return "<u>" + gipfel + "</u>";
		return gipfel;
	}
	public void setGipfel(String gipfel) {
		this.gipfel = gipfel;
	}
	public Integer getGipfelnummer() {
		return gipfelnummer;
	}
	public void setGipfelnummer(Integer gipfelnummer) {
		this.gipfelnummer = gipfelnummer;
	}
	public String getUntergebiet() {
		return untergebiet;
	}
	public void setUntergebiet(String untergebiet) {
		this.untergebiet = untergebiet;
	}

	public GeoPoint getGeopoint() {
		return geopoint;
	}

	public void setGeopoint(GeoPoint geopoint) {
		this.geopoint = geopoint;
	}
	
	public boolean isBestiegen() {
		return bestiegen;
	}

	public void setBestiegen(boolean bestiegen) {
		this.bestiegen = bestiegen;
	}


	@Override
	public String toString() {
		return gipfel;
	}

	public boolean isImVorstieg() {
		return imVorstieg;
	}

	public void setImVorstieg(boolean imVorstieg) {
		this.imVorstieg = imVorstieg;
		
		// In Datenbank eintragen
 	    ContentValues values = new ContentValues();
	    String selection = KleFuEntry._ID + " LIKE ?";
	    String[] selelectionArgs = { getGipfelId().toString() };
	    values.put(KleFuEntry.COLUMN_NAME_VORSTIEG, imVorstieg);				 				
	    KleFuContract.KleFuEntry.db.update(
		        KleFuEntry.TABLE_NAME_GIPFEL,
		        values,
		        selection,
		        selelectionArgs);
	}

	public Integer getGipfelId() {		
		if (gipfelId==null) {
			//Datenbankabfrage nach der Wegeid
			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = {
			    KleFuEntry._ID
			};
			
			String whereClause = KleFuEntry.COLUMN_NAME_GIPFEL  + " LIKE ?";		
			String[] whereArgs = { getGipfel() };					
			
			// Gebiete aus Datenbank lesen
			Cursor c = KleFuEntry.db.query(
			    KleFuEntry.TABLE_NAME_GIPFEL,  // The table to query
			    projection,                               // The columns to return
			    whereClause,                                // The columns for the WHERE clause
			    whereArgs,                            // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    null                                 // The sort order
			    );
			gipfelId = c.getInt(0);
			c.close();
		}
		return gipfelId;
	}

	public void setGipfelId(Integer gipfelId) {
		this.gipfelId = gipfelId;		
	}
	
	public String getHtmlGipfelnummerPlusGipfel() {
		return getGipfelnummer().toString() + " - " + 
		"<b>"+getGipfelHtml()+"</b>";
	}
}

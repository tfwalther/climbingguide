package de.climbingguide.erzgebirsgrenzgebiet.db;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.app.Activity;
import android.content.ContentValues;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class DBZugriff extends SQLiteOpenHelper {

	   public static final int DATABASE_VERSION = 5;
	   public static final String DATABASE_NAME = "ClimbingGuide.db";

	   //SQLite Datentypen
	   private static final String TEXT_TYPE = " TEXT";
//	   private static final String INTEGER = " INTEGER";
	   private static final String SMALLINT = " SMALLINT";
	   private static final String BOOLEAN = " BOOLEAN";
	   
	   private static final String COMMA_SEP = ",";

	   private static final String SQL_CREATE_ENTRIES_GEBIETE =
		       "CREATE TABLE " + KleFuEntry.TABLE_NAME_GEBIETE + " (" +
		       KleFuEntry._ID + " INTEGER PRIMARY KEY," +
		       KleFuEntry.COLUMN_NAME_GEBIET + TEXT_TYPE +
		       ")";	   

	   private static final String SQL_CREATE_ENTRIES_GIPFEL =
		       "CREATE TABLE " + KleFuEntry.TABLE_NAME_GIPFEL + " (" +
		       KleFuEntry._ID + " INTEGER PRIMARY KEY," +
		       KleFuEntry.COLUMN_NAME_GEBIET + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_UNTERGEBIET + TEXT_TYPE + COMMA_SEP + 
		       KleFuEntry.COLUMN_NAME_GIPFEL + TEXT_TYPE + COMMA_SEP +	   
		       KleFuEntry.COLUMN_NAME_GIPFELNUMMER + SMALLINT +	COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_NORTH_COORDINATE + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_EAST_COORDINATE + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_BESTIEGEN + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_VORSTIEG + BOOLEAN +
		       ")";	   

	   private static final String SQL_CREATE_ENTRIES_WEGE =
		       "CREATE TABLE " + KleFuEntry.TABLE_NAME_WEGE + " (" +
		       KleFuEntry._ID + " INTEGER PRIMARY KEY," +
		       KleFuEntry.COLUMN_NAME_GIPFELID + SMALLINT + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_WEGNAME + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_BESCHREIBUNG + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF + SMALLINT + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU + SMALLINT + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP + SMALLINT + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_ERSTBEGEHER1 + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_ERSTBEGEHER2 + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_ERSTBEGEHER3 + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM + TEXT_TYPE + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_STERN + SMALLINT + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_GEKLETTERT + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_VORSTIEG + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_RP + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_OU + BOOLEAN + COMMA_SEP +
		       KleFuEntry.COLUMN_NAME_IS_EXPANDED + BOOLEAN +
		       ")";
	   
	   private static final String SQL_CREATE_ENTRIES_SUCHANFRAGEN =
			   "CREATE TABLE " + KleFuEntry.TABLE_NAME_SUCHANFRAGEN + " (" +
				       KleFuEntry._ID + " INTEGER PRIMARY KEY," +
				       KleFuEntry.COLUMN_NAME_GEBIET + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_GIPFEL + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_GIPFELNUMMER_VON + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_GIPFELNUMMER_BIS + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_WEGNAME + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_VON + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_BIS + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_GEKLETTERT + BOOLEAN + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_NOCH_NICHT_GEKLETTERT + BOOLEAN +
				       ")";
	   
	   private static final String SQL_CREATE_ENTRIES_EIGENE_WEGE =
			   "CREATE TABLE " + KleFuEntry.TABLE_NAME_EIGENE_WEGE + " (" +
				       KleFuEntry._ID + " INTEGER PRIMARY KEY," +
				       KleFuEntry.COLUMN_NAME_WEGEID + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_VORSTIEG + BOOLEAN + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_RP + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_OU + SMALLINT + COMMA_SEP + 
				       KleFuEntry.COLUMN_NAME_DATUM + SMALLINT + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_PERSONEN + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_BEMERKUNGEN + TEXT_TYPE + COMMA_SEP +
				       KleFuEntry.COLUMN_NAME_IS_EXPANDED + BOOLEAN + 
				       ")";	   
	       
	   // Konstruktor
	   public DBZugriff(Activity activity) {
	      super(activity.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
//	      db = getWritableDatabase();		      
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	        // Tabellen anlegen		   
	        db.execSQL(SQL_CREATE_ENTRIES_GEBIETE);        
	        db.execSQL(SQL_CREATE_ENTRIES_GIPFEL);        
	        db.execSQL(SQL_CREATE_ENTRIES_WEGE);
	        db.execSQL(SQL_CREATE_ENTRIES_SUCHANFRAGEN);
	        db.execSQL(SQL_CREATE_ENTRIES_EIGENE_WEGE);
	        putValues(db);
	   }
	
	@Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion,
	                        int newVersion) {
			String[] whereArgs = null;
			db.delete(KleFuEntry.TABLE_NAME_GEBIETE, null, whereArgs);
			db.delete(KleFuEntry.TABLE_NAME_GIPFEL, null, whereArgs);
			db.delete(KleFuEntry.TABLE_NAME_WEGE, null, whereArgs);
			putValues(db);
	}
	
	private void putValues(SQLiteDatabase db) {
        //Daten in die Datenbank schreiben
	 	    // Create a new map of values, where column names are the keys
	 	    ContentValues values = new ContentValues();

	 	    
	 	// Erzgebirgsgrenzgebiet
	 	    values.put(KleFuEntry.COLUMN_NAME_GEBIET, "Erzgebirgsgrenzgebiet");
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_GEBIETE,
	 	        "null",
	 	        values);
	 	    
	 	//1 - Dieb
	 	    values.put(KleFuEntry.COLUMN_NAME_GEBIET, "Erzgebirgsgrenzgebiet");
	 	    values.put(KleFuEntry.COLUMN_NAME_UNTERGEBIET, "Diebsgrund"); 
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFEL, "Dieb");
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELNUMMER, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_NORTH_COORDINATE, "50.830740");
	 	    values.put(KleFuEntry.COLUMN_NAME_EAST_COORDINATE, "14.023252");
	 	    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_GIPFEL,
	 	        "null",
	 	        values);
	 	    values.clear();
	 	    
	 	    
	 	    //Wege
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Alter Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Von Block zwischen Turm und Massiv rechts Wand zu Absatz und z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, "12");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "13");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "14");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Heinz Tittel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Marx");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "20.10.1962");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    values.put(KleFuEntry.COLUMN_NAME_GEKLETTERT, "0");		    

	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Leichter Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Vom Block des AW links queren und Kamin in der Talseite z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, "11");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "15");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Heinz Tittel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "20.10.1962");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);	   
	 	    
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Morgenriß");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Ganz rechts in der SW-Wand, ca. 1,5 m rechts von Höhlung, Wand linkshaltend hoch und Riss zur Scharte. Links vom AW über Überhang z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, "15");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Hans Schmuck");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "W. Scharfe");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "19.07.1969");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Rächer der Entnervten");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "2 m rechts der Hehlerwqand Wand an 2 R vorbei zu Abs. (Käsebiervariante kreuzend). Links Wand an 3. R vorbei z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIIb"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Ulrich Schmidt");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "J. Friedrich");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "02.11.1989");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	    

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Hehlerwand");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "In der SW-Seite, 4 m links von Höhlung, Wand zu R. Rissspur erst gerade, dann rechtshaltend z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIc"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Uwe Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "M. Schindler");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "C. Brendel, J. Schreier");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "19.09.1982");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Fette Beute");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "3 m rechts vom Einstieg der Abendkante Wand gerade an 2 R vorbei zu Band. Dieses und Riss zum Kamin des Leichten Weges. Wie dieser z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIIa"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Uwe Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "31.08.1981");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Käsebiervariante");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Vom 1. R der fetten Beute 1 m absteigen, 3 m rechts queren und Rissspur zu rechtsansteigendem Band. Dieses und Riss zum Kamin des Leichten Weges. Wie dieser z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIc"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Helmut Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "U. Brendel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "31.08.1981");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Abendkante");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "An der stumpfen Westkante überh. Wand erst gerade, dann rechtshaltend an R vorbei und Hangelrippe zu Band. Riss z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIb"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Karlheinz Güntner");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Schmuck");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "K. Jäschke");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "08.07.1970");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Lange Finger");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "An der Nordkante von Block unterst. Wand erst gerade, dann rechtshaltend an R vorbei und Hangelrippe zu Band. Riss z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIb"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Manfred Vogel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Paul");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "D. Fahr");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "06.10.1984");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Nordkante");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Links der Nordkante (unterst.) Wand und Riss zu Absatz (R) Verschneidung und Kante z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIa"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, Schwierigkeit.SchwierigkeitStrToInt("VIIb"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Gerhard Alde");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "B. Engler");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "J. Schönfelder");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "13.09.1964");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Thüringer Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "2 m links der Nordkante Wnad und Rippe zu R. Wand und Rissspuren z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIb"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Mike Jäger (von unten ges.)");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "16.05.1988");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Nordostriss");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "In Mitte NO-Seite Riß- und Rippenfolge z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("V"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Helmut Marx");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "H. Tittel");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "20.10.1962");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Haarrißspur");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Zwischen Nordostriß und Neuem Weg Wand und Rissspur an R vorbei und Kamin z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("VIIIa"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Frank Richter");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "S. Elsner");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "30.08.1985");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	

	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Neuer Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Links in der NO-Seite Rißspuren z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, Schwierigkeit.SchwierigkeitStrToInt("V"));
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Gisbert Ludewig");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "01.08.1965");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);
	 	    values.clear();	 	
	 	// 2 - Pascher
	 	    values.put(KleFuEntry.COLUMN_NAME_GEBIET, "Erzgebirgsgrenzgebiet");
	 	    values.put(KleFuEntry.COLUMN_NAME_UNTERGEBIET, "Hellendorfer Gebiet"); 
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFEL, "Pascher");
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELNUMMER, "2");
	 	    values.put(KleFuEntry.COLUMN_NAME_NORTH_COORDINATE, "50.812133");
	 	    values.put(KleFuEntry.COLUMN_NAME_EAST_COORDINATE, "14.014576");
	 	    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_GIPFEL,
	 	        "null",
	 	    values); 
	 	    values.clear();
	 	    
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "2");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Alter Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Von Block in der NO-Seite links queren zur SO-Seite. Wand und links Rinne zu Absatz. Nach rechts und Ostkante z. G.");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, "16");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "Erich Kühn");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "R. Gehrke");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "W. Otto");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "01.05.1931");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	    KleFuEntry.TABLE_NAME_WEGE,
	 	    "null",
	 	    values);
	 	    
	 	    
	 	    
	 	    values.clear();
	 	 // Bielatal
	 	    values.put(KleFuEntry.COLUMN_NAME_GEBIET, "Bielatal");
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_GEBIETE,
	 	        "null",
	 	        values);
	 	    
	 	//1 - Brausenstein
	 	    values.put(KleFuEntry.COLUMN_NAME_GEBIET, "Bielatal");
	 	    values.put(KleFuEntry.COLUMN_NAME_UNTERGEBIET, "0"); 
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFEL, "Brausenstein");
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELNUMMER, "1");
	 	    values.put(KleFuEntry.COLUMN_NAME_NORTH_COORDINATE, "50.862611");
	 	    values.put(KleFuEntry.COLUMN_NAME_EAST_COORDINATE, "14.041809");	 	    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_GIPFEL,
	 	        "null",
	 	        values);
	 	   values.clear();
	 	    
	 	    //Wege
	 	    values.put(KleFuEntry.COLUMN_NAME_GIPFELID, "3");
	 	    values.put(KleFuEntry.COLUMN_NAME_WEGNAME, "Alter Weg");  
	 	    values.put(KleFuEntry.COLUMN_NAME_BESCHREIBUNG, "Von NO oder SW zur Scharte. Kamin zwischen Turm und Massiv linkshaltend hochspreizen, Übertritt und Wand dicht rechts der linken Schartenkante zum Gipfel");  
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_AF, "15");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_OU, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_SCHWIERIGKEIT_RP, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER1, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER2, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER3, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHER_ANDERE, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_ERSTBEGEHUNGSDATUM, "0");	    
	 	    values.put(KleFuEntry.COLUMN_NAME_STERN, "0");
	 	    values.put(KleFuEntry.COLUMN_NAME_AUSRUFEZEICHEN, "0");		    
	 	    db.insert(
	 	        KleFuEntry.TABLE_NAME_WEGE,
	 	        "null",
	 	        values);			
	}
}

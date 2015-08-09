package de.climbingguide.erzgebirsgrenzgebiet;

import java.io.File;
import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.os.Environment;
import android.provider.BaseColumns;
import de.climbingguide.erzgebirsgrenzgebiet.db.DBZugriff;

public final class KleFuContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public KleFuContract() {}
    
    public static int anzahltreffer;
    public static ArrayList<Gipfel> listDataHeader; // header titles
    // child data in format of header title, child title
    public static ArrayList<ArrayList<Weg>> listDataChild;
    public static Weg weg=new Weg();
    public static String ANZAHL_ALLE_GIPFEL = "anzahlAlleGipfel";
    
    public static Integer getMaxGipfel(String gebiet) {
    	if (gebiet==null) return KleFuEntry.MAXGIPFEL;
    	if (gebiet.equals("Bielatal"))
    		return 239;
    	else if (gebiet.equals("Erzgebirgsgrenzgebiet"))
    		return 15;
    	else if (gebiet.equals(ANZAHL_ALLE_GIPFEL)) {
    		return 6;
    	}
    	return KleFuEntry.MAXGIPFEL;
    }
        
    /* Inner class that defines the table contents */
    public static abstract class KleFuEntry implements BaseColumns {
        public static String KEY_FRAGMENT_OWN_LIST = "ownlist";
    	
    	public static final String INTENT_EXTRA_CLOSE = "close";
    	
    	public static final int MAXGIPFEL = 239;
        public static final int MAXSCHWIERIGKEIT = 34;
        public static final int MAXSCHWIERIGKEITSPRUNG = 4;
        public static final int MAXSCHWIERIGKEITLUECKE = 6;
    
    	public static final String ARAYLIST_SUCHANFRAGEN = "suchanfrage";
        
    	public static final String TABLE_NAME_GEBIETE = "Gebiete";
        public static final String TABLE_NAME_GIPFEL = "Peaks";
        public static final String TABLE_NAME_WEGE = "Wege";
        public static final String TABLE_NAME_EIGENE_WEGE = "eigenewege";
//        public static final String TABLE_NAME = "tabelle";
        public static final String TABLE_NAME_SUCHANFRAGEN ="Suchanfragen";

        
        public static final String COLUMN_NAME_GEBIET = "Gebiet";
        public static final String COLUMN_NAME_UNTERGEBIET = "Untergebiet";      
        public static final String COLUMN_NAME_GIPFEL = "Gipfel";
        public static final String COLUMN_NAME_GIPFELNUMMER = "Gipfelnummer";
        public static final String COLUMN_NAME_WEGNAME = "Wegname";
        public static final String COLUMN_NAME_BESCHREIBUNG = "Beschreibung";
        public static final String COLUMN_NAME_SCHWIERIGKEIT_AF = "Schwierigkeit_af";
        public static final String COLUMN_NAME_SCHWIERIGKEIT_OU = "Schwierigkeit_oU";
        public static final String COLUMN_NAME_SCHWIERIGKEIT_RP = "Schwierigkeit_RP";
        public static final String COLUMN_NAME_ERSTBEGEHER1 = "Erstbegeher_1";
        public static final String COLUMN_NAME_ERSTBEGEHER2 = "Erstbegeher_2";
        public static final String COLUMN_NAME_ERSTBEGEHER3 = "Erstbegeher_3";
        public static final String COLUMN_NAME_ERSTBEGEHER_ANDERE = "Erstbegeher_andere";
        public static final String COLUMN_NAME_ERSTBEGEHUNGSDATUM = "Erstbegehungsdatum";
        public static final String COLUMN_NAME_STERN = "Stern";
        public static final String COLUMN_NAME_AUSRUFEZEICHEN = "Ausrufezeichen";
        public static final String COLUMN_NAME_NORTH_COORDINATE = "north";
        public static final String COLUMN_NAME_EAST_COORDINATE = "east";
        public static final String COLUMN_NAME_GEKLETTERT = "geklettert";
        public static final String COLUMN_NAME_BESTIEGEN = "bestiegen";
        public static final String COLUMN_NAME_IS_EXPANDED = "isexpanded";
        
        public static final String COLUMN_NAME_SCHWIERIGKEIT_VON = "schwierigkeitVon";
        public static final String COLUMN_NAME_SCHWIERIGKEIT_BIS = "schwierigkeitBis";
        public static final String COLUMN_NAME_NOCH_NICHT_GEKLETTERT = "nochNichtGeklettert";
        public static final String COLUMN_NAME_GIPFELNUMMER_VON = "gipfelnummer_von";
        public static final String COLUMN_NAME_GIPFELNUMMER_BIS = "gipfelnummer_bis";

        public static final String COLUMN_NAME_WEGEID = "wegeid";
        public static final String COLUMN_NAME_GIPFELID = "gipfelid";
        public static final String COLUMN_NAME_VORSTIEG = "vorstieg";
        public static final String COLUMN_NAME_DATUM = "datum";
        public static final String COLUMN_NAME_RP = "rp";
        public static final String COLUMN_NAME_OU = "ou";
        public static final String COLUMN_NAME_PERSONEN = "personen";
        public static final String COLUMN_NAME_BEMERKUNGEN = "bemerkungen";
        public static final String NUMBERS = "numbers";
               
        public static final int ANZAHLGEBIETE = 100;
        public static final int ANZAHLWEGATTRIBUTE = 16;        

//        public static final String INTENT_SEARCH_RESULT = "intent_search_result";
        // Für Download-Nachrichten
    	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
    	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
    	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
    	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
    	public static final int MESSAGE_CONNECTING_STARTED = 1004;
    	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
    	public static final int MESSAGE_ENCOUNTERED_ERROR_FTP_CONNECT = 1007;
    	public static final int MESSAGE_UNZIP = 1006;
    	public static final int MESSAGE_UNZIP_STARTED = 1009;
    	
    	
    	// Für Suche nach Wegen
    	public static final int MESSAGE_SEARCH_STARTED = 1101;
    	public static final int MESSAGE_SEARCH_CANCELED = 1102;
		public static final int MESSAGE_SEARCH_COMPLETED = 1103;
		
		// Für Einrichten der Datenbank
		public static final int MESSAGE_DATABASE_WRITE_STARTED = 1201;
		public static final int MESSAGE_DATABASE_WRITE_FINISHED = 1202;
    	
    	// Für Download - Krams
    	public static final String DOWNLOAD_MAP_FTP_FILE = "/maps/europe/germany/sachsen.map";
    	public static final String DOWNLOAD_FTP_SERVER = "ftp.mapsforge.org";
    	public static final String DOWNLOAD_HTTP_URL_SACHSEN = "http://download.mapsforge.org" + DOWNLOAD_MAP_FTP_FILE;    	
    	public static final String DOWNLOAD_LOCAL_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Elbi/";
    	public static final String DOWNLOAD_LOCAL_MAP_NAME = "sachsen.map";
    	public static final int DOWNLOAD_BUFFER_SIZE = 4096;

//   	public static final String mapURL = "ftp://ftp5.gwdg.de/pub/misc/openstreetmap/openandromaps/maps/Germany/sachsen.zip";
    	public static final String DOWNLOAD_MAP_FTP_FILE_OA = "/pub/misc/openstreetmap/openandromaps/maps/Germany/sachsen.zip";
    	public static final String DOWNLOAD_FTP_SERVER_OA = "ftp5.gwdg.de";
    	public static final String DOWNLOAD_LOCAL_MAP_NAME_OA = "sachsen.zip";  
    	public static final String DOWNLOAD_THEME_URL = "http://www.openandromaps.org/wp-content/users/tobias/Elevate.zip";
    	public static
    	final String ELEVATE_LEGENDE = "http://www.openandromaps.org/kartenlegende/elevation-hike-theme";
    	
    	// Die Datenbank
        public static DBZugriff Zugriff;
        public static SQLiteDatabase db;       
        
        //mapsforge staff
    	/**
    	 * The default number of tiles in the file system cache.
    	 */
    	public static final int FILE_SYSTEM_CACHE_SIZE_DEFAULT = 250;

    	/**
    	 * The maximum number of tiles in the file system cache.
    	 */
    	public static final int FILE_SYSTEM_CACHE_SIZE_MAX = 500;

    	/**
    	 * The default move speed factor of the map.
    	 */
    	public static final int MOVE_SPEED_DEFAULT = 10;

    	/**
    	 * The maximum move speed factor of the map.
    	 */
    	public static final int MOVE_SPEED_MAX = 30;

    	public static final String BUNDLE_CENTER_AT_FIRST_FIX = "centerAtFirstFix";
    	public static final String BUNDLE_SHOW_MY_LOCATION = "showMyLocation";
    	public static final String BUNDLE_SNAP_TO_LOCATION = "snapToLocation";
    	public static final int DIALOG_ENTER_COORDINATES = 0;
    	public static final int DIALOG_INFO_MAP_FILE = 1;
    	public static final int DIALOG_LOCATION_PROVIDER_DISABLED = 2;
//    	public static final FileFilter FILE_FILTER_EXTENSION_MAP = new FilterByFileExtension(".map");
//    	public static final FileFilter FILE_FILTER_EXTENSION_XML = new FilterByFileExtension(".xml");
    	public static final int SELECT_MAP_FILE = 0;
    	public static final int SELECT_RENDER_THEME_FILE = 1;
    	
    	public static final String BREITE = "latitude";
    	public static final String HOHE = "longitude";
    	public static final double DEFAULT_LATITUDE = 50.912016;
    	public static final double DEFAULT_LONGITUDE = 14.200985;
    	public static final int ACTIVITY_LIVEMAP_INTENT_CODE = 255;
    	    
    }
    
    public static Boolean mapFileExists() {
    	File file = new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + KleFuEntry.DOWNLOAD_LOCAL_MAP_NAME);    	
    	return file.exists();
    }
    
    public static boolean isOfflineRenderer(String str){
    	return (str.equals("OSMARENDER") || str.equals("DATABASE_RENDERER_ELEVATE") || str.equals("DATABASE_RENDERER_OPENANDROMAPS") || str.equals("DATABASE_RENDERER_OPENANDROMAPS_LIGHT") || str.equals("DATABASE_RENDERER_OPENANDROMAPS_CYCLE"));
    }
    
}

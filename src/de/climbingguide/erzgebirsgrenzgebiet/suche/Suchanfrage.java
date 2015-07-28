package de.climbingguide.erzgebirsgrenzgebiet.suche;

import de.climbingguide.erzgebirsgrenzgebiet.ClimbingGuideApplication;
import de.climbingguide.erzgebirsgrenzgebiet.Gipfel;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.Schwierigkeit;

public class Suchanfrage {

	String gebiet;
	boolean gebietBekannt;
	Integer gipfelnummerVon;
	Integer gipfelnummerBis;
	String gipfel;
	boolean gipfelBekannt;
	String weg;
	Schwierigkeit schwierigkeitVon;
	Schwierigkeit schwierigkeitBis;
	boolean bereitsGeklettert;
	boolean nochNichtGeklettert;
	String htmlString;
	
	public Suchanfrage(String gebiet, Integer gipfelnummerVon,
			Integer gipfelnummerBis, String gipfel, String weg,
			Schwierigkeit schwierigkeitVon, Schwierigkeit schwierigkeitBis,
			boolean bereitsGeklettert, boolean nochNichtGeklettert) {
		super();
		ClimbingGuideApplication instance = ClimbingGuideApplication.getInstance();
		this.gebiet = gebiet;
		this.gipfelnummerVon = gipfelnummerVon;
		this.gipfelnummerBis = gipfelnummerBis;
		this.gipfel = gipfel;
		this.weg = weg;
		this.schwierigkeitVon = schwierigkeitVon;
		this.schwierigkeitBis = schwierigkeitBis;
		this.bereitsGeklettert = bereitsGeklettert;
		this.nochNichtGeklettert = nochNichtGeklettert;
		
		htmlString = "";
		gebietBekannt = !gebiet.equals("");
		if (gebietBekannt) {
			htmlString = htmlString + instance.getString(R.string.gebiet) + " = "
				+ "<b>" + gebiet + "</b>";
		}
		if (gipfelnummerBis != 0) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			htmlString = htmlString + instance.getString(R.string.gipfelnummer) + 
				" " + instance.getString(R.string.von) + " " + "<b>" + gipfelnummerVon.toString() + "</b>" + " "
				+ instance.getString(R.string.bis) + " " + "<b>" + gipfelnummerBis.toString() + "</b>";
			gipfelBekannt = true;
		} else if (gipfelnummerVon != 0) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			Gipfel gipfelObject = new Gipfel(gipfel);
			htmlString = htmlString + instance.getString(R.string.gipfel) + " = "
				+ gipfelnummerVon.toString() + instance.getString(R.string.minus) +  "<b>" + gipfelObject.getGipfelHtml() + "</b>";
			gipfelBekannt = true;
		} else {
			gipfelBekannt = false;
		}
		if (schwierigkeitBis.getSchwierigkeitInt() != 0) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			htmlString = htmlString + instance.getString(R.string.schwierigkeit) + 
				" " + instance.getString(R.string.von) + " " + "<b>" + schwierigkeitVon.getSchwierigkeitString() + "</b>" + " "
				+ instance.getString(R.string.bis) + " " + "<b>" +  schwierigkeitBis.getSchwierigkeitString() + "</b>";
		} else if (schwierigkeitVon.getSchwierigkeitInt() != 0) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			htmlString = htmlString + instance.getString(R.string.schwierigkeit) +
					" <b>" + schwierigkeitVon.getSchwierigkeitString() + "</b>";
		}
		if (!isBereitsGeklettert() && isNochNichtGeklettert()) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			htmlString = htmlString + instance.getString(R.string.nur) + " " + instance.getString(R.string.noch_nicht_geklettert);			
		} else if (isBereitsGeklettert() && !isNochNichtGeklettert()) {
			if (!htmlString.equals("")) htmlString = htmlString + "<br>";
			htmlString = htmlString + instance.getString(R.string.nur) + " " + instance.getString(R.string.bereits_geklettert);			 			
		}
		
		if (htmlString.equals("")) htmlString = instance.getString(R.string.alle_wege);

	}
	
	public String getGebiet() {
		return gebiet;
	}
	public void setGebiet(String gebiet) {
		this.gebiet = gebiet;
	}
	public Integer getGipfelnummerVon() {
		return gipfelnummerVon;
	}
	public void setGipfelnummerVon(Integer gipfelnummerVon) {
		this.gipfelnummerVon = gipfelnummerVon;
	}
	public Integer getGipfelnummerBis() {
		return gipfelnummerBis;
	}
	public void setGipfelnummerBis(Integer gipfelnummerBis) {
		this.gipfelnummerBis = gipfelnummerBis;
	}
	public String getGipfel() {
		return gipfel;
	}
	public void setGipfel(String gipfel) {
		this.gipfel = gipfel;
	}
	public String getWeg() {
		return weg;
	}
	public void setWeg(String weg) {
		this.weg = weg;
	}
	public Schwierigkeit getSchwierigkeitVon() {
		return schwierigkeitVon;
	}
	public void setSchwierigkeitVon(Schwierigkeit schwierigkeitVon) {
		this.schwierigkeitVon = schwierigkeitVon;
	}
	public Schwierigkeit getSchwierigkeitBis() {
		return schwierigkeitBis;
	}
	public void setSchwierigkeitBis(Schwierigkeit schwierigkeitBis) {
		this.schwierigkeitBis = schwierigkeitBis;
	}
	public boolean isBereitsGeklettert() {
		return bereitsGeklettert;
	}
	public void setBereitsGeklettert(boolean bereitsGeklettert) {
		this.bereitsGeklettert = bereitsGeklettert;
	}
	public boolean isNochNichtGeklettert() {
		return nochNichtGeklettert;
	}
	public void setNochNichtGeklettert(boolean nochNichtGeklettert) {
		this.nochNichtGeklettert = nochNichtGeklettert;
	}
	
	public boolean isGebietBekannt() {
		return gebietBekannt;
	}

	public void setGebietBekannt(boolean gebietBekannt) {
		this.gebietBekannt = gebietBekannt;
	}


	public boolean isGipfelBekannt() {
		return gipfelBekannt;
	}

	public void setGipfelBekannt(boolean gipfelBekannt) {
		this.gipfelBekannt = gipfelBekannt;
	}
	
	public String getHtmlString() {
		return htmlString;
	}	
	
}

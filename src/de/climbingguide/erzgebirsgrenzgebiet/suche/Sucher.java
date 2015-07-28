package de.climbingguide.erzgebirsgrenzgebiet.suche;


public interface Sucher {
	//Filedownload Zeug
	public SucheThread getSucheThread();
	public SucheHandler getSucheHandler();
	public Boolean getGebietBekannt();
	public String getGebiet();
	public String getGipfel();
	public String getWegname();
	public Integer getGipfelnummer();
	public Integer getGipfelnummerBis();
	public Boolean getGipfelBekannt();
	public Integer getSchwierigkeitBisInt();
	public Integer getSchwierigkeitVonInt();
	public Boolean isSchonGeklettert();
	public Boolean isNochNichtGeklettert();	
}

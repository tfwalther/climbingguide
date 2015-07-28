package de.climbingguide.erzgebirsgrenzgebiet;

public class Schwierigkeit {

	private int schwierigkeit;
	
	public static String SchwierigkeitIntToString(int i) {
		switch (i) {
		case 1: return "1";
		case 2: return "2";
		case 3: return "3";
		case 4: return "4";
		case 11: return "I";
		case 12: return "II";
		case 13: return "III";
		case 14: return "IV";
		case 15: return "V";
		case 16: return "VI";
		case 17: return "VIIa";
		case 18: return "VIIb";
		case 19: return "VIIc";
		case 20: return "VIIIa";
		case 21: return "VIIIb";
		case 22: return "VIIIc";
		case 23: return "IXa";
		case 24: return "IXb";
		case 25: return "IXc";
		case 26: return "Xa";
		case 27: return "Xb";
		case 28: return "Xc";
		case 29: return "XIa";
		case 30: return "XIb";
		case 31: return "XIc";
		case 32: return "XIIa";
		case 33: return "XIIb";
		case 34: return "XIIc";
		}
		return "";
	}

	public static int SchwierigkeitStrToInt(String s) {
		if (s.equals("I")) return 11;
		if (s.equals("II")) return 12;
		if (s.equals("III")) return 13;
		if (s.equals("IV")) return 14;
		if (s.equals("V")) return 15;
		if (s.equals("VI")) return 16;
		if (s.equals("VIIa")) return 17;
		if (s.equals("VIIb")) return 18;
		if (s.equals("VIIc")) return 19;
		if (s.equals("VIIIa")) return 20;
		if (s.equals("VIIIb")) return 21;
		if (s.equals("VIIIc")) return 22;
		if (s.equals("IXa")) return 23;
		if (s.equals("IXb")) return 24;
		if (s.equals("IXc")) return 25;
		if (s.equals("Xa")) return 26;
		if (s.equals("Xb")) return 27;
		if (s.equals("Xc")) return 28;
		if (s.equals("XIa")) return 29;
		if (s.equals("XIb")) return 30;
		if (s.equals("XIc")) return 31;
		if (s.equals("XIIa")) return 32;
		if (s.equals("XIIb")) return 33;
		if (s.equals("XIIc")) return 34;
		if (s.equals("1")) return 1;
		if (s.equals("2")) return 2;
		if (s.equals("3")) return 3;
		if (s.equals("4")) return 4;
		return 0;
	}	
	
	//Konstruktoren	
	public Schwierigkeit(int i) {
		schwierigkeit = i;
	}
	
	public Schwierigkeit(String string) {
		schwierigkeit = SchwierigkeitStrToInt(string);
	}
	
	//Methoden
	public int getSchwierigkeitInt() {
		return schwierigkeit;
	}
	
	public String getSchwierigkeitString(){		
		return SchwierigkeitIntToString(schwierigkeit);
	}
	
	public boolean isEnabled() {
		if (schwierigkeit == 0) return false;
		return true;
	}
}

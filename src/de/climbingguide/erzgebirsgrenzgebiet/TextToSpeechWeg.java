package de.climbingguide.erzgebirsgrenzgebiet;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TextToSpeechWeg extends TextToSpeech {
		
	public TextToSpeechWeg(Context context, OnInitListener listener) {
		super(context, listener);
	}
	
	public void speak(Weg weg) {
		super.speak(weg.getGipfel(), TextToSpeech.QUEUE_FLUSH, null);		
		super.speak(expandAbbreviatrions(weg.getWegname()), TextToSpeech.QUEUE_ADD, null);		
		super.speak(expandAbbreviatrions(weg.getBeschreibung()), TextToSpeech.QUEUE_ADD, null);
	}
	
	private String expandAbbreviatrions(String text) {
		text = text.replace(" N ", " Nord ");
		text = text.replace(" NO ", " Nordost ");
		text = text.replace(" O ", " Ost ");
		text = text.replace(" SO ", " Südost ");
		text = text.replace(" S ", " Süd ");
		text = text.replace(" SW ", " Südwest ");
		text = text.replace(" W ", " West ");
		text = text.replace(" NW ", " Nordwest ");

		text = text.replace(" N-", " Nord-");
		text = text.replace(" NO-", " Nordost-");
		text = text.replace(" O-", " Ost-");
		text = text.replace(" SO-", " Südost-");
		text = text.replace(" S-", " Süd-");
		text = text.replace(" SW-", " Südwest-");
		text = text.replace(" W-", " West-");
		text = text.replace(" NW-", " Nordwest-");
		
		text = text.replace(" m ", " Meter ");
		text = text.replace(" R ", " Ring ");
		text = text.replace(" Abs. ", " Absatz ");
		
		text = text.replace(" überh.", "überhängende");
		double zufallszahl = Math.random();

		text = text.replace(" AW ", " A W ");
		
		if (zufallszahl < 0.3) {
			text = text.replace("z. G.", "zum Gipfel.");
		} else if (zufallszahl < 0.5) {
			text = text.replace("z. G.", "zum Gasthaus.");
		} else {

		}
		return text;
	}

}

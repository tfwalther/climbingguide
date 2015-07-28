package de.climbingguide.erzgebirsgrenzgebiet.list;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class ListViewChildHolder {
	protected TextView textViewWegname;
	protected TextView textViewSchwierigkeit;
	protected CheckBox checkBoxGeklettert;
	
	protected LinearLayout detailsLayout;
	protected RelativeLayout nonDetailsLayout;
	
	protected TextView textViewDetWeg;
	protected TextView textViewDetSchwierigkeit;
	protected TextView textViewDetBeschreibung;
	protected CheckBox checkBoxDetBereitsGeklettert;
	protected ImageButton buttonDetVorlesen;
	protected CheckBox checkBoxDetVorstieg;
	protected CheckBox checkBoxDetRP;
	protected CheckBox checkBoxoU;
	
	protected LinearLayout linearLayoutGeklettert;
}

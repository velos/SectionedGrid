package com.velosmobile.demo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.velosmobile.utils.R;
import com.velosmobile.utils.SectionableAdapter;

/**
 * A sample Adapter that demonstrates one use of the SectionableAdapter class,
 * using a hard-coded two-dimensional array for the data source.
 * 
 * @author Velos Mobile
 */
/*
 * Copyright © 2012 Velos Mobile
 */
public class BookcaseAdapter extends SectionableAdapter implements
		View.OnClickListener {

	// For simplicity, we hard-code the headers and data. In an actual app, this
	// can come from the network, the filesystem, SQLite, or any of the 
	// usual suspects.
	static final String[] AUTHORS = new String[] { "Roberto Bola–o",
			"David Mitchell", "Haruki Murakami", "Thomas Pynchon" };
	private static final String[][] BOOKS = new String[][] {
			{ "The Savage Detectives", "2666" },
			{ "Ghostwritten", "number9dream", "Cloud Atlas",
					"Black Swan Green", "The Thousand Autumns of Jacob de Zoet" },
			{ "A Wild Sheep Chase",
					"Hard-Boiled Wonderland and the End of the World",
					"Norwegian Wood", "Dance Dance Dance",
					"South of the Border, West of the Sun",
					"The Wind-Up Bird Chronicle", "Sputnik Sweetheart",
					"Kafka on the Shore", "After Dark", "1Q84" },
			{ "V.", "The Crying of Lot 49", "Gravity's Rainbow", "Vineland",
					"Mason & Dixon", "Against the Day", "Inherent Vice" } };

	private Activity activity;

	public BookcaseAdapter(Activity activity, LayoutInflater inflater,
			int rowLayoutID, int headerID, int itemHolderID, int resizeMode) {
		super(inflater, rowLayoutID, headerID, itemHolderID, resizeMode);
		this.activity = activity;
	}

	@Override
	public Object getItem(int position) {
		for (int i = 0; i < BOOKS.length; ++i) {
			if (position < BOOKS[i].length) {
				return BOOKS[i][position];
			}
			position -= BOOKS[i].length;
		}
		// This will never happen.
		return null;
	}

	@Override
	protected int getDataCount() {
		int total = 0;
		for (int i = 0; i < BOOKS.length; ++i) {
			total += BOOKS[i].length;
		}
		return total;
	}

	@Override
	protected int getSectionsCount() {
		return BOOKS.length;
	}

	@Override
	protected int getCountInSection(int index) {
		return BOOKS[index].length;
	}

	@Override
	protected int getTypeFor(int position) {
		int runningTotal = 0;
		int i = 0;
		for (i = 0; i < BOOKS.length; ++i) {
			int sectionCount = BOOKS[i].length;
			if (position < runningTotal + sectionCount)
				return i;
			runningTotal += sectionCount;
		}
		// This will never happen.
		return -1;
	}

	@Override
	protected String getHeaderForSection(int section) {
		return AUTHORS[section];
	}

	@Override
	protected void bindView(View convertView, int position) {
		String title = (String) getItem(position);
		TextView label = (TextView) convertView
				.findViewById(R.id.bookItem_title);
		label.setText(title);
		convertView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_SEARCH);
		TextView label = (TextView) v.findViewById(R.id.bookItem_title);
		String text = label.getText().toString();
		i.putExtra(SearchManager.QUERY, text);
		activity.startActivity(i);
	}

}

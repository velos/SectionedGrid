package com.velosmobile.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.velosmobile.utils.R;

/**
 * A sample application that demonstrates use of the SectionableAdapter to
 * show a scrolling, sectioned grid representing books on a bookshelf.
 * @author Velos Mobile.
 */
/*
 * Copyright © 2012 Velos Mobile
 */
public class SectionedGridActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sectioned_grid);

		ListView list = (ListView) findViewById(R.id.sectionedGrid_list);
		BookcaseAdapter adapter = new BookcaseAdapter(this,
				getLayoutInflater(), R.layout.book_row, R.id.bookRow_header,
				R.id.bookRow_itemHolder);
		list.setAdapter(adapter);
		list.setDividerHeight(0);
	}

}

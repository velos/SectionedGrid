package com.velosmobile.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An Adapter that populates a grid of 1-n columns. Unlike a standard Android GridView,
 * lists using this Adapter can label sections within the grid, and can include shorter
 * rows that do not fill each column space. Subclasses must define their sections and
 * contents.
 * @author Velos Mobile
 */
/*
 * Copyright © 2012 Velos Mobile
 */
public abstract class SectionableAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int rowResID;
	private int headerID;
	private int itemHolderID;
	private int colCount;
	private int sectionsCount;
	
	/**
	 * Constructor.
	 * @param inflater inflater to create rows within the grid.
	 * @param rowLayoutID layout resource ID for each row within the grid.
	 * @param headerID resource ID for the header element contained within the grid row.
	 * @param itemHolderID resource ID for the cell wrapper contained within the grid row. This View must only contain cells.
	 */
	public SectionableAdapter(LayoutInflater inflater, int rowLayoutID, int headerID, int itemHolderID)
	{
		super();
		this.inflater = inflater;
		this.rowResID = rowLayoutID;
		this.headerID = headerID;
		this.itemHolderID = itemHolderID;
		// Determine how many columns our row holds.
		View row = inflater.inflate(rowLayoutID, null);
		ViewGroup holder = (ViewGroup)row.findViewById(itemHolderID);
		colCount = holder.getChildCount();
		sectionsCount = getSectionsCount();
	}
	
	/**
	 * Returns the total number of items to display.
	 */
	protected abstract int getDataCount();
	
	/**
	 * Returns the number of sections to display.
	 */
	protected abstract int getSectionsCount();
	
	/**
	 * @param index the 0-based index of the section to count.
	 * @return the number of items in the requested section.
	 */
	protected abstract int getCountInSection(int index);
	
	/**
	 * @param position the 0-based index of the data element in the grid.
	 * @return which section this item belongs to.
	 */
	protected abstract int getTypeFor(int position);
	
	/**
	 * @param section the 0-based index of the section.
	 * @return the text to display for this section.
	 */
	protected abstract String getHeaderForSection(int section);
	
	/**
	 * Populate the View and attach any listeners.
	 * @param cell the inflated cell View to populate.
	 * @param position the 0-based index of the data element in the grid.
	 */
	protected abstract void bindView(View cell, int position);
	
	/**
	 * Perform any row-specific customization your grid requires. For example, you could add a header to the
	 * first row or a footer to the last row.
	 * @param row the 0-based index of the row to customize.
	 * @param convertView the inflated row View.
	 */
	protected void customizeRow(int row, View rowView)
	{
		// By default, does nothing. Override to perform custom actions.
	}
	
	@Override
	public int getCount()
	{
		int totalCount = 0;
		for (int i = 0; i < sectionsCount; ++i)
		{
			int count = getCountInSection(i);
			if (count > 0)
				totalCount += (getCountInSection(i)-1) / colCount + 1;
		}
		if (totalCount == 0)
			totalCount = 1;
		return totalCount;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int realPosition = 0;
		int viewsToDraw = 0;
		int rows = 0;
		int totalCount = 0;
		for (int i = 0; i < sectionsCount; ++i)
		{
			int sectionCount = getCountInSection(i);
			totalCount += sectionCount;
			if (sectionCount > 0 && position <= rows + (sectionCount - 1) / colCount)
			{
				realPosition += (position - rows) * colCount;
				viewsToDraw = (int)(totalCount - realPosition);
				break;
			}
			else
			{
				if (sectionCount > 0)
				{
					rows += (int)((sectionCount - 1) / colCount + 1);
				}
				realPosition += sectionCount;
			}
		}
		// Phew... okay! Now we just need to build it!
		if (convertView == null)
		{
			convertView = inflater.inflate(rowResID, parent, false);
		}
		int lastType = -1;
		if (realPosition > 0)
			lastType = getTypeFor(realPosition-1);
		if (getDataCount() > 0)
		{
			TextView header = (TextView)convertView.findViewById(headerID);
			int newType = getTypeFor(realPosition);
			if (newType != lastType)
			{
				header.setVisibility(View.VISIBLE);
				header.setText(getHeaderForSection(newType));

			}
			else
			{
				header.setVisibility(View.GONE);
			}
		}
		customizeRow(position, convertView);

		for (int i = 0; i < colCount; ++i)
		{
			View child = ((ViewGroup)convertView.findViewById(itemHolderID)).getChildAt(i);
			if (i < viewsToDraw && child != null)
			{
				bindView(child, realPosition + i);
				child.setVisibility(View.VISIBLE);
			}
			else if (child != null)
			{
				child.setVisibility(View.INVISIBLE);
			}
		}
		return convertView;
	}
	
}

/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample;

import java.io.File;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.epapyrus.plugpdf.core.PDFDocument;
import com.epapyrus.plugpdf.core.PlugPDFException.WrongPassword;
import com.epapyrus.plugpdf.sample.documentView.ReaderWithControllerActivity;
import com.epapyrus.plugpdf.sample.documentViewWithoutController.ReaderActivity;

/**
 * Fetches and displays the PDF document(s) on the DIRECTORY_DOWNLOADS path
 * (i.e. '/mnt/sdcard/Download') in ListView.
 *  
 * @author ePapyrus
 * @see <a target="_blank" href="https://developer.android.com/reference/android/widget/ListView.html">android.widget.ListView</a>
 */

public class PDFListActivity extends Activity implements OnItemClickListener {

	protected File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	private LinearLayout mLayout;
	private String intentType;
	
	/**
	 * Sets the activity content, creating a layout.
	 * 
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle)">android.app.Activity.onCreate(android.os.Bundle)</a>
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		intentType = getIntent().getStringExtra("TYPE");

		mLayout = new LinearLayout(this);
		
		setContentView(mLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		refreshList();
	}
	
	/**
	 * Fetches the files from the directory, adds a click listener and lists the PDF document(s) in
	 * {@link ListView}. If there are no files available and the intent type doesn't start
	 * with "Reader", the user is asked to store a document; otherwise, opens the sample PDF just
	 * after a screen touch takes place.
	 *
	 * @see <a target="_blank" href="https://developer.android.com/reference/android/widget/ListView.html">android.widget.ListView</a>
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/content/Intent.html">android.content.Intent</a>
	 */
	protected void refreshList() {
		
		mLayout.removeAllViews();
		
		File[] files = downloadDir.listFiles();
		
		if (files != null && files.length > 0) {

			ListView listView = new ListView(this);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pdf_list_item, R.id.list_title) {
				
				@Override
				public View getView(int position, View convertView, android.view.ViewGroup parent) {
					
					// Get TextView from super
					View v = super.getView(position, convertView, parent);
					
					String fileName = getItem(position);
					
					try {
						
						ImageView lockIcon = (ImageView) v.findViewById(R.id.list_lock_icon);
						lockIcon.setVisibility(View.INVISIBLE);
						
						PDFDocument doc = new PDFDocument(
							new File(downloadDir, fileName).getAbsolutePath(), "");
						
						doc.release();
						
					} catch (WrongPassword e) {
						
						ImageView lockIcon = (ImageView) v.findViewById(R.id.list_lock_icon);
						lockIcon.setVisibility(View.VISIBLE);
						
					} catch (Exception e) {
						
						Log.e("PlugPDF", "[ERROR] " + e.getMessage());
						
					}
					
					return v;
				};
			};
			
			listView.setAdapter(adapter);
		
			Arrays.sort(files);
			
			// Add a PDF file to the list
			for (File file : files) {
				if (file.getName().toLowerCase().endsWith(".pdf")) {
					adapter.add(file.getName());
				}
			}
		
			listView.setOnItemClickListener(this);
			
			mLayout.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
		} else {
			
			TextView textView = new TextView(this);
			String text = "Please put file in Download directory on your SD Card.";
			
			if (willOpenReader()) {
				text += " If you touch to this screen will open the sample PDF.";
				textView.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View v) {
						Intent readerIntent = getIntent(intentType);
						startActivity(readerIntent);
					}
				});
			}
			
			textView.setText(text);
			textView.setTextSize(20.f);
			mLayout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
		}
	}
	
	/* (non-Javadoc)
	 * @see <a target="_blank" href="https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener.html#onItemClick%28android.widget.AdapterView%3C?%3E,%20android.view.View,%20int,%20long%29">android.widget.AdapterView.OnItemClickListener.onItemClick(android.widget.AdapterView, android.view.View, int, long)</a>
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long longIndex) {
		Intent readerIntent = getIntent(intentType);
		readerIntent.putExtra("fileName", downloadDir.getAbsolutePath() + "/" + parent.getItemAtPosition(index));
		startActivity(readerIntent);
	}

	/**
	 * Checks the intent type, whether it starts with the keyword 'READER' or not
	 *
	 * @return boolean
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/content/Intent.html">android.content.Intent</a>
	 */
	private boolean willOpenReader() {
		return intentType.startsWith("READER");
	}

	/**
	 * Given an intent type, determines and returns its corresponding activity
	 *
	 * @param intentType
	 * @return Intent of type {@link ReaderWithControllerActivity} or {@link ReaderActivity}, or null
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/content/Intent.html">android.content.Intent</a>
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Activity.html">android.app.Activity</a>
	 */
	private Intent getIntent(String intentType) {
		
		if (intentType.equals("READER"))
			return new Intent(this, ReaderWithControllerActivity.class);
		
		else if (intentType.equals("READER_WITHOUT_CONTROLLER"))
			return new Intent(this, ReaderActivity.class);
		
		else 
			return null;
	}
}

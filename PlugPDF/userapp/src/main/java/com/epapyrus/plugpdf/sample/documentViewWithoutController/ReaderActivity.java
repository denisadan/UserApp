/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.documentViewWithoutController;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.epapyrus.plugpdf.PasswordDialog;
import com.epapyrus.plugpdf.core.viewer.BasePlugPDFDisplay.PageDisplayMode;
import com.epapyrus.plugpdf.core.viewer.DocumentState.OPEN;
import com.epapyrus.plugpdf.core.viewer.ReaderListener;
import com.epapyrus.plugpdf.core.viewer.ReaderView;
import com.epapyrus.plugpdf.sample.PlugPDFLauncherApplication;

/**
 * Simple PDF document viewer with no controllers such as a top toolbar or a bottom slide bar.
 * Please see {@link PlugPDFLauncherApplication}
 *
 * @author ePapyrus
 * @see <a href="http://www.plugpdf.com">http://www.plugpdf.com</a>
 */
public class ReaderActivity extends Activity {

	private ReaderView mReader;
	
	/**
	 * Displays a PDF document by creating the simple reader view, sets the reader listener after
	 * adding some basic functions: single tap, on scroll, double tap, change zoom, change display,
	 * go to page, and search finish.
	 *    
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the file name selected by the user
		Intent intent = getIntent();
		final String fileName = intent.getStringExtra("fileName");
		
		LinearLayout layout = new LinearLayout(this);
		setContentView(layout);
		
		mReader = new ReaderView(this);

		//mReader.setPageDisplayMode(PageDisplayMode.BILATERAL_REALISTIC);
		
		// Handle failure due to wrong password
		mReader.setReaderListener(new ReaderListener() {
			
			@Override
			public void onLoadFinish(OPEN state) {
				if (state == OPEN.WRONG_PASSWD) {
					PasswordDialog dialog = new PasswordDialog(ReaderActivity.this) {
						public void onInputtedPassword(String password) {
							openPDF(fileName, password);
						}
					};
					dialog.show();
				}
			}
			
			public void onSingleTapUp(MotionEvent e) {}
			public void onSearchFinish(boolean success) {}
			public void onScroll(int distanceX, int distanceY) {}
			public void onGoToPage(int pageIdx, int pageCount) {} 
			public void onDoubleTapUp(MotionEvent e) {}
			public void onChangeZoom(double zoomLevel) {}
			public void onChangeDisplayMode(PageDisplayMode mode, int pageIndex) {}
			public void onLongPress(MotionEvent e) {}
		});
		
		openPDF(fileName, "");
		
		layout.addView(mReader);
	}
	
	/**
	 * Opens a PDF document according to the given path and password
	 * 
	 * @param fileName The PDF file's path and name
	 * @param password Password used to unlock an encrypted document (if unencrypted use "")
	 */
	private void openPDF(String fileName, String password) {
		if (fileName != null && fileName.length() > 0) {
			mReader.openFile(fileName, password);
			
		} else { // If did not select file, open file in the assets.
			byte[] data = readAssetFile("Gone_With_the_Wind.pdf");
			mReader.openData(data, data.length, password);
			
		}
	}
	
	/**
	 * Converts the given PDF file into an array of bytes.
	 *
	 * @param fileName The PDF document to convert
	 * @return data The PDF document converted into byte array
	 * @throws exception Informs about why the document couldn't be opened
	 */
	private byte[] readAssetFile(String fileName) {
		
		AssetManager am = getResources().getAssets();
		byte[] data = null;
		
		try {
			
			InputStream is = am.open(fileName);

			int size = is.available();
			if (size > 0) {
				data = new byte[size];
				is.read(data);
			}

			is.close();
			
		} catch (Exception ex) {
			Log.e("PlugPDF", "[ERROR] open fail because, ", ex);
		}
		
		return data;
	}
	
	/**
	 * Saves the document by checking its availability just before destroying this activity.
	 *
	 * If the activity was opened with openData(...), then it will be saved on the root folder of
	 * your device's external storage.
	 *
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (mReader.getDocument() != null) {
			mReader.save();
			mReader.clear();
		}
		super.onDestroy();
	}
}

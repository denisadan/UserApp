/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.documentView;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.epapyrus.plugpdf.SimpleDocumentReader;
import com.epapyrus.plugpdf.SimpleDocumentReaderListener;
import com.epapyrus.plugpdf.SimpleReaderFactory;
import com.epapyrus.plugpdf.core.annotation.acroform.BaseField.FieldState;
import com.epapyrus.plugpdf.core.annotation.acroform.CheckBoxField;
import com.epapyrus.plugpdf.core.annotation.acroform.CustomCheckBoxPainter;
import com.epapyrus.plugpdf.core.viewer.DocumentState.OPEN;
import com.epapyrus.plugpdf.core.viewer.ReaderView;
import com.epapyrus.plugpdf.sample.PlugPDFLauncherApplication;
import com.epapyrus.plugpdf.sample.R;

/**
 * Simple PDF document viewer with controllers such as a top toolbar or a bottom slide bar.
 * Please see {@link PlugPDFLauncherApplication}
 *
 * @author ePapyrus
 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Activity.html">Activity</a>
 */
public class ReaderWithControllerActivity extends Activity {

	private SimpleDocumentReader mReader;

	/**
	 * Displays a PDF by creating simple PDF viewer, which is used for viewing, reading and
	 * browsing the document.
	 *
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle)">android.app.Activity.onCreate(android.os.Bundle)</a>
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the file name selected by the user
		Intent intent = getIntent();
		String fileName = intent.getStringExtra("fileName");

		mReader = SimpleReaderFactory.createSimpleViewer(this, listener);
		if (fileName != null && fileName.length() > 0) {
			mReader.openFile(fileName, "");

		} else { // If did not select file, open file in the assets.
            byte[] data = new byte[0];
            try {
                data = readAssetFile("Gone_With_the_Wind.pdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            mReader.openData(data, data.length, "");

		}

		mImgChecked = getResources().getDrawable(R.drawable.cb_checked);
		mImgNormal = getResources().getDrawable(R.drawable.cb_normal);
		mImgDisabled = getResources().getDrawable(R.drawable.cb_disabled);

		CheckBoxField.setGlobalCustomPainter(customPainter);

		ReaderView.setEnableUseRecentPage(true);
	}

	private Drawable mImgChecked;
	private Drawable mImgNormal;
	private Drawable mImgDisabled;

	/**
	 * Checkbox painter. The checkbox image can be changed by implementing its draw method.
	 */
	private CustomCheckBoxPainter customPainter = new CustomCheckBoxPainter() {

		@Override
		public Drawable draw(CheckBoxField field, Canvas canvas) {
			Drawable img = mImgNormal;
			if (field.getFieldState() == FieldState.DISABLE) {
				img = mImgDisabled;
			} else {
				if (field.isChecked()) {
					img = mImgChecked;
				}
			}
			img.setBounds(0, 0, field.getWidth(), field.getHeight());
			img.draw(canvas);
			return img;
		}
	};

	/**
	 * Receives event notifications when a PDF document completes loading on a SimpleDocumentReader.
	 */
	private SimpleDocumentReaderListener listener = new SimpleDocumentReaderListener() {

		@Override
		public void onLoadFinish(OPEN state) {
			Log.i("PlugPDF", "[INFO] Open " + state);
		}
	};

	/**
	 * Converts the given PDF file into an array of bytes.
	 *
	 * @param fileName The PDF document to convert
	 * @return data The PDF document converted into byte array
	 * @throws Exception Informs about why the document couldn't be opened
	 */
	private byte[] readAssetFile(String fileName) throws Exception {

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
            ex.printStackTrace();
            throw(ex);
		}

		return data;
	}

	/**
	 * Saves the document by checking its availability just before destroying this activity.
	 *
	 * If the activity was opened with openData(...), then it will be saved on the root folder of
	 * the user's device's external storage.
	 *
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Activity.html#onDestroy()">android.app.Activity.onDestroy()</a>
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

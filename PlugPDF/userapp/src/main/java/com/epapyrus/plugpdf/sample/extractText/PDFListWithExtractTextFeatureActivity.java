/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.extractText;

import java.io.File;
import java.io.FileWriter;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.epapyrus.plugpdf.PasswordDialog;
import com.epapyrus.plugpdf.core.PDFDocument;
import com.epapyrus.plugpdf.core.PlugPDFException.WrongPassword;
import com.epapyrus.plugpdf.sample.FileDialog;
import com.epapyrus.plugpdf.sample.FileDialog.FileDialogListener;
import com.epapyrus.plugpdf.sample.PDFListActivity;

/**
 * Implements the process of extracting some text from a PDF document.
 *
 * @author ePapyrus
 */
public class PDFListWithExtractTextFeatureActivity extends PDFListActivity {

	private PDFDocument doc;

	/* (non-Javadoc)
	 * See {@link PDFListActivity}
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		File targetPDF = new File(downloadDir, parent.getItemAtPosition(
				position).toString());
		
		openDocument(targetPDF.getAbsolutePath(), "");
	}

	/**
	 * Extracts the selected text from a given PDF document, showing the user a dialog,
	 * and asking him/her to pick a destination file in which to save the extracted text.
	 */
	private void showDialog() {
		FileDialog fileDialog = new FileDialog(PDFListWithExtractTextFeatureActivity.this, new FileDialogListener() {
			
			@Override
			public void onChosenFile(String filePath) {
				try {
					
					String extractText = "";
					
					for (int i = 0; i < doc.getPageCount(); i++) {
						PointF pageSize = doc.getPageSize(i);
						RectF area = new RectF(0, 0, pageSize.x, pageSize.y);
						extractText += doc.extractText(i, area);
					}
					
					FileWriter writer = new FileWriter(new File(filePath));
					writer.write(extractText);
					writer.close();
					
					doc.release();
					
				} catch (Exception e) {
					Log.e("PlugPDF", "[ERROR] write fail because, ", e);
				}
			}
		});
		
		fileDialog.show();
	}

	/**
	 * Given a path and a password, creates the corresponding PDF document.
	 *
	 * @param fileName The path and name of the accessible PDF document
	 * @param password Password used to unlock an encrypted document (if unencrypted use "")
	 * @throws Exception The exception thrown when the document cannot be opened
	 * @throws WrongPassword The exception that is thrown when the PDF password is not correct
	 */
	public void openDocument(final String fileName, String password) {
		
		try {
		
			doc = new PDFDocument(fileName, password);
		
			if (doc != null) {
				showDialog();
			}
			
		} catch (WrongPassword e) {
			PasswordDialog dialog = new PasswordDialog(this) {
				public void onInputtedPassword(String password) {
					openDocument(fileName, password);
				}
			};
			dialog.show();
		} catch (Exception e) {
			Log.e("PlugPDF", "[ERROR] open fail because, ", e);
		}
	}
}

/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.mergePDF;

import java.io.File;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epapyrus.plugpdf.PasswordDialog;
import com.epapyrus.plugpdf.core.PDFDocument;
import com.epapyrus.plugpdf.core.PlugPDFException.WrongPassword;
import com.epapyrus.plugpdf.core.PlugPDFUtility;
import com.epapyrus.plugpdf.sample.PDFListActivity;

/**
 * Implements the process of merging two PDF documents into a single one.
 *
 * @author ePapyrus
 */
public class PDFListWithMergeFeatureActivity extends PDFListActivity {

	private PDFDocument srcDoc1;
	private PDFDocument srcDoc2;
	private EditText dest;
	private Toast toast;
	private Button mergeBtn;
	private Button cancelBtn;

	/**
	 * Item listener that allows to select any two PDF documents.
	 *
	 * See {@link PDFListActivity}
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		File targetPDF = new File(downloadDir, parent.getItemAtPosition(
				position).toString());

		if (srcDoc1 == null) {

			toast = Toast.makeText(this, "Select second PDF to merge", Toast.LENGTH_LONG);
			toast.show();

			openDocument(targetPDF.getAbsolutePath(), "");
			
		} else if (srcDoc2 == null) {

			openDocument(targetPDF.getAbsolutePath(), "");

		}
	}


	/**
	 * Merges two PDF documents and saves the result as a new PDF.
	 */
	public void mergePDF() {

		for (int i = 0; i < srcDoc2.getPageCount(); i++) {
			srcDoc1.transplantPage(srcDoc2, i);
		}

		File targetPDF = new File(downloadDir, dest.getText()
				.toString() + ".pdf");

		srcDoc1.saveAsFile(targetPDF.getAbsolutePath());

		srcDoc1.release();
		srcDoc2.release();

		srcDoc1 = null;
		srcDoc2 = null;

	}

	/**
	 * Creates a dialog for merging two PDF documents.
	 *
	 * @return dialog
	 */
	private Dialog createDialog() {

		Dialog dialog = new Dialog(this);
		dialog.setTitle("Merge PDFs");

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(20, 0, 20, 0);

		dialog.setContentView(layout);

		TextView destLabel = new TextView(this);
		dest = new EditText(this);
		TextView destLabel2 = new TextView(this);
		mergeBtn = new Button(this);
		cancelBtn = new Button(this);

		destLabel.setText("Output File name:");
		destLabel2.setText(".pdf");
		mergeBtn.setText("Merge");
		cancelBtn.setText("Cancel");

		dest.setWidth((int) PlugPDFUtility.convertDipToPx(this, 100));

		LinearLayout destLayout = new LinearLayout(this);
		destLayout.addView(destLabel);
		destLayout.addView(dest);
		destLayout.addView(destLabel2);

		LinearLayout btnLayout = new LinearLayout(this);
		btnLayout.addView(mergeBtn);
		btnLayout.addView(cancelBtn);

		layout.addView(destLayout);
		layout.addView(btnLayout);

		return dialog;
	}


	/**
	 * Sets the click listener to the merging dialog buttons.
	 *
	 * @param dialog UI dialog for merging two PDF documents
	 */
	private void setListenerToBtn(final Dialog dialog) {

		mergeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mergePDF();

				refreshList();

				dialog.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				srcDoc1 = null;
				srcDoc2 = null;

				dialog.dismiss();
			}
		});
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
		
			if (srcDoc1 == null) {
				srcDoc1 = new PDFDocument(fileName, password);
			} else {
				srcDoc2 = new PDFDocument(fileName, password);
			}
		
			if (srcDoc1 != null && srcDoc2 != null) {
				Dialog dialog = createDialog();
	
				setListenerToBtn(dialog);
	
				dialog.show();
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

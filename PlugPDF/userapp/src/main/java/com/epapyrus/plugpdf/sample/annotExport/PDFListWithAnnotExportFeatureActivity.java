/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.annotExport;

import java.io.File;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.epapyrus.plugpdf.PasswordDialog;
import com.epapyrus.plugpdf.core.PDFDocument;
import com.epapyrus.plugpdf.core.PlugPDFException.WrongPassword;
import com.epapyrus.plugpdf.sample.FileDialog;
import com.epapyrus.plugpdf.sample.FileDialog.FileDialogListener;
import com.epapyrus.plugpdf.sample.PDFListActivity;

/**
 * Shows the process of importing and exporting annotations.
 *  
 * @author ePapyrus
 */
public class PDFListWithAnnotExportFeatureActivity extends PDFListActivity {

	private PDFDocument doc;
	
	private Button exportBtn;
	private Button importBtn;
	private Button cancelBtn;
	
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
	 * Exports annotations to the given file
	 * @param filePath The PDF document's path and name
	 */
	public void exportAnnot(String filePath) {
		doc.exportAnnotToXFDF(filePath);
		doc.release();
	}

	/**
	 * Imports annotations from the given file
	 * @param filePath The PDF document's path and name
	 */
	public void importAnnot(String filePath) {
		doc.importAnnotFromXFDF(filePath);
		doc.release();
	}
	
	/**
	 * Creates an UI dialog in order to import/export annotations from/to a PDF document.
	 * @return dialog
	 */
	private Dialog createDialog() {
		
		Dialog dialog = new Dialog(this);
		dialog.setTitle("Annotation export/import");
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(20, 0, 20, 0);
		
		dialog.setContentView(layout);
		
		exportBtn = new Button(this);
		importBtn = new Button(this);
		cancelBtn = new Button(this);
		
		exportBtn.setText("Export annotation");
		importBtn.setText("Import annotation");
		cancelBtn.setText("Cancel");
		
		layout.addView(exportBtn);
		layout.addView(importBtn);
		layout.addView(cancelBtn);
		
		return dialog;
	}

	/**
	 * Sets the click listener to the import/export dialog buttons.
	 *
	 * @param dialog
	 */
	private void setListenerToBtn(final Dialog dialog) {
		
		exportBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FileDialog fileDialog = new FileDialog(PDFListWithAnnotExportFeatureActivity.this, new FileDialogListener() {
					
					@Override
					public void onChosenFile(String filePath) {
						
						exportAnnot(filePath);
						
					}
				});
				
				dialog.dismiss();
				fileDialog.show();
			}
		});
		
		importBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FileDialog fileDialog = new FileDialog(PDFListWithAnnotExportFeatureActivity.this, new FileDialogListener() {
					
					@Override
					public void onChosenFile(String filePath) {
							
						importAnnot(filePath);
						
					}
				});
				
				dialog.dismiss();
				fileDialog.show(".xfdf");
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		
			doc = new PDFDocument(fileName, password);
		
			if (doc != null) {
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

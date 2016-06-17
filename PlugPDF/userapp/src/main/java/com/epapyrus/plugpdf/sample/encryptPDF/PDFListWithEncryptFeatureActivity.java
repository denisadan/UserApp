/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample.encryptPDF;

import java.io.File;

import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epapyrus.plugpdf.PasswordDialog;
import com.epapyrus.plugpdf.core.PDFDocument;
import com.epapyrus.plugpdf.core.PlugPDFException.WrongPassword;
import com.epapyrus.plugpdf.core.PlugPDFUtility;
import com.epapyrus.plugpdf.sample.PDFListActivity;

/**
 * Implements the process of encrypting a PDF document.
 *  
 * @author ePapyrus
 */
public class PDFListWithEncryptFeatureActivity extends PDFListActivity {
	
	private PDFDocument doc;
	
	private EditText userPass;
	private EditText ownerPass;
	
	private CheckBox printCb;
	private CheckBox modifyContentCb;
	private CheckBox copyContentCb;
	private CheckBox modifyAnnotCb;
	private CheckBox fillFieldCb;
	private CheckBox extractCb;
	private CheckBox documentAssemblyCb;
	
	private Button encryptBtn;
	private Button cancelBtn;

	/* (non-Javadoc)
	 * See {@link PDFListActivity}
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id) {

		File targetPDF = new File(downloadDir, parent.getItemAtPosition(
				index).toString());
		
		openDocument(targetPDF.getAbsolutePath(), "");
		
	}

	/**
	 * Encrypts the given PDF document according to the information provided: user password,
	 * owner password, copy content, modify annotations, modify contents, print, fill field and extract.
	 *
	 * @param destPDF The path and the name of the PDF to be saved after the encryption
	 */
	public void encryptPDF(String destPDF) {
		
		// Get permission via the checkboxes
		int perm = PDFDocument.getUserAccessPermissions(
					printCb.isChecked(),
					modifyContentCb.isChecked(),
					copyContentCb.isChecked(),
					modifyAnnotCb.isChecked(),
					fillFieldCb.isChecked(),
					extractCb.isChecked(),
					documentAssemblyCb.isChecked());
		
		// Encrypt the document with the password and the permission provided
		doc.setEncrypt(userPass.getText().toString(),
				ownerPass.getText().toString(), perm);
		
		doc.saveAsFile(destPDF);
		doc.release();
	}
	
	/**
	 * Creates an UI dialog in order to encrypt a PDF document.
	 * @return dialog
	 */
	private Dialog createDialog() {
		
		Dialog dialog = new Dialog(this);
		dialog.setTitle("Encrypt PDF");
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(20, 0, 20, 0);
		
		dialog.setContentView(layout);
		
		TextView userPassLabel = new TextView(this);
		userPass = new EditText(this);
		
		TextView ownerPassLabel = new TextView(this);
		ownerPass = new EditText(this);
		
		printCb = new CheckBox(this);
		modifyContentCb = new CheckBox(this);
		copyContentCb = new CheckBox(this);
		modifyAnnotCb = new CheckBox(this);
		fillFieldCb = new CheckBox(this);
		extractCb = new CheckBox(this);
		documentAssemblyCb = new CheckBox(this);
		
		encryptBtn = new Button(this);
		cancelBtn = new Button(this);
		
		userPassLabel.setText("User Password: ");
		ownerPassLabel.setText("User Password: ");
		
		printCb.setText("Print");
		modifyContentCb.setText("Modify Content");
		copyContentCb.setText("Copy Content");
		modifyAnnotCb.setText("Modify Annot");
		fillFieldCb.setText("Fill Field");
		extractCb.setText("Extract");
		documentAssemblyCb.setText("Document Assembly");
		
		encryptBtn.setText("Apply");
		cancelBtn.setText("Cancel");
		
		userPass.setMinWidth((int) PlugPDFUtility.convertDipToPx(this, 150));
		ownerPass.setMinWidth((int) PlugPDFUtility.convertDipToPx(this, 150));
		
		LinearLayout userPassLayout = new LinearLayout(this);
		userPassLayout.addView(userPassLabel);
		userPassLayout.addView(userPass);
		
		LinearLayout ownerPassLayout = new LinearLayout(this);
		ownerPassLayout.addView(ownerPassLabel);
		ownerPassLayout.addView(ownerPass);
		
		LinearLayout btnLayout = new LinearLayout(this);
		btnLayout.setGravity(Gravity.RIGHT);
		btnLayout.addView(encryptBtn);
		btnLayout.addView(cancelBtn);
		
		layout.addView(userPassLayout);
		layout.addView(ownerPassLayout);
		layout.addView(printCb);
		layout.addView(modifyContentCb);
		layout.addView(copyContentCb);
		layout.addView(modifyAnnotCb);
		layout.addView(fillFieldCb);
		layout.addView(extractCb);
		layout.addView(documentAssemblyCb);
		layout.addView(btnLayout);
		
		return dialog;
	}
	
	/**
	 * Sets the click listener to the encryption dialog buttons.
	 *
	 * @param dialog Encryption dialog
	 * @param srcPDFPath The source PDF's path and name
	 */
	private void setListenerToBtn(final Dialog dialog, final String srcPDFPath) {
		
		encryptBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				encryptPDF(srcPDFPath.substring(0,
						srcPDFPath.length() - 4) + "-encrypt.pdf");
				
				refreshList();
				
				dialog.dismiss();
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

				setListenerToBtn(dialog, fileName);

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

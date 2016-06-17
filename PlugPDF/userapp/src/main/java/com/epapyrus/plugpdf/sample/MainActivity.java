/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.epapyrus.plugpdf.sample.annotExport.PDFListWithAnnotExportFeatureActivity;
import com.epapyrus.plugpdf.sample.encryptPDF.PDFListWithEncryptFeatureActivity;
import com.epapyrus.plugpdf.sample.extractText.PDFListWithExtractTextFeatureActivity;
import com.epapyrus.plugpdf.sample.mergePDF.PDFListWithMergeFeatureActivity;

/**
 * This is the PlugPDF Sample Project's Main {@link Activity}
 *
 * Creates the main view, and implements {@link OnClickListener}
 *  
 * @author ePapyrus
 * @see <a href="http://www.plugpdf.com">http://www.plugpdf.com</a>
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * Sets the activity content, creating the layout buttons and adding some click listeners
	 *
	 * @param savedInstanceState The activity's saved instance state.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button documentViewBtn = (Button) findViewById(R.id.document_view_btn);
		Button documentViewWithoutControllerBtn = (Button) findViewById(R.id.document_view_without_controller_btn);
		Button encryptPDFBtn = (Button) findViewById(R.id.encrypt_pdf_btn);
		Button mergePDFpathBtn = (Button) findViewById(R.id.merge_pdf_btn);
		Button annotExportBtn = (Button) findViewById(R.id.annot_export_btn);
		Button extractTextBtn = (Button) findViewById(R.id.extract_text_btn);
		
		documentViewBtn.setOnClickListener(this);
		documentViewWithoutControllerBtn.setOnClickListener(this);
		encryptPDFBtn.setOnClickListener(this);
		mergePDFpathBtn.setOnClickListener(this);
		annotExportBtn.setOnClickListener(this);
		extractTextBtn.setOnClickListener(this);
	}

	/**
	 * Starts an activity when the corresponding button is clicked.
	 *
	 * @param v The view that was clicked
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		Intent intent = null;
		
		switch (id) {
		case R.id.document_view_btn: // Shows the PDF with ReaderView
			intent = new Intent(this, PDFListActivity.class);
			intent.putExtra("TYPE", "READER");
			break;
		case R.id.document_view_without_controller_btn: // Shows the PDF with SimpleReaderView
			intent = new Intent(this, PDFListActivity.class);
			intent.putExtra("TYPE", "READER_WITHOUT_CONTROLLER");
			break;
		case R.id.encrypt_pdf_btn: // Encrypt PDF
			intent = new Intent(this, PDFListWithEncryptFeatureActivity.class);
			intent.putExtra("TYPE", "ENCRYPT_PDF");
			break;
		case R.id.merge_pdf_btn: // Merge PDF
			intent = new Intent(this, PDFListWithMergeFeatureActivity.class);
			intent.putExtra("TYPE", "MERGE_PDF");
			break;
		case R.id.annot_export_btn: // Annotation export/import
			intent = new Intent(this, PDFListWithAnnotExportFeatureActivity.class);
			intent.putExtra("TYPE", "ANNOT_EXPORT");
			break;
		case R.id.extract_text_btn: // Extract text
			intent = new Intent(this, PDFListWithExtractTextFeatureActivity.class);
			intent.putExtra("TYPE", "EXTRACT_TEXT");
			break;
		}
		
		startActivity(intent);
	}
}

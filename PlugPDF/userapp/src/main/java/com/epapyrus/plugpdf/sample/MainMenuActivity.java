package com.epapyrus.plugpdf.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.epapyrus.plugpdf.sample.mergePDF.PDFListWithMergeFeatureActivity;

public class MainMenuActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button documentViewBtn = (Button) findViewById(R.id.document_view_btn);
        Button mergePDFpathBtn = (Button) findViewById(R.id.merge_pdf_btn);
        documentViewBtn.setOnClickListener(this);
        mergePDFpathBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent intent = null;

        switch (id) {
            case R.id.document_view_btn: // Shows the PDF with ReaderView
                intent = new Intent(this, PDFListActivity.class);
                intent.putExtra("TYPE", "READER");
                break;

            case R.id.merge_pdf_btn: // Merge PDF
                intent = new Intent(this, PDFListWithMergeFeatureActivity.class);
                intent.putExtra("TYPE", "MERGE_PDF");
                break;
        }
        startActivity(intent);
    }
}

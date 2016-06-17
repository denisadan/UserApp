/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Shows the process of creating a file dialog.
 *
 * @author ePapyrus
 */
public class FileDialog {

	private Context mContext;
	private EditText fileNameView;
	private TextView curPathView;
	
	private String mFileName = ".txt";
	private String mCurDir = "";
	private String mRootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	
	private List<String> mFileList = null;
	private FileDialogListener mListener;
	private ArrayAdapter<String> mListAdapter = null;


	/**
	 * Interface of file dialog listener.
	 */
	public interface FileDialogListener {
		public void onChosenFile(String filePath);
	}

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param SimpleFileDialogListener
	 */
	public FileDialog(Context context,
			FileDialogListener SimpleFileDialogListener) {

		mContext = context;
		mListener = SimpleFileDialogListener;

	}

	/**
	 * Creates a file dialog, displaying the current files and directories.
	 *
	 * @return File dialog layout
	 */
	private ViewGroup createDialogLayout() {
		LinearLayout dialogLayout = new LinearLayout(mContext);

		fileNameView = new EditText(mContext);
		fileNameView.setText(mFileName);
		
		curPathView = new TextView(mContext);
		curPathView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		curPathView.setBackgroundColor(0xFFBBBB);
		curPathView.setTextColor(Color.WHITE);
		curPathView.setText(mCurDir);

		dialogLayout.setOrientation(LinearLayout.VERTICAL);
		dialogLayout.addView(curPathView);
		dialogLayout.addView(fileNameView);
		
		return dialogLayout;
	}

	/**
	 * Creates the file dialog's title.
	 *
	 * @return titleView
	 */
	private View createTitleView() {
		
		TextView titleView = new TextView(mContext);

		titleView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		titleView.setText("Open");

		titleView.setGravity(Gravity.CENTER_VERTICAL);
		titleView.setBackgroundColor(0xffbbbbbb);
		titleView.setTextColor(Color.WHITE);
		
		return titleView;
	}

	/**
	 * Creates and displays the dialog.
	 */
	public void show() {
		show(mRootPath, mFileName);
	}
	
	/**
	 * Creates and displays the dialog.
	 *
	 * @param defaultFileName Default file name
	 */
	public void show(String defaultFileName) {
		show(mRootPath, defaultFileName);
	}
	
	/**
	 * Creates and displays the dialog.
	 *
	 * @param rootPath  The external storage directory's absolute path
	 * @param defaultFileName File name.
	 */
	public void show(String rootPath, String defaultFileName) {

		mRootPath = rootPath;
		mCurDir = rootPath;
		mFileList = getFiles(new File(rootPath));

		AlertDialog.Builder dialogBuilder = createDialogBuilder(mFileList);

		final AlertDialog dirsDialog = dialogBuilder.create();
		
		dirsDialog.show();
	}

	/**
	 * Given a directory, creates a list of files.
	 *
	 * @param dir Directory
	 * @return List of files
	 */
	private List<String> getFiles(File dir) {
		List<String> dirs = new ArrayList<String>();
		
		if (!mCurDir.equals(mRootPath))
			dirs.add("..");

		if (!dir.exists() || !dir.isDirectory()) {
			return dirs;
		}

		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				dirs.add(file.getName() + "/");
			else
				dirs.add(file.getName());
		}
		
		Collections.sort(dirs, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.endsWith("/") ? 
						o2.endsWith("/") ? o1.compareTo(o2)
								: -1
						: !o2.endsWith("/") ? o1.compareTo(o2)
								: 1;
			}
		});
		return dirs;
	}

	/**
	 * Creates an AlertDialog.Builder by adding a title layout, a files layout, as well as
	 * these buttons: ok and cancel.
	 *
	 * @param listItems List of files
	 * @return The AlertDialog.Builder
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/AlertDialog.Builder.html">android.app.AlertDialog.Builder</a>
	 */
	private AlertDialog.Builder createDialogBuilder(List<String> listItems) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

		ViewGroup fileDialogLayout = createDialogLayout();
		View titleView = createTitleView();
		
		dialogBuilder.setCustomTitle(titleView);
		dialogBuilder.setView(fileDialogLayout);
		
		mListAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.select_dialog_item, android.R.id.text1, listItems);
		dialogBuilder.setSingleChoiceItems(mListAdapter, -1, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String selectedFile = mListAdapter.getItem(which);
				
				if (selectedFile.equals("..")) {
					
					mCurDir = mCurDir.substring(0, mCurDir.lastIndexOf("/") - 1);
					
				} else if ((new File(mCurDir, selectedFile).isFile())) {
					
					fileNameView.setText(selectedFile);
					
				} else {
					
					mCurDir += "/" + selectedFile;
					
				}

				updateDirectory();
			}
		});
		dialogBuilder.setCancelable(false);
		
		dialogBuilder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mListener != null) {
					mListener.onChosenFile(mCurDir
							+ fileNameView.getText().toString());
				}
			}
		}).setNegativeButton("Cancel", null);
		
		return dialogBuilder;
	}

	/**
	 * Updates the files directory by first clearing the file list, and then adding the current
	 * path's files to the list.
	 */
	private void updateDirectory() {
		mFileList.clear();
		mFileList.addAll(getFiles(new File(mCurDir)));
		curPathView.setText(mCurDir);
		mListAdapter.notifyDataSetChanged();
	}
}
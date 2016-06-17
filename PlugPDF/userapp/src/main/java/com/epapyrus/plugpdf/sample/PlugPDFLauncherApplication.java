/*
 * Copyright (C) 2013 ePapyrus, Inc. All rights reserved.
 *
 * This file is part of the PlugPDF Sample Project whose source code is provided to show how to
 * use the PlugPDF SDK.
 */

package com.epapyrus.plugpdf.sample;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.epapyrus.plugpdf.core.PlugPDF;
import com.epapyrus.plugpdf.core.PlugPDFException.InvalidLicense;
import com.epapyrus.plugpdf.core.PlugPDFException.LicenseMismatchAppID;
import com.epapyrus.plugpdf.core.PlugPDFException.LicenseTrialTimeOut;
import com.epapyrus.plugpdf.core.PlugPDFException.LicenseUnusableOS;
import com.epapyrus.plugpdf.core.PlugPDFException.LicenseWrongProductVersion;

/**
 * Initializes the PlugPDF SDK.
 * 
 * A valid license is required in order to bring your Android PDF app to life with PlugPDF.
 *
 * @see <a target="_blank" href="https://plugpdf.com/download/">Free Trial</a>
 * @see <a target="_blank" href="https://plugpdf.com/indie-license-req/">Indie License</a>
 * @see <a target="_blank" href="http://plugpdf.com/order/">Or click here to order now!</a>
 * @author ePapyrus
 */
public class PlugPDFLauncherApplication extends Application {
	
	/**
	 * Initializes and validates the PlugPDF SDK
	 * 
	 * @throws Invalid License exception
	 * @throws License Wrong Product Version exception
	 * @throws Trial License is over
	 * @throws Unusable OS license exception
	 * @throws License Mismatch APP ID exception
	 * 
	 * @see <a target="_blank" href="http://developer.android.com/reference/android/app/Application.html#onCreate%28%29">android.app.Application.onCreate()</a>
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		String exceptionMsg = "";
		try {
			Log.d("Version", PlugPDF.getVersionName());
			PlugPDF.init(getApplicationContext(),
					"C9B85CC8HGE9GH82ED34H98F7DDAH9729BCDAACDAF7888HE6786FBF5");
			// PlugPDF.deployAssetFontResource(getApplicationContext());
			PlugPDF.enableUncaughtExceptionHandler();
			PlugPDF.setUpdateCheckEnabled(false);
			// PlugPDF.setBitmapConfig(Bitmap.Config.RGB_565);
		} catch (LicenseWrongProductVersion ex) {
			exceptionMsg = "This license key is not valid for this version of the PlugPDF SDK.";
		} catch (LicenseTrialTimeOut ex) {
			exceptionMsg = "Your trial period has expired.";
		} catch (LicenseUnusableOS ex) {
			exceptionMsg = "This license key is not valid for the Android platform.";
		} catch (LicenseMismatchAppID ex) {
			exceptionMsg = "This license key does not match the App ID.";
		} catch (InvalidLicense ex) {
			exceptionMsg = "This license key is not valid.";
		} catch (Exception ex) {
			if (ex.getMessage() == null) {
				exceptionMsg = "An unknown error occurred.";
			} else {
				exceptionMsg = ex.getMessage();
			}
		}
		if (exceptionMsg.length() > 0) {
			Toast.makeText(getApplicationContext(), exceptionMsg, Toast.LENGTH_LONG).show();
			Log.e("Exception", exceptionMsg);
		}
	}
}

package com.epapyrus.plugpdf.sample;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;

public class SwipeMacro {
	
	private static final int DELAY = 100;
	
	public static void doSwipe(float startX, float startY, float endX, float endY) {
		doTouch(startX, startY);
		doDrag(startX, startY, endX, endY);
		doFallOff(endX, endY);
	}
	
	public static void doTouch(float x, float y) {
		
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis() + DELAY;
		int metaState = 0;
		MotionEvent motionEvent = MotionEvent.obtain(
		    downTime, 
		    eventTime, 
		    MotionEvent.ACTION_DOWN, 
		    x, 
		    y, 
		    metaState
		);
		Instrumentation instrumentation = new Instrumentation();
		instrumentation.sendPointerSync(motionEvent);
	}
	
	public static void doDrag(float startX, float startY, float endX, float endY) {
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis() + DELAY;
		int metaState = 0;
		MotionEvent motionEvent = MotionEvent.obtain(
		    downTime, 
		    eventTime, 
		    MotionEvent.ACTION_MOVE, 
		    startX + ((endX - startX) / 2), 
		    startY + ((endY - startY) / 2), 
		    metaState
		);
		Instrumentation instrumentation = new Instrumentation();
		instrumentation.sendPointerSync(motionEvent);
	}
	
	public static void doFallOff(float x, float y) {
		
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis() + DELAY;
		int metaState = 0;
		MotionEvent motionEvent = MotionEvent.obtain(
		    downTime, 
		    eventTime, 
		    MotionEvent.ACTION_UP, 
		    x, 
		    y, 
		    metaState
		);
		Instrumentation instrumentation = new Instrumentation();
		instrumentation.sendPointerSync(motionEvent);
	}
}

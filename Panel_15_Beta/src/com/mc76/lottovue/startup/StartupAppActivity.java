package com.mc76.lottovue.startup;

import org.panel.R;
import org.panel.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class StartupAppActivity extends Activity {

	private static final int PROGRESS = 0x1;
	private ProgressBar mProgress;
	private int mProgressStatus = 0;
	private Handler progressBarHandler = new Handler();
	private Context cxt = null;
	
	private long fileSize = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_main);
		this.setTitle("LottoVue version 1.0.0, May 1, 2013");
		cxt = this.getBaseContext();
		
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		// addListenerOnButton();

		// Start lengthy operation in a background thread
		new Thread(new Runnable() {
			public void run() {
				while (mProgressStatus < 100) {
					mProgressStatus = doSomeTasks();

					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(mProgressStatus);
							Log.d("Status:", String.valueOf(mProgressStatus));							
						}
					});
				}
				if (mProgressStatus==100) {
					Log.d("Status completed:", String.valueOf(mProgressStatus));		
					//start another activity 

					//startActivity(new Intent("com.mkyong.android.SecondActivity"));
					startActivity(new Intent(cxt, Test.class));
					mProgress.setVisibility(ProgressBar.GONE);
				}
			}
		}).start();
	}

	// file download simulator... a really simple
	public int doSomeTasks() {

		while (fileSize <= 100000) {

			fileSize++;

			if (fileSize == 10000) {
				return 10;
			} else if (fileSize == 20000) {
				return 20;
			} else if (fileSize == 30000) {
				return 30;
			}
			// ...add your own

		}

		return 100;

	}

}
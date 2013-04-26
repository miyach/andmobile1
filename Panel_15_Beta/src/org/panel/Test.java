package org.panel;

import org.panel.Panel.OnPanelListener;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import easing.interpolator.BackInterpolator;
import easing.interpolator.BounceInterpolator;
import easing.interpolator.ElasticInterpolator;
import easing.interpolator.ExpoInterpolator;
import easing.interpolator.EasingType.Type;

public class Test extends Activity implements OnPanelListener {

	private Panel bottomPanel;
	private Panel topPanel;
	/** Called when the activity is first created. */
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	    ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Panel panel;
        
        topPanel = panel = (Panel) findViewById(R.id.topPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BounceInterpolator(Type.OUT));
        
        panel = (Panel) findViewById(R.id.leftPanel1);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(Type.OUT, 2));

        panel = (Panel) findViewById(R.id.leftPanel2);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new BackInterpolator(Type.OUT, 2));

//        panel = (Panel) findViewById(R.id.rightPanel);
//        panel.setOnPanelListener(this);
//        panel.setInterpolator(new ExpoInterpolator(Type.OUT));

        bottomPanel = panel = (Panel) findViewById(R.id.bottomPanel);
        panel.setOnPanelListener(this);
        panel.setInterpolator(new ElasticInterpolator(Type.OUT, 1.0f, 0.3f));
        
//        findViewById(R.id.smoothButton1).setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				bottomPanel.setOpen(!bottomPanel.isOpen(), true);
//			}
//        });
//        findViewById(R.id.smoothButton2).setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				topPanel.setOpen(!topPanel.isOpen(), false);
//			}
//        });
    }

	public void onPanelClosed(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("Test", "Panel [" + panelName + "] closed");
	}
	public void onPanelOpened(Panel panel) {
		String panelName = getResources().getResourceEntryName(panel.getId());
		Log.d("Test", "Panel [" + panelName + "] opened");
	}
	
	public void onClickCaptureNumberByEnter(View e){
		//start another itente
		Intent i = new 
    			Intent("com.mc76.slidedrawer.CaptureEnterNumberActivity");
		
    	//---use putExtra() to add new key/value pairs---            
    	i.putExtra("str1", "This is a string");
    	i.putExtra("age1", 25);
    	//---use a Bundle object to add new key/values 
    	// pairs---  
    	Bundle extras = new Bundle();
    	extras.putString("str2", "This is another string");
    	extras.putInt("age2", 35);                
    	//---attach the Bundle object to the Intent object---
    	i.putExtras(extras);                
    	//---start the activity to get a result back---
    	//startActivityForResult(i, 1);
    	startActivity(i);
	}
}

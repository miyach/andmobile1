package com.mc76.lottovue;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//disabled rotation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		
	    ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void onClickLotto649(View v) {
        Toast.makeText(this, "Clicked Lotto 649", Toast.LENGTH_SHORT).show();	            			
        
//		Intent i = new Intent("net.learn2develop.PassingDataSecondActivity");
//    	//---use putExtra() to add new key/value pairs---            
//    	i.putExtra("str1", "This is a string");
//    	i.putExtra("age1", 25);
//    	//---use a Bundle object to add new key/values 
//    	Bundle extras = new Bundle();
//    	extras.putString("str2", "This is another string");
//    	extras.putInt("age2", 35);                
//    	//---attach the Bundle object to the Intent object---
//    	i.putExtras(extras);                
//    	//---start the activity to get a result back---
//    	//startActivityForResult(i, 1);
		
	}
	
	public void onClickLottoMax(View v) {
        Toast.makeText(this, "Clicked Lotto Max", Toast.LENGTH_SHORT).show();	            			
	}

	@Override     
	 public void onConfigurationChanged(Configuration newConfig) {       
	        try {     
	            super.onConfigurationChanged(newConfig);      
	            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {      
	                // land   
	                Toast.makeText(this, "Lanscape", Toast.LENGTH_SHORT).show();	            	
	            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {      
	               // port       
	                Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();	            		            	
	            }    
	        } catch (Exception ex) {       
	     }   
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
        CreateMenu(menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {    
         return MenuChoice(item);    
    }    
    
    private void CreateMenu(Menu menu)
    {
        MenuItem mnu1 = menu.add(0, 0, 0, "Lotto-649");
        {         
            mnu1.setIcon(R.drawable.ic_launcher);
            mnu1.setShowAsAction(
            	MenuItem.SHOW_AS_ACTION_IF_ROOM |
                MenuItem.SHOW_AS_ACTION_WITH_TEXT);            
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Lotto-Max");
        {         
            mnu2.setIcon(R.drawable.ic_launcher);            
            mnu2.setShowAsAction(
            	MenuItem.SHOW_AS_ACTION_IF_ROOM |
                MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

    }
 
    private boolean MenuChoice(MenuItem item)
    {        
        switch (item.getItemId()) {
        case  android.R.id.home:
            Toast.makeText(this, 
                "You clicked on the Application icon", 
                Toast.LENGTH_LONG).show();

            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        case 0:
            Toast.makeText(this, "You clicked on Lotto 6/49", 
                Toast.LENGTH_SHORT).show();
            return true;
        case 1:
            Toast.makeText(this, "You clicked on Lotto Max", 
                Toast.LENGTH_SHORT).show();
            return true;
        
        }
        return false;
    }    

}

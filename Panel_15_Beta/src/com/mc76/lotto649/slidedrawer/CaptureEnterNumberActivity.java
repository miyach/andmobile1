package com.mc76.lotto649.slidedrawer;

import java.util.ArrayList;

import org.panel.R;
import org.panel.Test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CaptureEnterNumberActivity extends Activity {
	Context ctx = null;
	AlertDialog.Builder builder = null;
	public GridView gridView;
	public ArrayList<String> selectedNumbers = new ArrayList();

	static final String[] numbers = new String[] { "01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37",
			"38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
			"49" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotto649_capture_number);

		ctx =  this.getBaseContext();
		
		builder = new AlertDialog.Builder(this);
		gridView = (GridView) findViewById(R.id.gridView1);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, numbers);

		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView tv = ((TextView) v);
				String selectedvalue = (String) tv.getText();
				Toast.makeText(getApplicationContext(), selectedvalue,
						Toast.LENGTH_SHORT).show();
				if (selectedvalue != null
						&& selectedNumbers.contains(selectedvalue)) {
					// change background color back to default color
					tv.setBackgroundColor(Color.TRANSPARENT);
					selectedNumbers.remove(selectedvalue);
				} else {

				if (selectedNumbers != null && selectedNumbers.size() == 5) {
					//save last selected numbers
					selectedNumbers.add(selectedvalue);
					tv.setBackgroundColor(Color.YELLOW);
					
					// 1. Instantiate an AlertDialog.Builder with its
					// constructor
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								// Yes button clicked								
								dialog.dismiss();
								Intent i = new Intent(ctx,Test.class);								
						    	//---use putExtra() to add new key/value pairs---            
						    	i.putExtra("lotto", "649");
								i.putExtra("selectedNumbers", selectedNumbers.toString());
								startActivity(i);
								break;
							case DialogInterface.BUTTON_NEGATIVE:
								// No button clicked
								dialog.dismiss();
								break;
							}
						}
					};

					// AlertDialog.Builder builder = new
					// AlertDialog.Builder(this);
					String title = "";
					for (int i = 0; i < selectedNumbers.size(); i++) {
						if (i == 5) {
							title = title + (String) selectedNumbers.get(i);
						} else {
							title = title + (String) selectedNumbers.get(i)
									+ ", ";
						}
					}
					builder.setTitle(title);
					builder.setMessage("Save selected numbers?")
							.setPositiveButton("Yes", dialogClickListener)
							.setNegativeButton("No", dialogClickListener)
							.show();

				} else if (selectedNumbers != null && selectedNumbers.size() < 5) {	
					selectedNumbers.add(selectedvalue);
					tv.setBackgroundColor(Color.YELLOW);
				} else if (selectedNumbers != null && selectedNumbers.size() > 5) {	
					tv.setBackgroundColor(Color.TRANSPARENT);					
					Toast.makeText(getApplicationContext(), "You have selected maximum of 6 numbers!", Toast.LENGTH_LONG).show();
				}
				
				}
			}
		});

		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				TextView tv = ((TextView) v);
				String selectedvalue = (String) tv.getText();

				Toast.makeText(getApplicationContext(),
						"Selected:" + selectedvalue, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getBaseContext(), "Nothing Selected",
						Toast.LENGTH_SHORT);
			}
		});
	}

}

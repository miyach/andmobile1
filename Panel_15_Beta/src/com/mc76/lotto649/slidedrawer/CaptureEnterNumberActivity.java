package com.mc76.lotto649.slidedrawer;

import java.util.ArrayList;

import org.panel.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
				}

				if (selectedNumbers != null && selectedNumbers.size() == 5) {
					Toast.makeText(getApplicationContext(),
							"You have reached maximum of 6 numbers!",
							Toast.LENGTH_LONG).show();
					// 1. Instantiate an AlertDialog.Builder with its
					// constructor

					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								// Yes button clicked
								dialog.dismiss();
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
						if (i == selectedNumbers.size()) {
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

				} else {
					selectedNumbers.add(selectedvalue);
					tv.setBackgroundColor(Color.YELLOW);
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

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		CreateMenu(menu);
//		return true;
//	}
//
//	private void CreateMenu(Menu menu) {
//		MenuItem mnu1 = menu.add(0, 0, 0, "Item 1");
//		{
//			mnu1.setAlphabeticShortcut('a');
//			mnu1.setIcon(R.drawable.ic_launcher);
//		}
//		MenuItem mnu2 = menu.add(0, 1, 1, "Item 2");
//		{
//			mnu2.setAlphabeticShortcut('b');
//			mnu2.setIcon(R.drawable.ic_launcher);
//		}
//		MenuItem mnu3 = menu.add(0, 2, 2, "Item 3");
//		{
//			mnu3.setAlphabeticShortcut('c');
//			mnu3.setIcon(R.drawable.ic_launcher);
//		}
//		MenuItem mnu4 = menu.add(0, 3, 3, "Item 4");
//		{
//			mnu4.setAlphabeticShortcut('d');
//		}
//		menu.add(0, 4, 4, "Item 5");
//		menu.add(0, 5, 5, "Item 6");
//		menu.add(0, 6, 6, "Item 7");
//	}
//
//	private boolean MenuChoice(MenuItem item) {
//		switch (item.getItemId()) {
//		case 0:
//			Toast.makeText(this, "You clicked on Item 1", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 1:
//			Toast.makeText(this, "You clicked on Item 2", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 2:
//			Toast.makeText(this, "You clicked on Item 3", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 3:
//			Toast.makeText(this, "You clicked on Item 4", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 4:
//			Toast.makeText(this, "You clicked on Item 5", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 5:
//			Toast.makeText(this, "You clicked on Item 6", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		case 6:
//			Toast.makeText(this, "You clicked on Item 7", Toast.LENGTH_LONG)
//					.show();
//			return true;
//		}
//		return false;
//	}
}

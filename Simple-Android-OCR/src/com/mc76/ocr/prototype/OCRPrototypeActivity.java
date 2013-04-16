package com.mc76.ocr.prototype;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class OCRPrototypeActivity extends Activity {
	public static final String PACKAGE_NAME = "com.mc76.ocr.prototype";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/OCRPrototype1/";

	// You should have the trained data file in assets folder
	// You can get them at:
	// http://code.google.com/p/tesseract-ocr/downloads/list
	public static final String lang = "eng";

	private static final String TAG = "OCRPrototypeActivity.java";

	protected Button _button;
	protected ImageView _image;
	protected EditText _field;
	protected String _path;
	protected boolean _taken;

	protected static final String PHOTO_TAKEN = "photo_taken";

	// processbar settings
	static int progress;
	ProgressBar progressBar;
	int progressStatus = 0;
	Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {

//		OCRHelper.preLoadTrainingDictionary(getAssets(), getBaseContext(),
//				DATA_PATH, lang, TAG);

		FileLoadingHelper helper = new FileLoadingHelper(this,DATA_PATH,lang);
		helper.unzipTrainingFile();
		
		super.onCreate(savedInstanceState);

		//progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		//progressBar.setVisibility(View.GONE);
		
		setContentView(R.layout.main);

		_image = (ImageView) findViewById(R.id.image);
		_field = (EditText) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.button);
		_button.setOnClickListener(new ButtonClickHandler());

		_path = DATA_PATH + "/ocr.jpg";
	}

	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.v(TAG, "Starting Camera app");
			_field.setText("");

			Toast.makeText(getBaseContext(), "Starting Camera app",
					Toast.LENGTH_SHORT).show();
			startCameraActivity();
		}
	}

	// Simple android photo capture:
	// http://labs.makemachine.net/2010/03/simple-android-photo-capture/

	protected void startCameraActivity() {
		File file = new File(_path);
		Uri outputFileUri = Uri.fromFile(file);

		if (OCRHelper.isIntentAvailable(this.getBaseContext(),
				MediaStore.ACTION_IMAGE_CAPTURE)) {
			final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			startActivityForResult(intent, 0);
			Log.v(TAG, "found camera, start capturing image");
		} else {
			Log.v(TAG, "camera not found!");
			Toast.makeText(getBaseContext(), "Camera not found in the phone!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);

		if (resultCode == -1) {
			Log.v(TAG, "preview photo");
			// Bundle extras = data.getExtras();
			// Bitmap mImageBitmap = (Bitmap) extras.get("data");
			// _image.setImageBitmap(mImageBitmap);
			// process photo
			Toast.makeText(getBaseContext(), "preview photo",
					Toast.LENGTH_SHORT).show();

			progress = 0;
			progressBar = (ProgressBar) findViewById(R.id.progress_bar);
			//progressBar.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {
				public void run() {
					// ---do some work here---
					while (progressStatus < 10) {
						progressStatus = doSomeWork();
					}
					handler.post(new Runnable() {
						public void run() {
							// ---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
							progressBar.setVisibility(View.GONE);
						}
					});
				}

				private int doSomeWork() {
					try {
						// ---simulate doing some work---
						// Thread.sleep(500);
						onPhotoTaken();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ++progress;
				}
			}).start();

		} else {
			Log.v(TAG, "User cancelled");
			Toast.makeText(getBaseContext(), "User cancelled",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(OCRPrototypeActivity.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(OCRPrototypeActivity.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken() {
		_taken = true;

		Bitmap bitmap = OCRHelper.convertToBitmap(getBaseContext(), _path,
				TAG);
		// show the image
		_image.setImageBitmap(bitmap);

		String recognizedText = OCRHelper.OCRFile(getBaseContext(), _path,
				lang, bitmap, TAG);
		if (recognizedText.length() != 0) {
			_field.setText(_field.getText().toString().length() == 0 ? recognizedText
					: _field.getText() + " " + recognizedText);
			_field.setSelection(_field.getText().toString().length());
			// display boxes
			// _image.setImageBitmap(bitmap);
		}
		// Cycle done.
	}

}
package com.mc76.lotto.ocr;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ShootAndCropActivity extends Activity {

	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/ShootAndCrop/";

	// keep track of cropping intent
	final int PIC_CROP = 2;
	// keep track of camera capture intent
	final int CAMERA_CAPTURE = 1;
	final int PICK_FROM_GALLERY = 3;
	// captured picture uri
	private Uri picUri;	
	private ImageView picView;

	String _path = DATA_PATH + "/crop.jpg";
	private static final String TAG = "ShootAndCropActivity.java";

	protected Button _button;
	protected EditText _field;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	public static final String lang = "lot";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_field = (EditText) findViewById(R.id.field);
		_button = (Button) findViewById(R.id.capture_btn);
		_path = DATA_PATH + "/ocr.jpg";
		
		// retrieve a reference to Image View
		picView = (ImageView) findViewById(R.id.picture);				
		// retrieve a reference to the UI button		
		Button buttonGallery = (Button) findViewById(R.id.btn_select_gallery);
		 buttonGallery.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
			 // TODO Auto-generated method stub
			 Intent intent = new Intent();
			 // call android default gallery
			 intent.setType("image/*");
			 intent.setAction(Intent.ACTION_GET_CONTENT);
			 try {
				 intent.putExtra("return-data", true);
				 startActivityForResult(Intent.createChooser(intent,"Complete action using"), PICK_FROM_GALLERY);
			 } catch (ActivityNotFoundException e) {
					String errorMessage = "Whoops - your device doesn't support Image Gallery!";
					showMessage(errorMessage);
					Log.v(TAG, errorMessage);
			 }
		   }
	    });
				
		Button captureBtn = (Button) findViewById(R.id.capture_btn);
		// handle button clicks
		//captureBtn.setOnClickListener((OnClickListener) this);
		captureBtn.setOnClickListener( new View.OnClickListener() {

			 public void onClick(View v) {
				if (!isIntentAvailable(getBaseContext(),
							MediaStore.ACTION_IMAGE_CAPTURE)) {
						String errorMessage = "Whoops - your device doesn't support capturing images!";
						showMessage(errorMessage);
				} else {
					try {
							File file = new File(_path);
							picUri = Uri.fromFile(file);
							Intent takePictureIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
							takePictureIntent.putExtra("return-data", true);
							startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
							Log.v(TAG, "found camera, start capturing image");
						} catch (ActivityNotFoundException anfe) {
							// display an error message
							Log.v(TAG, "camera not found!");							
							String errorMessage =  "Camera not found in this phone!";
							showMessage(errorMessage);
							Log.v(TAG, errorMessage);
						}
					}
		  }
		});

		//initilized OCR training data
		FileLoadingHelper helper = new FileLoadingHelper(this,DATA_PATH,lang,_field);
		helper.unzipTrainingFile();
		
//		String[] paths = new String[] { DATA_PATH };
//		try {
//			for (String path : paths) {
//				File dir = new File(path);
//				if (!dir.exists()) {
//					if (!dir.mkdirs()) {
//						Log.v(TAG, "ERROR: Creation of directory " + path
//								+ " on sdcard failed");
//						showMessage("ERROR: Creation of directory " + path
//								+ " on sdcard failed");
//						return;
//					} else {
//						Log.v(TAG, "Created directory " + path + " on sdcard");
//						showMessage("Created directory " + path + " on sdcard");
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.v(TAG, "Error:" + e.getMessage());
//			showMessage("Error:" + e.getMessage());
//		}
	}

//	public void onClick(View v) {
//		if (v.getId() == R.id.capture_btn) {
//			if (!isIntentAvailable(this.getBaseContext(),
//					MediaStore.ACTION_IMAGE_CAPTURE)) {
//				String errorMessage = "Whoops - your device doesn't support capturing images!";
//				showMessage(errorMessage);
//			} else {
//				try {
//					File file = new File(_path);
//					picUri = Uri.fromFile(file);
//					Intent takePictureIntent = new Intent(
//							MediaStore.ACTION_IMAGE_CAPTURE);
//					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//					takePictureIntent.putExtra("return-data", true);
//					startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
//				} catch (ActivityNotFoundException anfe) {
//					// display an error message
//					String errorMessage = "Whoops - your device doesn't support capturing images!";
//					showMessage(errorMessage);
//					Log.v(TAG, errorMessage);
//				}
//			}
//		}
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		try {
			
			if (resultCode == RESULT_OK && requestCode == PICK_FROM_GALLERY) {			
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					Bitmap photo = (Bitmap) extras2.get("data");
					picView.setImageBitmap(photo);
				}
			}
			
			if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE) {
				// user is returning from capturing an image using the camera
				showMessage("returning from camera capture");
				if (data != null) {
					picUri = data.getData();
				} else {
					showMessage("No data return from camera Capture");
				}
				if (picUri == null) {
					// do it the "Samsung" way
					showMessage("Samsung Way!");
					final ContentResolver cr = getContentResolver();
					final String[] p1 = new String[] {
							MediaStore.Images.ImageColumns._ID,
							MediaStore.Images.ImageColumns.DATE_TAKEN };
					Cursor c1 = cr.query(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, p1,
							null, null, p1[1] + " DESC");
					if (c1.moveToFirst()) {
						String uristringpic = "content://media/external/images/media/"
								+ c1.getInt(0);
						picUri = Uri.parse(uristringpic);
						showMessage("picture url:" + picUri.getPath());
						Log.i("TAG", "newuri   " + picUri);
					}
					c1.close();
				}
				// carry out the crop operation
				performCrop();				
			}

			if (resultCode == RESULT_OK && requestCode == PIC_CROP) {
				// user is returning from cropping the image
				// get the returned data
				Bitmap photo = null;
				if (data != null) {
					// get the cropped bitmap
					Bundle extras = data.getExtras();
					if (extras != null) {
						// Bitmap thePic = extras.getParcelable("data");
						// display the returned cropped image
						photo = (Bitmap) extras.get("data");
						if (photo != null) {
							picView.setImageBitmap(photo);
							
							//process photo 
							onPhotoTaken();
						}
					}
				} else {
					showMessage("No data return from Crop activity results");
					if (this.picUri != null) {
						showMessage("Decoded Photo File");
						setPicture(picView, _path);
						Log.d(TAG, "Decoded Photo File");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Error:" + e.getMessage());
			showMessage("Error:" + e.getMessage());
		}
	}


	protected void onPhotoTaken() {
		_taken = true;

		Bitmap bitmap = OCRHelper.convertToBitmap(getBaseContext(), _path,TAG);
		// show the image
		//picView.setImageBitmap(bitmap);
		_field.setText("OCRing, please wait!");
		String recognizedText = OCRHelper.OCRFile(getBaseContext(), DATA_PATH,
				lang, bitmap, TAG);
		if (recognizedText.length() != 0) {			
			_field.setText("");			
			_field.setText(_field.getText().toString().length() == 0 ? recognizedText
					: _field.getText() + " " + recognizedText);
			_field.setSelection(_field.getText().toString().length());
			// display boxes??
			// _image.setImageBitmap(bitmap);
		}
		if (recognizedText.length() == 0)  {
			_field.setText("empty text...please try again.");						
		}
	}
	
	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			showMessage(errorMessage);
		}
	}

	private void setPicture(ImageView mImageView, String mCurrentPhotoPath) {
		// Get the dimensions of the View
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public void showMessage(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(ShootAndCropActivity.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(ShootAndCropActivity.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

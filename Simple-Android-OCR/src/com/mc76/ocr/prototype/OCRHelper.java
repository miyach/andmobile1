package com.mc76.ocr.prototype;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.Toast;

public class OCRHelper {

	public static void preLoadTrainingDictionary(AssetManager assetManager, Context context, String filePath, String lang, String TAG) {
		// http://code.google.com/p/tesseract-ocr/downloads/list
		String[] paths = new String[] { filePath, filePath + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path
							+ " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}

		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(filePath + "tessdata/" + lang + ".traineddata"))
				.exists()) {
			try {

				//AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");
				OutputStream out = new FileOutputStream(filePath
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();

				Log.v(TAG, "Copied " + lang + " traineddata");
				Toast.makeText(context,
						"Copied " + lang + " traineddata", Toast.LENGTH_SHORT)
						.show();
			} catch (IOException e) {
				Log.e(TAG,
						"Was unable to copy " + lang + " traineddata "
								+ e.toString());
				Toast.makeText(
						context,
						"Was unable to copy " + lang + " traineddata "
								+ e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}	
	
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	@SuppressLint("NewApi")
	public static Bitmap convertToBitmap(Context context, String filepath,String TAG) {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		//options.inSampleSize = 4;
		options.inSampleSize = 2;
		//options.inPreferQualityOverSpeed = true;
		//options.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		Toast.makeText(context, "Decode file", Toast.LENGTH_SHORT)
				.show();
		try {
			ExifInterface exif = new ExifInterface(filepath);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			Toast.makeText(context, " Convert to ARGB_8888",
					Toast.LENGTH_SHORT).show();
			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			 e.printStackTrace();
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
			Toast.makeText(context, "error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		
		return bitmap;
	}
	
	public static String OCRFile(Context context,String scanfile, String lang, Bitmap bitmap,String TAG) {
		String recognizedText = "";
		
		Log.v(TAG, "Before baseApi");
		Toast.makeText(context, "Before baseApi", Toast.LENGTH_SHORT).show();
		try {
			TessBaseAPI baseApi = new TessBaseAPI();
			baseApi.setDebug(true);
			baseApi.init(scanfile, lang);
			baseApi.setImage(bitmap);
			recognizedText = baseApi.getUTF8Text();
			baseApi.end();

			// You now have the text in recognizedText var, you can do anything
			// with it.
			// We will display a stripped out trimmed alpha-numeric version of
			// it (if lang is eng)
			// so that garbage doesn't make it to the display.

			Log.v(TAG, "OCRED TEXT: " + recognizedText);
			//Toast.makeText(getBaseContext(), "OCRED TEXT: " + recognizedText,Toast.LENGTH_SHORT).show();
			if (lang.equalsIgnoreCase("eng")) {
				recognizedText = recognizedText
						.replaceAll("[^a-zA-Z0-9]+", " ");
			}
			recognizedText = recognizedText.trim();
		} catch (Exception e) {
			 e.printStackTrace();
			Toast.makeText(context, "error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
		} 	
		return recognizedText;
	}
}

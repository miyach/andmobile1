package com.mc76.lotto.ocr;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

public class FileLoadingHelper {
	String TAG = "FileLoadingHelper.java";
	Activity activity = null;
	String DATA_PATH = null;
	String TESS_DATA = "tessdata";
	String TESS_ZIP_ENG = "eng.zip";
	String TESS_ZIP_LOT = "lot.zip";	
	String lang = "eng";
	String lang_lot = "lot";
	EditText viewstatus = null;

	public FileLoadingHelper(Activity anActivity, String datapath,
			String language, EditText status) {
		activity = anActivity;
		DATA_PATH = datapath;
		lang = language;
		viewstatus = status;
	}

	public boolean unzipTrainingFile() {
		boolean success = false;
		thread.start();
		
		return success;
	}

	private Thread thread = new Thread() {

		@Override
		public void run() {
			sendMessage("Unzipping OCR data, please wait...");
			
			String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

			try {
				for (String path : paths) {
					File dir = new File(path);
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							Log.v(TAG, "ERROR: Creation of directory " + path
									+ " on sdcard failed");
							return;
						} else {
							Log.v(TAG, "Created directory " + path
									+ " on sdcard");
						}
					}

				}
			} catch (Exception e) {
				sendMessage("Exception occured: " + e.getMessage());
				e.printStackTrace();
			}

			sendMessage("Unzipping files, please wait...");
			// double check if file exist
//			if (!(new File(DATA_PATH + TESS_DATA + "/eng.traineddata"))
//					.exists()) {
//				sendMessage("Unzipping file:" + TESS_ZIP_ENG);
//				unzipFile (TESS_ZIP_ENG);					
//				sendMessage("Unzipping completed.");																
//			} 
			if (!(new File(DATA_PATH + TESS_DATA + "/lot.traineddata"))
					.exists()) {
				sendMessage("Unzipping file:" + TESS_ZIP_LOT);
				unzipFile (TESS_ZIP_LOT);					
				sendMessage("Unzipping completed.");																
			} 
			sendMessage("OCR ready!");
		}
	};

	public void unzipFile(String zipfile) {
		try {
			// Open the ZipInputStream
			ZipInputStream inputStream = new ZipInputStream(activity
					.getAssets().open(TESS_DATA + "/" + zipfile));

			// Loop through all the files and folders
			for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream
					.getNextEntry()) {
				sendMessage("Extracting: " + entry.getName() + "...");

				String innerFileName = DATA_PATH + TESS_DATA + "/"
						+ entry.getName();
				File innerFile = new File(innerFileName);
				if (innerFile.exists()) {
					innerFile.delete();
				}

				// Check if it is a folder
				if (entry.isDirectory()) {
					// Its a folder, create that folder
					innerFile.mkdirs();
				} else {
					// Create a file output stream
					FileOutputStream outputStream = new FileOutputStream(
							innerFileName);
					final int BUFFER = 2048;

					// Buffer the ouput to the file
					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
							outputStream, BUFFER);

					// Write the contents
					int count = 0;
					byte[] data = new byte[BUFFER];
					while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
						bufferedOutputStream.write(data, 0, count);
					}

					// Flush and close the buffers
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
				sendMessage("DONE," + zipfile);

				// Close the current entry
				inputStream.closeEntry();
			}
			inputStream.close();	
		} catch (IOException e) {
			sendMessage("Exception occured: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "\n" + msg.getData().getString("data"));
			viewstatus.setText(msg.getData().getString("data"));
			super.handleMessage(msg);
		}
	};

	private void sendMessage(String text) {
		Message message = new Message();
		Bundle data = new Bundle();
		data.putString("data", text);
		message.setData(data);
		handler.sendMessage(message);
	}

}

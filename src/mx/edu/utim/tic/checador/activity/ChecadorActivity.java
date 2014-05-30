package mx.edu.utim.tic.checador.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.camera.CameraPreview;
import mx.edu.utim.tic.checador.db.BitacoraDataSource;
import mx.edu.utim.tic.checador.db.Registro;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * For Android camera about, check the oficial
 * https://developer.android.com/guide/topics/media/camera.html and
 * https://developer.android.com/reference/android/hardware/Camera.html
 * 
 * @author afelipe
 * 
 */
public class ChecadorActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private boolean pictureTaked = false, rotationS = true;
	public String mediaFileName = null, mediafilePath = "";
	FrameLayout preview;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	ImageButton shutterBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checador);
		this.shutterBtn = (ImageButton) findViewById(R.id.shutterBtn);
		
		// if not camera presence, come back and do nothing
		if (!checkCameraHardware(this)) {
			Toast.makeText(this, "No camera in your device, sorry! :D ", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		//loadCameraTask = new LoadCamera();
		//loadCameraTask.execute((Void) null);
		
//		if(!this.loadCamera())
//		{
//			finish();
//		}
		
		this.shutterBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mCamera != null) {

					mCamera.takePicture(null, null, mPicture);
					//mCamera.startPreview();
				} else {
					Log.d("Error camera", "No camera started");
					// mCamera = getCameraInstance();
					releaseCamera();
					//loadCamera();
				}
			}
		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	        //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    	rotationS = true;
	    }else
	    {
	    	rotationS = false;
	    }
	}
	
	protected void playSound(){
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
		rotationS = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!rotationS){
			releaseCamera(); // release the camera immediately on pause event
			finish();
		}else{
			if(!this.loadCamera())
				finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}
	
	private boolean loadCamera() {
		// Create an instance of Camera
		mCamera = getCameraInstance();
		if(mCamera == null)
		{
			//Toast.makeText(this, "Ha ocurrido un error con la cámara, ésta no responde.", Toast.LENGTH_SHORT).show();
			releaseCamera();
			return false;
		}
		
		
		setCameraDefaults();
		// Create our Preview view and set it as the content of our activity.
		// I set fragmentLaoyout dimentions to 1dp to 1dp at
		// activity_camara_frontral.xml for no showing user graphics
		// that's not a correctly solution, check how to, withou preview
		// check this StackOver
		// https://stackoverflow.com/questions/10799976/take-picture-without-preview-android
		mPreview = new CameraPreview(this, mCamera);
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		try {
			int rotation = getWindowManager().getDefaultDisplay().getRotation();
			if(rotation == 0 )
				mCamera.setDisplayOrientation(90);
			if(rotation == 1)
				mCamera.setDisplayOrientation(0);
			else if(rotation == 3)
				mCamera.setDisplayOrientation(180);
			
			mCamera.setPreviewDisplay(mPreview.getHolder());
			mCamera.startPreview();
			return true;
		} catch (IOException e) {
			Toast.makeText(this, "Ha ocurrido un error al cargar la vista de imagen.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
			return false;
		}
		
	}

	private void releaseCamera() {
		if (mCamera != null) {
			//mCamera.setPreviewCallback(null);
			//mPreview.getHolder().removeCallback(mPreview);
			mCamera.stopPreview();
			mCamera.release(); // release the camera for other applications
			mCamera = null;
			mPreview.destroyDrawingCache();
			mPreview = null;
		}
	}

	private void setCameraDefaults() {
		if(mCamera==null)
			return;
		
		Camera.Parameters params = mCamera.getParameters();
		// Supported picture formats (all devices should support JPEG).
		List<Integer> formats = params.getSupportedPictureFormats();
		if (formats.contains(ImageFormat.JPEG)) {
			params.setPictureFormat(ImageFormat.JPEG);
			params.setJpegQuality(100);
		} else
			params.setPictureFormat(PixelFormat.RGB_565);

		
		// Now the supported picture sizes.
		//List<Size> sizes = params.getSupportedPictureSizes();
		List<Size> sizes = params.getSupportedPreviewSizes();
		Camera.Size size = sizes.get(sizes.size() - 1);
		params.setPictureSize(size.width, size.height);
		// Set the brightness to auto.
		params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
		// Set the flash mode to auto.
		params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		// Set the scene mode to auto.
		params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
		// Lastly set the focus to auto.
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		try{
		mCamera.setParameters(params);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			if (Camera.getNumberOfCameras() == 2)
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // attempt instance
			else
				c = Camera.open(); // attempt to get a Camera instance, bay default is Back
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			Log.d("Checador TIC", "Error in camera: " + e.getMessage());
		}
		return c; // returns null if camera is unavailable
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			pictureTaked = false;
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE, getResources().getString(R.string.images_folder));
			if (pictureFile == null) {
				Log.d("Error camera take picture", "Error creating media file, check storage permissions: ");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				pictureTaked = true;
			} catch (FileNotFoundException e) {
				Log.d("Error saving picture", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("Error saving picture", "Error accessing file: " + e.getMessage());
			} finally {
				releaseCamera();
				// startActivity(new Intent(CamaraFrontal.this,
				// CamaraFrontal.class));
				// finish();
				//mCamera.startPreview();
				if(pictureTaked){
					//create reg in db
					BitacoraDataSource.guardarRegistro(getApplicationContext(), new Registro(0, pictureFile.getName(), new Date()));
					Toast.makeText(getApplicationContext(), "Se ha almacenado tu registro.", Toast.LENGTH_SHORT).show();
					finish();
					Intent goPreview = new Intent(getApplicationContext(), PreviewActivity.class);
					goPreview.putExtra("fileName", pictureFile.getName());
					goPreview.putExtra("fecha", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
					startActivity(goPreview);
					
				}else
				{
					//launch messagebox for notification
					mCamera.startPreview();
				}
			}
		}

	};

	// this method was taken from StackOver answer
	// https://stackoverflow.com/a/17631104/3395146
	private static File getOutputMediaFile(int type, String folder_name) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folder_name);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Error saving picture", "Unable to create directory!");
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
		File mediaFile = null;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			// throw new SaveFileException(TAG, "Unkknown media type!");
		}
		Log.d("saving file", mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		return mediaFile;
	}
	
	public class LoadCamera extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... arg0) {
			return loadCamera();
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			if(!success){
				//shutterBtn.setEnabled(false);
				finish();
			}
		}
	}
	
	public class TakePictureTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			if (mCamera != null) {
				try{
					mCamera.takePicture(null, null, mPicture);
					return true;
				}catch(Exception e){
					return false;
				}
			} else {
				Log.d("Error camera", "No camera started");
				Toast.makeText(getApplicationContext(), "Error al intentar capturar la foto. Vuelve a intentarlo.",  Toast.LENGTH_SHORT).show();
				releaseCamera();
				finish();
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
				finish();
			}
			if(pictureTaked){
				//go to preview
			}
		}
	}
}

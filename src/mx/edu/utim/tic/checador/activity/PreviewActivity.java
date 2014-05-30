package mx.edu.utim.tic.checador.activity;

import java.io.File;

import mx.edu.utim.tic.checador.R;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class PreviewActivity extends Activity {
	
	ImageView imagePreview;
	ImageButton backBtn;
	TextView fechaText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_preview);

		this.imagePreview = (ImageView) findViewById(R.id.pictureView);
		this.backBtn = (ImageButton) findViewById(R.id.backBtn);
		this.fechaText =(TextView) findViewById(R.id.fechaPreviewText);  
		this.backBtn.setAlpha(0.5f);
		
		Bundle extras = getIntent().getExtras();
		if(extras == null){
			finish();
		}
		
		String pathImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.images_folder);
		
		String fileName = pathImage + File.separator + extras.getString("fileName");
		//Toast.makeText(getApplicationContext(), "Cargando imagen: " + fileName, Toast.LENGTH_SHORT).show();
		fechaText.setText(extras.getString("fecha"));
		File imgFile = new  File(fileName);
		if(imgFile.exists()){
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			
			
			Display display = getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics();
			display.getMetrics(outMetrics);
			float scWidth = outMetrics.widthPixels;
			imagePreview.getLayoutParams().width = (int) scWidth;
			imagePreview.getLayoutParams().height = (int) (scWidth * 0.6f);		
			this.imagePreview.setImageBitmap(myBitmap);
			
//			final double viewWidthToBitmapWidthRatio = (double) myBitmap.getWidth() / (double)myBitmap.getWidth();
//			imagePreview.getLayoutParams().height = (int) (myBitmap.getHeight() * viewWidthToBitmapWidthRatio);
		}else
		{
			Toast.makeText(getApplicationContext(), "No se pudo localizar la imagen.", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		this.backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}

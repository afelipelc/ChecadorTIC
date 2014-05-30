package mx.edu.utim.tic.checador.activity;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.R.id;
import mx.edu.utim.tic.checador.R.layout;
import mx.edu.utim.tic.checador.R.menu;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	ImageButton ingresarBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		this.ingresarBtn = (ImageButton) findViewById(R.id.ingresarChecadorBtn);
		
		this.ingresarBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), ChecadorActivity.class));
			}
		});
		
//		this.ingresarBtn.setOnTouchListener(new View.OnTouchListener()
//        {       
//            public boolean onTouch(View arg0, MotionEvent arg1) { 
//                if(arg1.getAction() == MotionEvent.ACTION_MOVE)
//                {                   
//                	startActivity(new Intent(getApplicationContext(), ChecadorActivity.class));
//                }
//                return false;     
//                }     
//            });
		
		//Log.d("Checador TIC", "Rotation: " + getWindowManager().getDefaultDisplay().getRotation());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_admin:
			Intent goAdd = new Intent(this, LoginActivity.class);
			startActivity(goAdd);
			break;
		}
		return true;
	}
}

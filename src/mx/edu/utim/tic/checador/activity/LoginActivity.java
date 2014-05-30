package mx.edu.utim.tic.checador.activity;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.R.layout;
import mx.edu.utim.tic.checador.R.menu;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends Activity {

	Button loginBtn;
	EditText pinText;
	SharedPreferences configP;
	String pinS, defaultPin = "1239";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		this.loginBtn = (Button) findViewById(R.id.loginBtn);
		this.pinText = (EditText) findViewById(R.id.pinText);
		configP = getSharedPreferences("configuracion", 0); 
		pinS = configP.getString("pin", defaultPin);
		
		if(pinS.equals(defaultPin)){
			pinText.setHint(getString(R.string.default_pin));
		}
		
		this.loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ValidLogin();
			}
		});
		
		this.pinText.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if(arg1 == EditorInfo.IME_ACTION_NEXT || arg1 == EditorInfo.IME_ACTION_GO || arg1 == EditorInfo.IME_ACTION_DONE)
				{
					ValidLogin();
				}
				return false;
			} 
		});
	}

	private void ValidLogin(){
		if(pinText.getText().toString().equals(null) || pinText.getText().toString().equals(""))
		{
			this.pinText.requestFocus();
			Toast.makeText(this, "Ingresar PIN", Toast.LENGTH_SHORT).show();
		}
		
		if(pinS.equals(pinText.getText().toString())){
			finish();
			startActivity(new Intent(this, LogsActivity.class));
		}else{
			this.pinText.setText("");
			this.pinText.requestFocus();
			Toast.makeText(this, "PIN Inválido", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
}

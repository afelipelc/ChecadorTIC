package mx.edu.utim.tic.checador.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.db.BitacoraDataSource;
import mx.edu.utim.tic.checador.db.Registro;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LogsRangoActivity extends Activity {

	GridView gridLayout;
	List<Registro> gridArray;
	CustomGridViewAdapter customAdapter;
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	TextView avisoText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_logs_dummy);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		Date fechaInicio = null;
		Date fechaFin = null;
		Bundle extras = getIntent().getExtras();
		if(extras == null)
			finish();
		try
		{
		fechaInicio = format.parse(extras.getString("fechaI"));
		fechaFin = format.parse(extras.getString("fechaF"));
		}catch(ParseException e){
			e.printStackTrace();
			finish();
		}
		
		avisoText = (TextView) findViewById(R.id.avisoText);
		gridLayout = (GridView) findViewById(R.id.logsGrid);

		gridArray = BitacoraDataSource.listaRegistros(this, fechaInicio, fechaFin);
        if(gridArray == null || gridArray.size() == 0){
        	avisoText.setVisibility(View.VISIBLE);
        	return;
        }
        
		customAdapter = new CustomGridViewAdapter(this, R.layout.item_layout, (ArrayList<Registro>) gridArray);
		gridLayout.setAdapter(customAdapter);
		
		 gridLayout.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
	        		Intent goPreview = new Intent(getApplicationContext(), PreviewActivity.class);
					goPreview.putExtra("fileName", gridArray.get(position).getNombreImg());
					goPreview.putExtra("fecha", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(gridArray.get(position).getFecha()));
					startActivity(goPreview);
	        		}
	        	});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.logs_rango, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

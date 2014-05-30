package mx.edu.utim.tic.checador.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.activity.CustomGridViewAdapter;
import mx.edu.utim.tic.checador.activity.PreviewActivity;
import mx.edu.utim.tic.checador.db.BitacoraDataSource;
import mx.edu.utim.tic.checador.db.Registro;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class LogsDiaFragment extends Fragment {
	List<Registro> gridArray;
	CustomGridViewAdapter customAdapter;
	View rootView;
	GridView gridLayout;
	TextView avisoText;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_logs_dummy, container, false);
        gridLayout = (GridView)rootView.findViewById(R.id.logsGrid);
        avisoText = (TextView) rootView.findViewById(R.id.avisoText);
        gridArray = BitacoraDataSource.listaRegistrosDia(getActivity());
        
        if(gridArray == null || gridArray.size() == 0){
        	avisoText.setVisibility(View.VISIBLE);
        	return rootView;
        }
        
        customAdapter = new CustomGridViewAdapter(getActivity(), R.layout.item_layout, (ArrayList<Registro>)gridArray);
        gridLayout.setAdapter(customAdapter);
        
//        loadTask = new LoadDataTask();
//        loadTask.execute((Void) null);
        
        gridLayout.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        		Intent goPreview = new Intent(getActivity(), PreviewActivity.class);
				goPreview.putExtra("fileName", gridArray.get(position).getNombreImg());
				goPreview.putExtra("fecha", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(gridArray.get(position).getFecha()));
				startActivity(goPreview);
        		}
        	});
        
        return rootView;
    }
	

}

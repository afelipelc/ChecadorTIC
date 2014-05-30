package mx.edu.utim.tic.checador.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.activity.LogsRangoActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class LogsRangoFragment extends Fragment {
	Date fechaInicioD = new Date(), fechaFinD = new Date();
	Calendar calendar = Calendar.getInstance();
	int fechaedit=0;
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	
	static final int DATE_DIALOG_ID = 0;
	Button fechaInicio, fechaFin, verRegistrosDiaBtn;
	//Calendar c = Calendar.getInstance();
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logs_rango, container, false);
        fechaInicio = (Button) rootView.findViewById(R.id.fechaInicioBtn);
        fechaFin = (Button) rootView.findViewById(R.id.fechaFinBtn);
        verRegistrosDiaBtn = (Button) rootView.findViewById(R.id.verRegistrosDiaBtn);
        
        fechaInicio.setText(format.format(fechaInicioD));
        fechaFin.setText(format.format(fechaFinD));
        verRegistrosDiaBtn.setText(format.format(fechaFinD));
        
        fechaInicio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fechaedit=1;
				onCreateDialog(DATE_DIALOG_ID).show();
			}
		});
        
        fechaFin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fechaedit=2;
				onCreateDialog(DATE_DIALOG_ID).show();
			}
		});
        
        this.verRegistrosDiaBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fechaedit=3;
				onCreateDialog(DATE_DIALOG_ID).show();
			}
		});
        return rootView;
	}
	
	    protected Dialog onCreateDialog(int id) {
		    switch (id) {
		    case DATE_DIALOG_ID:
		    	
		        return new DatePickerDialog(getActivity(),
		                    mDateSetListener,
		                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		    }
		    return null;
		}
	   
	 // updates the date we display in the TextView
    private void updateDisplay(int year, int month, int day) {
    	switch(fechaedit)
		{
		case 1:
				calendar.set(year, month, day);
				fechaInicioD = calendar.getTime();
				fechaInicio.setText(format.format(fechaInicioD));
			break;
		case 2:
				calendar.set(year, month, day);
				fechaFinD = calendar.getTime();
				fechaFin.setText(format.format(fechaFinD));
				Intent goRango = new Intent(getActivity(), LogsRangoActivity.class);
				goRango.putExtra("fechaI", format.format(fechaInicioD));
				goRango.putExtra("fechaF", format.format(fechaFinD));
				startActivity(goRango);
			break;
		case 3:
			calendar.set(year, month, day);
			verRegistrosDiaBtn.setText(format.format(calendar.getTime()));
			Intent goDia = new Intent(getActivity(), LogsRangoActivity.class);
			goDia.putExtra("fechaI", format.format(calendar.getTime()));
			goDia.putExtra("fechaF", format.format(calendar.getTime()));
			startActivity(goDia);
		break;
		}
    	
		fechaedit=0;
    }
	
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    updateDisplay(year, monthOfYear, dayOfMonth);
                }
            };
            


}

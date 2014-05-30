package mx.edu.utim.tic.checador.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.db.BitacoraDataSource;
import mx.edu.utim.tic.checador.db.Registro;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OpcionesFragment extends Fragment {
	
	Button respaldarBDbtn, restaurarBDbtn, eliminarDatosbtn, cambiarPINbtn;
	boolean badPIN = false;
	View rootView;
	File stD,data;
	static final int DATE_DIALOG_ID = 0, CHANGE_DIALOG_PIN = 1;
	
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	Calendar calendar = Calendar.getInstance();
	SharedPreferences configP;
	DeleteDataTask deleteTask = null;
	AlertDialog.Builder confirmDialog, alertDialog, newPinDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_opciones, container, false);
		respaldarBDbtn = (Button) rootView.findViewById(R.id.respaldarBDBtn);
		restaurarBDbtn = (Button) rootView.findViewById(R.id.restaurarBDbtn);
		eliminarDatosbtn = (Button) rootView.findViewById(R.id.eliminarDatosBtn);
		cambiarPINbtn = (Button) rootView.findViewById(R.id.cambiarPinBtn);
		
		stD = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.images_folder)); //Environment.getExternalStorageDirectory();
	    data = Environment.getDataDirectory();
		
	    //Alert delete dialog
		alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Eliminar datos");
		alertDialog.setMessage("A continuación seleccione la fecha HASTA la que se eliminarán los datos. \nLa eliminación incluye las fotos.");
		alertDialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				onCreateDialog(DATE_DIALOG_ID).show();
			}

		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// nothing
			}
		});
	    /*
		//new pin dialog
		newPinDialog = new AlertDialog.Builder(getActivity());
		newPinDialog.setTitle("Nuevo PIN");
		newPinDialog.setMessage("Ingresa el nuevo PIN en ambas cajas de texto");
		
		final LinearLayout containerPIN = new LinearLayout(getActivity());
		// Set an EditText view to get user input 
		final EditText newPIN = new EditText(getActivity());
		newPIN.setHint("Nuevo PIN");
		newPIN.setSingleLine(true);
		newPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());//.setRawInputType(18);
		newPIN.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		//newPIN.setSingleLine(true);
		newPIN.setWidth(210);
		containerPIN.addView(newPIN);
		
		final EditText confirmPIN = new EditText(getActivity());
		confirmPIN.setHint("Confirmar PIN");
		confirmPIN.setSingleLine(true);
		confirmPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
		confirmPIN.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		confirmPIN.setWidth(210);
		//confirmPIN.setWidth(LayoutParams.MATCH_PARENT);
		containerPIN.addView(confirmPIN);
		newPinDialog.setView(containerPIN);
		
		newPinDialog.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Log.d("Checador", "Texto: " + newPIN.getText().toString());
				if(newPIN.getText().toString() != null && confirmPIN.getText().toString() != null && !newPIN.getText().toString().equals(""))
				{
					Log.d("Checador", "Guardando: " + newPIN.getText().toString());
					GuardarPIN(newPIN.getText().toString());
				}
				else
				{
					badPIN = true;
				}
			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// nothing
			}
		});
		*/
		
		respaldarBDbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RespaldarBD();
			}
		});
		restaurarBDbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RestaurarBD();
			}
		});
		
		eliminarDatosbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertDialog.show();
			}
		});
		
		cambiarPINbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CambiarPIN();	
			}
		});
		return rootView;
	}
	
	private void CambiarPIN(){
		onCreateDialog(CHANGE_DIALOG_PIN).show();
		if(badPIN){
			Toast.makeText(getActivity(), "El PIN y confirmar PIN no coinciden.", Toast.LENGTH_SHORT).show();
			//newPinDialog.show();
		}
	}
	private void GuardarPIN(String PIN){
		if(configP == null)
			configP = getActivity().getSharedPreferences("configuracion", 0);
		
		SharedPreferences.Editor configE = configP.edit();
		configE.putString("pin", PIN);
		configE.commit();
		
		Toast.makeText(getActivity(), "El PIN ha cambiado", Toast.LENGTH_LONG).show();
	}
    
	private void RespaldarBD(){
		try {
            if (stD.canWrite()) {
                String currentDBPath = "//data//mx.edu.utim.tic.checador//databases//bitacora.db";
                String backupDBPath = "bitacora.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(stD, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getActivity(), "Se ha respaldado la BD.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
	}
	
	private void RestaurarBD(){
//		File stD = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.images_folder)); //Environment.getExternalStorageDirectory();
//	    File data = Environment.getDataDirectory();
		try {
            if (stD.canWrite()) {
                String currentDBPath = "//data//mx.edu.utim.tic.checador//databases//bitacora.db";
                String backupDBPath = "bitacora.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(stD, backupDBPath);

                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getActivity(), "Se ha restaurado la BD.", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
	}
	

	
	 // updates the date we display in the TextView
    private void doTask(int year, int month, int day) {
    	calendar.set(year, month, day);
    	
    	confirmDialog = new AlertDialog.Builder(getActivity());
		confirmDialog.setTitle("Advertencia");
		confirmDialog.setMessage("Confirma eliminar todos los datos hasta:\n" + format.format(calendar.getTime()));
		confirmDialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				//Eliminar
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				//EliminarDatos();
				deleteTask = new DeleteDataTask();
				deleteTask.execute((Void) null);
			}

		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// nothing
			}
		});
    	
    	confirmDialog.show();
    }
	
    protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(getActivity(),
	                    mDateSetListener,
	                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	    case CHANGE_DIALOG_PIN:
	    	AlertDialog.Builder newPinDialog = new AlertDialog.Builder(getActivity());
			newPinDialog.setTitle("Nuevo PIN");
			newPinDialog.setMessage("Ingresa el nuevo PIN en ambas cajas de texto");
			
			final LinearLayout containerPIN = new LinearLayout(getActivity());
			// Set an EditText view to get user input 
			final EditText newPIN = new EditText(getActivity());
			newPIN.setHint("Nuevo PIN");
			newPIN.setSingleLine(true);
			newPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());//.setRawInputType(18);
			newPIN.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			//newPIN.setSingleLine(true);
			newPIN.setWidth(210);
			containerPIN.addView(newPIN);
			
			final EditText confirmPIN = new EditText(getActivity());
			confirmPIN.setHint("Confirmar PIN");
			confirmPIN.setSingleLine(true);
			confirmPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
			confirmPIN.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			confirmPIN.setWidth(210);
			//confirmPIN.setWidth(LayoutParams.MATCH_PARENT);
			containerPIN.addView(confirmPIN);
			newPinDialog.setView(containerPIN);
			
			newPinDialog.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int i) {
					Log.d("Checador", "Texto: " + newPIN.getText().toString());
					if(newPIN.getText().toString() != null && confirmPIN.getText().toString() != null && !newPIN.getText().toString().equals(""))
					{
						Log.d("Checador", "Guardando: " + newPIN.getText().toString());
						GuardarPIN(newPIN.getText().toString());
					}
					else
					{
						badPIN = true;
					}
				}
			}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// nothing
				}
			});
			
			return newPinDialog.create();
	    }
	    return null;
	}
    
   private boolean noData;
	private boolean EliminarDatos(){
		String pathImage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getResources().getString(R.string.images_folder);
		
		try{
			noData = false;
			List<Registro> registrosEliminar = BitacoraDataSource.listaRegistrosHasta(getActivity(), calendar.getTime());
			//Log.d("Checador TIC", (registrosEliminar == null ) + " << null? || size: " + registrosEliminar.size());
			if(registrosEliminar == null || ( registrosEliminar !=null && registrosEliminar.size() == 0)){
				//Toast.makeText(getActivity(), "No existen registros hasta el " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
				noData = true;
				return false;
			}
			
			for(Registro item : registrosEliminar){
				//eliminar archivo
				File file = new File(pathImage + File.separator +  item.getNombreImg());
				if(file.exists())
					file.delete();
			}
			
			//delete from database
			BitacoraDataSource.eliminarRegistrosHasta(getActivity(), calendar.getTime());
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
    
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    doTask(year, monthOfYear, dayOfMonth);
                }
          };
    
          public class DeleteDataTask extends AsyncTask<Void, Void,Boolean>{

      		@Override
      		protected Boolean doInBackground(Void... arg0) {
      			return EliminarDatos();
      		}

      		@Override
      		protected void onPostExecute(Boolean result) {
      			if(result)
      				Toast.makeText(getActivity(), "Datos eliminados", Toast.LENGTH_SHORT).show();
      			else if(noData)
      				Toast.makeText(getActivity(), "No hubo datos que eliminar hasta el: " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
      			else
      				Toast.makeText(getActivity(), "Ocurrió un error mientras se eliminaban los datos.", Toast.LENGTH_SHORT).show();
      			
      			deleteTask = null;
      		}
      	}
}

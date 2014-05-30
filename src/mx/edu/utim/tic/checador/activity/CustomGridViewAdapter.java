package mx.edu.utim.tic.checador.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mx.edu.utim.tic.checador.R;
import mx.edu.utim.tic.checador.db.Registro;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridViewAdapter  extends ArrayAdapter<Registro>{
	Context context; 
	int layoutResourceId;
	String pathImages = "";
	
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	ArrayList<Registro> data = new ArrayList<Registro>(); 
	
	public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList<Registro> data) { 
		super(context, layoutResourceId, data); 
		this.layoutResourceId = layoutResourceId; 
		this.context = context;
		this.data = data; 
		pathImages = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator +  context.getResources().getString(R.string.images_folder);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView; 
		RecordHolder holder = null; 
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.txtDate = (TextView) row.findViewById(R.id.itemDatetext);
			holder.imageItem = (ImageView) row.findViewById(R.id.itemImageView);
			row.setTag(holder);
		}else{
			holder = (RecordHolder) row.getTag();
		}
		Registro item = data.get(position);
		if(item.getFecha()!=null)
			holder.txtDate.setText(format.format(item.getFecha()));
		if(item.getNombreImg() != null && item.getNombreImg()!="")
		{
			File imgFile = new File(pathImages + File.separator + item.getNombreImg());
			if(imgFile.exists()){
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				holder.imageItem.setImageBitmap(myBitmap);
			}
		}
		return row;
	} 
	
	static class RecordHolder {
		TextView txtDate;
		ImageView imageItem;
	}

}

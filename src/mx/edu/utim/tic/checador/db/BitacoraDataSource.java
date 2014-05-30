package mx.edu.utim.tic.checador.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BitacoraDataSource {
	final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	final static SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	public final static Registro guardarRegistro(Context context, Registro registro) {
		ContentValues valores = new ContentValues();
		valores.put("imagen", registro.getNombreImg());
		valores.put("fecha", format.format(registro.getFecha()));
		SQLiteDatabase db = new MySQLiteHelper(context).getWritableDatabase();
		try {
			db.insert("bitacoraTbl", null, valores);
			db.close();
			return registro;
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return null;
		}
	}

	public final static List<Registro> listaRegistros(Context context,  Date inicio,  Date fin) {
		//return executeSQLtoList(context, "Select * from bitacoraTbl", new String[] {});
		return executeSQLtoList(context, "Select id, imagen, fecha, (strftime('%s', fecha , 'start of day') * 1000) AS fechams from bitacoraTbl where fechams >= (strftime('%s', ?, 'start of day') * 1000) and fechams <= (strftime('%s', ?, 'start of day') * 1000)", new String[]{ format.format(inicio), format.format(fin) });
	}
	
	public final static List<Registro> listaRegistrosDia(Context context) {
		return executeSQLtoList(context, "Select id, imagen, fecha, (strftime('%s', fecha , 'start of day') * 1000) AS fechams from bitacoraTbl where fechams >= (strftime('%s', ?, 'start of day') * 1000) and fechams <= (strftime('%s', ?, 'start of day') * 1000)", new String[]{ format.format(new Date()), format.format(new Date()) });
	}
	
	public final static List<Registro> listaRegistrosSemana(Context context) {
		
		Calendar cal = Calendar.getInstance();
		Calendar inicioSemana = (Calendar) cal.clone();
		inicioSemana.add(Calendar.DAY_OF_WEEK, inicioSemana.getFirstDayOfWeek() - inicioSemana.get(Calendar.DAY_OF_WEEK));
		
		Calendar finSemana = (Calendar) inicioSemana.clone();
		finSemana.add(Calendar.DAY_OF_YEAR, 6);
	    
		return executeSQLtoList(context, "Select id, imagen, fecha, (strftime('%s', fecha , 'start of day') * 1000) AS fechams from bitacoraTbl where fechams >= (strftime('%s', ?, 'start of day') * 1000) and fechams <= (strftime('%s', ?, 'start of day') * 1000)", new String[]{ format.format(inicioSemana.getTime()), format.format(finSemana.getTime()) });
	}
	
	public final static List<Registro> listaRegistrosHasta(Context context, Date hasta) {
		//return executeSQLtoList(context, "Select id, imagen, fecha, (strftime('%s', fecha , 'start of day') * 1000) AS fechams from bitacoraTbl where fechams <= (strftime('%s', ?, 'start of day') * 1000)", new String[]{ format.format(new Date())});
		return executeSQLtoList(context, "Select id, imagen, fecha from bitacoraTbl where fecha < ?", new String[]{ shortFormat.format(hasta)});
	}

	public final static void eliminarRegistrosHasta(Context context, Date hasta) {
		SQLiteDatabase db = new MySQLiteHelper(context).getWritableDatabase();
		db.delete("bitacoraTbl", "fecha < ?", new String[]{shortFormat.format(hasta)});
		
		//return executeSQLtoList(context, "DELETE id, imagen, fecha, (strftime('%s', fecha , 'start of day') * 1000) AS fechams from bitacoraTbl where fechams <= (strftime('%s', ?, 'start of day') * 1000)", new String[]{ format.format(new Date()), format.format(new Date()) });
	}
	private final static List<Registro> executeSQLtoList(Context context, String sql, String[] selectArgs) {
		SQLiteDatabase db = new MySQLiteHelper(context).getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, selectArgs);
		
		if (cursor != null && cursor.getCount() > 0) {
			List<Registro> listaRegistros = new ArrayList<Registro>();
			while (cursor.moveToNext()) {
				listaRegistros.add(CursorToRegistro(cursor));
			}
			db.close();
			return listaRegistros;
		} else {
			db.close();
			return null;
		}
	}

	private final static Registro CursorToRegistro(Cursor cursor) {
		Registro item = new Registro();
		item.setNombreImg(cursor.getString(cursor.getColumnIndex("imagen")));
		item.setId(cursor.getInt(cursor.getColumnIndex("id")));
		try {
			item.setFecha(format.parse(cursor.getString(cursor.getColumnIndex("fecha"))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return item;
	}
}

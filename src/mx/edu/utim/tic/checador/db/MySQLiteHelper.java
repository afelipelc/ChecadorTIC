package mx.edu.utim.tic.checador.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{
	private final static String nombreDB = "bitacora.db";
	private final static int versionDB = 1;
	private final static String bitacoraTbl = 
			"create table bitacoraTbl(" +
			" id integer primary key, " +
			" imagen text not null," +
			" fecha TIMESTAMP not null  DEFAULT current_timestamp);";
	//constructor
	public MySQLiteHelper(Context context) {
		super(context, nombreDB, null, versionDB);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(bitacoraTbl);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP table bitacoraTbl");
		onCreate(db);
	}
}

package mx.edu.utim.tic.checador.db;

import java.util.Date;

public class Registro {
	private int id;
	private String nombreImg;
	private Date fecha;

	public Registro(){
	}
	public Registro(int id, String nombre, Date fecha){
		this.id = id;
		this.nombreImg = nombre;
		this.fecha = fecha;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombreImg() {
		return nombreImg;
	}

	public void setNombreImg(String nombreImg) {
		this.nombreImg = nombreImg;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}

package al.uax.ejercicio.tema3.travelsummary;

import java.io.Serializable;

public class TravelInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//Atributos de la clase
	private String ciudad;
	private String pais;
	private int anyo;
	private String anotacion;
	
	//Constructor
	TravelInfo(String ciudad, String pais, int anyo, String anotacion){
		this.ciudad = ciudad;
		this.pais = pais;
		this.anyo = anyo;
		this.anotacion = anotacion;
	}
	
	//Métodos
	public String getCiudad() {
		return ciudad;
	}
	
	public String getPais() {
		return pais;
	}
	
	public int getAnyo() {
		return anyo;
	}
	
	public String getAnotacion() {
		return anotacion;
	}
}

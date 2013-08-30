package al.uax.ejercicio.tema3.travelsummary;

public class TravelInfo {
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

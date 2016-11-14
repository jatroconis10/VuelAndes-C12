package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class InfoViaje {
	
	@JsonProperty(value="origen")
	private String origen;
	
	@JsonProperty(value="destino")
	private String destino;
	
	@JsonProperty(value="clase")
	private boolean clase;
	
	@JsonProperty(value="aerolinea")
	private String aerolinea;
	
	@JsonProperty(value="fechaSalida")
	private Date fechaSalida;
	
	@JsonProperty(value="millas")
	private double millas;
	
	@JsonProperty(value="duracion")
	private int duracion;
	
	public InfoViaje(){
		super();
	}

	public InfoViaje(String origen, String destino, boolean clase, String aerolinea, Date fechaSalida,double millas, int duracion) {
		super();
		this.origen = origen;
		this.destino = destino;
		this.clase = clase;
		this.aerolinea = aerolinea;
		this.millas = millas;
		this.duracion = duracion;
		this.fechaSalida = fechaSalida;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public boolean isClase() {
		return clase;
	}

	public void setClase(boolean clase) {
		this.clase = clase;
	}

	public String getAerolinea() {
		return aerolinea;
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}

	public double getMillas() {
		return millas;
	}

	public void setMillas(double millas) {
		this.millas = millas;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
}

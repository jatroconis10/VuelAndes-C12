package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;;

public class SolicitudReservaPasajero {
	
	@JsonProperty(value="origen")
	private String origen;
	
	@JsonProperty(value="destino")
	private String destino;
	
	@JsonProperty(value="ejecutiva")
	private boolean ejecutiva;
	
	@JsonProperty(value="fecha")
	private Date fecha;
	
	@JsonProperty(value="costo")
	private boolean costo;
	
	@JsonProperty(value="tiempo")
	private boolean tiempo;
	
	@JsonProperty(value="escalas")
	private boolean escalas;
	
	

	public SolicitudReservaPasajero(String origen, String destino, boolean ejecutiva, Date fecha, boolean costo,
			boolean tiempo, boolean escalas) {
		super();
		this.origen = origen;
		this.destino = destino;
		this.ejecutiva = ejecutiva;
		this.fecha = fecha;
		this.costo = costo;
		this.tiempo = tiempo;
		this.escalas = escalas;
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

	public boolean isEjecutiva() {
		return ejecutiva;
	}

	public void setEjecutiva(boolean ejecutiva) {
		this.ejecutiva = ejecutiva;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isCosto() {
		return costo;
	}

	public void setCosto(boolean costo) {
		this.costo = costo;
	}

	public boolean isTiempo() {
		return tiempo;
	}

	public void setTiempo(boolean tiempo) {
		this.tiempo = tiempo;
	}

	public boolean isEscalas() {
		return escalas;
	}

	public void setEscalas(boolean escalas) {
		this.escalas = escalas;
	}
	
	

}

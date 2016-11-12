package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservasAerolineas {
	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="codIata")
	private String codIata;
	
	@JsonProperty(value="reservas")
	private List<ReservasPasajeros> reservas;

	public ReservasAerolineas(String nombre, String codIata, List<ReservasPasajeros> reservas) {
		super();
		this.nombre = nombre;
		this.codIata = codIata;
		this.reservas = reservas;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodIata() {
		return codIata;
	}

	public void setCodIata(String codIata) {
		this.codIata = codIata;
	}

	public List<ReservasPasajeros> getReservas() {
		return reservas;
	}

	public void setReservas(List<ReservasPasajeros> reservas) {
		this.reservas = reservas;
	}
	
	
}

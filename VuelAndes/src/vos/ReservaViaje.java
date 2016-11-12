package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservaViaje {
	
	@JsonProperty(value="id")
	private Long id;
	
	@JsonProperty(value="tipoIdU")
	private int tipoIdU;
	
	@JsonProperty(value="idU")
	private long idU;
	
	@JsonProperty(value="reservasPasajeros")
	private List<Long> reservasPasajeros;
	
	@JsonProperty(value="reservasCargas")
	private List<Long> reservasCargas;

	public ReservaViaje(Long id, int tipoIdU, long idU, List<Long> reservasPasajeros,
			List<Long> reservasCargas) {
		super();
		this.id = id;
		this.tipoIdU = tipoIdU;
		this.idU = idU;
		this.reservasPasajeros = reservasPasajeros;
		this.reservasCargas = reservasCargas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTipoIdU() {
		return tipoIdU;
	}

	public void setTipoIdU(int tipoIdU) {
		this.tipoIdU = tipoIdU;
	}

	public long getIdU() {
		return idU;
	}

	public void setIdU(int idU) {
		this.idU = idU;
	}

	public List<Long> getReservasPasajeros() {
		return reservasPasajeros;
	}

	public void setReservasPasajeros(List<Long> reservasPasajeros) {
		this.reservasPasajeros = reservasPasajeros;
	}

	public List<Long> getReservasCargas() {
		return reservasCargas;
	}

	public void setReservasCargas(List<Long> reservasCargas) {
		this.reservasCargas = reservasCargas;
	}
	
	
	

}

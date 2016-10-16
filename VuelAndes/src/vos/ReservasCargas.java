package vos;

public class ReservasCargas extends Reservas {
	private Integer trackingNumber;
	
	public ReservasCargas(Integer id, Integer tipoId, Integer idUsuario, Integer idVuelo, Integer trackingNumber) {
		super(id,tipoId, idUsuario, idVuelo);
		this.trackingNumber = trackingNumber;
	}

	public ReservasCargas(){
		super(0,0,0,0);
	}

	public Integer getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(Integer trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

}

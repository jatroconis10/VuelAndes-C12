package vos;

public class ReservasCargas extends Reservas {
	private Integer trackingNumber;
	
	public ReservasCargas(Integer id, Integer tipoId, Long idUsuario, Long idVuelo, Integer trackingNumber) {
		super(id,tipoId, idUsuario, idVuelo);
		this.trackingNumber = trackingNumber;
	}

	public ReservasCargas(){
		super(0,0,new Long(0),new Long(0));
	}

	public Integer getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(Integer trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

}

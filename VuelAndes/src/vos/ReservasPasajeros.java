package vos;

public class ReservasPasajeros extends Reservas {

	private boolean esEjecutiva;
	
	
	public ReservasPasajeros(Integer id,Integer tipoId, Long idUsuario, Long idVuelo,boolean esEjecutiva) {
		super(id, tipoId, idUsuario, idVuelo);
		this.esEjecutiva = esEjecutiva;
		
	}
	public ReservasPasajeros(){
		super(0,0,new Long(0),new Long(0));
	}
	public boolean getEjecutiva(){
		return esEjecutiva;
	}
	public void setEsEjecutiva(boolean esEjecutiva) {
		this.esEjecutiva = esEjecutiva;
	}
	
}

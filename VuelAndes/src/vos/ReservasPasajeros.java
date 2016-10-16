package vos;

public class ReservasPasajeros extends Reservas {

	private boolean esEjecutiva;
	
	
	public ReservasPasajeros(Integer id,Integer tipoId, Integer idUsuario, Integer idVuelo,boolean esEjecutiva) {
		super(id, tipoId, idUsuario, idVuelo);
		this.esEjecutiva = esEjecutiva;
		
	}
	public ReservasPasajeros(){
		super(0,0,0,0);
	}
	public boolean getEjecutiva(){
		return esEjecutiva;
	}
	public void setEsEjecutiva(boolean esEjecutiva) {
		this.esEjecutiva = esEjecutiva;
	}
	
}

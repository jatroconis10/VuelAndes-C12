package vos;

import java.util.Date;

public class VueloReserva extends Vuelo {
	private double costoPorDensidad;
	
	
	public VueloReserva(TipoViaje tipo, Long idVuelo, Date horaSalida, Date horaLlegada, Long duracion,
			Double distancia, Integer frecuencia, String aeroPuertoSalida, String aeroPuertoLlegada,
			String codIATAaerolinea) {
		super(tipo, idVuelo, horaSalida, horaLlegada, duracion, distancia, frecuencia, aeroPuertoSalida, aeroPuertoLlegada,
				codIATAaerolinea);
	}

	public VueloReserva(TipoViaje tipo, Long idVuelo, Date horaSalida, Long duracion,
			Double distancia, Integer frecuencia, String aeroPuertoSalida, String aeroPuertoLlegada,
			String codIATAaerolinea) {
		super();
		this.tipo = tipo;
		this.idVuelo = idVuelo;
		this.horaSalida = horaSalida;
		this.duracion = duracion;
		this.distancia = distancia;
		this.frecuencia = frecuencia;
		this.aeroPuertoSalida = aeroPuertoSalida;
		this.aeroPuertoLlegada = aeroPuertoLlegada;
		this.codIATAaerolinea = codIATAaerolinea;
	}

	
	
	
	
	public double getCostoPorDensidad() {
		return costoPorDensidad;
	}

	public void setCostoPorDensidad(double costoPorDensidad) {
		this.costoPorDensidad = costoPorDensidad;
	}

}

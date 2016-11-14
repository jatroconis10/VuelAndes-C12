package vos;

import java.util.Date;

public class VueloPasajero extends Vuelo {

	public VueloPasajero(TipoViaje tipo, Long idVuelo, Date horaSalida, Date horaLlegada, Long duracion,
			Double distancia, Integer frecuencia, String aeroPuertoSalida, String aeroPuertoLlegada,
			String codIATAaerolinea) {
		super(tipo, idVuelo, horaSalida, horaLlegada, duracion, distancia, frecuencia, aeroPuertoSalida, aeroPuertoLlegada,
				codIATAaerolinea);
		// TODO Auto-generated constructor stub
	}

	public VueloPasajero(TipoViaje tipo, Long idVuelo, Date horaSalida, Long duracion,
			Double distancia, Integer frecuencia, String aeroPuertoSalida, String aeroPuertoLlegada,
			String codIATAaerolinea) {
		
		this.tipo = tipo;
		this.idVuelo = idVuelo;
		this.horaSalida = horaSalida;
		this.duracion = duracion;
		this.distancia = distancia;
		this.frecuencia = frecuencia;
		this.aeroPuertoSalida = aeroPuertoSalida;
		this.aeroPuertoLlegada = aeroPuertoLlegada;
		this.codIATAaerolinea = codIATAaerolinea;
		// TODO Auto-generated constructor stub
	}
}

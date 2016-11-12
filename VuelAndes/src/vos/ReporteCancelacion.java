package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReporteCancelacion {
	
	@JsonProperty(value="reservasNuevas")
	private List<ReservaViaje> reservas;
	
	@JsonProperty(value="infoAerolineas")
	private List<ReservasAerolineas> reservasAerolineas;

	public ReporteCancelacion(List<ReservaViaje> reservas, List<ReservasAerolineas> reservasAerolineas) {
		super();
		this.reservas = reservas;
		this.reservasAerolineas = reservasAerolineas;
	}

	public List<ReservaViaje> getReservas() {
		return reservas;
	}

	public void setReservas(List<ReservaViaje> reservas) {
		this.reservas = reservas;
	}

	public List<ReservasAerolineas> getReservasAerolineas() {
		return reservasAerolineas;
	}

	public void setReservasAerolineas(List<ReservasAerolineas> reservasAerolineas) {
		this.reservasAerolineas = reservasAerolineas;
	}
	
	

}

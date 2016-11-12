package rest;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.VuelAndesMaster;
import vos.*;

@Path("reservas/{tipoId:\\d}/{idUsuario:\\d}")
public class VuelAndesReservasServices {
	
	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@POST
	@Path("reservarPasajero")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response hacerReservaPasajeros(ReservasPasajeros r) {
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			tm.hacerReservaP(r);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(r).build();
	}
	
	@POST
	@Path("/reservarCarga")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response hacerReservaCarga(ReservasCargas r) {
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			tm.hacerReservaC(r);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(r).build();
	}
	
	@POST
	@Path("solicitarReservaPasajero")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hacerReservaPasajero(SolicitudReservaPasajero solicitud, @PathParam("tipoId")int tipoId
			, @PathParam("idUsuario")Long idU){
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		ReservaViaje r = null;
		try {
			r = tm.hacerReservaP(solicitud, tipoId, idU);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(r).build();
		
	}
	
	@DELETE
	@Path("cancelarReservaVuelo/{idReserva:\\d}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarReservaPasajeroVuelo(@PathParam("tipoId")int tipoId
			, @PathParam("idUsuario")Long idU, @PathParam("idReserva")Long idReserva){
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		String r = "";
		try {
			r = tm.cancelarReservaVuelo(idReserva, tipoId, idU);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(r).build();
	}
	
	@DELETE
	@Path("cancelarReservaViaje/{idReserva:\\d}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelarReservaPasajeroViaje(@PathParam("tipoId")int tipoId
			, @PathParam("idUsuario")Long idU, @PathParam("idReserva")Long idReserva){
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		String r = "";
		try {
			r = tm.cancelarReservaViaje(idReserva, tipoId, idU);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(r).build();
	}
}

package rest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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

import vos.*;
import tm.VuelAndesMaster;

@Path("vuelos/{tipoId}/{idUsuario}")
public class VuelAndesVuelosServices {
	
	@PathParam("tipoId")
	private int tipoId;
	
	@PathParam("idUsuario")
	private long idUsuario;
	
	@Context
	private ServletContext context;

	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	@POST
	@Path("asociar/{idVuelo:\\d+}/{idAvion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response asociarAeronaveAVuelo(@PathParam("idVuelo") Long id,@PathParam("idAvion")String a) {
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			tm.asociarAeronaveAVuelo(id, a);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	@POST
	@Path("registrar/{idVuelo:\\d+}/{fechaSalida}/{fechaLlegada}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarVueloRealizado(@PathParam("idVuelo") Long id,@PathParam("fechaSalida")String salida,@PathParam("fechaLlegada")String llegada) {
		System.out.println("Entrar al servicio");
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			tm.registrarVueloRealizado(id, salida,llegada);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).build();
	}
	@GET
	@Path("reporte/{idVuelo:\\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generarReporte(@PathParam("idVuelo") Long id) {
		System.out.println("Entrar al servicio");
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		Double reporte = null;
		try {
			reporte = tm.generarReporte(id);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reporte).build();
	}
	
	@GET
	@Path("itinerario/{aeropuertoId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarItinerarioAeropuerto(
			@PathParam("aeropuertoId") String a,
			@DefaultValue("")@QueryParam("fechaInit")String fechaI,
			@DefaultValue("")@QueryParam("fechaFinal")String fechaF,
			@DefaultValue("")@QueryParam("aerolinea")String aero,
			@DefaultValue("0")@QueryParam("tipoAeronave")int tipo,
			@DefaultValue("")@QueryParam("horaSalida")String hSalida,
			@DefaultValue("")@QueryParam("horaLLegada")String hLlegada) 
	{
		System.out.println("Entrar al servicio");
		ArrayList<Vuelo> itinerario;
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			itinerario = tm.consultarIntinerario(a,fechaI,fechaF,aero,tipo,hSalida,hLlegada);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(itinerario).build();
	}
	
	@GET
	@Path("itinerarioCondNeg/{aeropuertoId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarItinerarioNoCumpleAeropuerto(
			@PathParam("aeropuertoId") String a,
			@DefaultValue("")@QueryParam("fechaInit")String fechaI,
			@DefaultValue("")@QueryParam("fechaFinal")String fechaF,
			@DefaultValue("")@QueryParam("aerolinea")String aero,
			@DefaultValue("0")@QueryParam("tipoAeronave")int tipo,
			@DefaultValue("")@QueryParam("horaSalida")String hSalida,
			@DefaultValue("")@QueryParam("horaLLegada")String hLlegada) 
	{
		System.out.println("Entrar al servicio");
		ArrayList<Vuelo> itinerario;
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			itinerario = tm.consultarIntinerario(a,fechaI,fechaF,aero,tipo,hSalida,hLlegada);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(itinerario).build();
	}
	
	@GET
	@Path("infoAerolinea/{aerolineaId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarInformacionAerolinea(@PathParam("aerolineaId") String a, @DefaultValue("V")@QueryParam("rol")int rol ) {
		System.out.println("Entrar al servicio");
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		try {
			tm.consultarInformacionAerolinea(a,rol);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(a).build();
	}
	
	@DELETE
	@Path("cancelarViaje/{idVuelo:\\d}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelarViaje(@PathParam("idVuelo")Long idVuelo,@PathParam("tipoId")int tipoId){
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		ReporteCancelacion r = null;
		try {
			r = tm.cancelarViaje(idVuelo, tipoId, idUsuario);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("").build();
	}
	
	@GET
	@Path("consultarViajes/{millas:\\d}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarViajes(@PathParam("millas")int millas,
			@DefaultValue("")@QueryParam("fechaI")String fechaI,
			@DefaultValue("")@QueryParam("fechaF")String fechaF){
		
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		List<InfoViaje> r = null;
		try {
			r = tm.consultarViajes(tipoId, idUsuario, millas, fechaI, fechaF);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("").build();
	}
	
	@GET
	@Path("traficoEntre/{ciudad1}/{ciudad2}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response darTraficoEntre(@PathParam("ciudad1")String c1, 
			@PathParam("ciudad2")String c2,
			@DefaultValue("")@QueryParam("fechaI")String fechaI,
			@DefaultValue("")@QueryParam("fechaF")String fechaF)
	{
		VuelAndesMaster tm = new VuelAndesMaster(getPath());
		List<InfoViaje> r = null;
		try {
			r = tm.consultarTraficoEntre(tipoId, idUsuario, c1, c2, fechaI, fechaF);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity("").build();
	}
}

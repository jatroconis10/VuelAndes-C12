package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vos.*;
import vos.Vuelo.TipoViaje;

public class DAOVuelos extends AbstractDAO {

	public DAOVuelos(){
		super();
	}

	public Date darFechaVuelo(Long idVuelo) throws SQLException,Exception{
		
		String sql;
		sql = "SELECT HORA_SALIDA FROM ISIS2304A141620.VUELOS WHERE ID_VUELO="+idVuelo;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()){
			
			return rs.getDate("HORA_SALIDA");
		}
		else{
			throw new Exception("No existe ese vuelo");
		}
		
	}
	public List<Vuelo> darVuelos() throws SQLException, Exception{

		String sql;
		sql = "SELECT * FROM ISIS2304A141620.VUELOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		List<Vuelo> lista = new ArrayList<>();

		while(rs.next()){
			long idVuelo= rs.getLong("ID_VUELO");
			Date horaSalida = rs.getDate("HORA_SALIDA");
			Date horaLlegada = rs.getDate("HORA_LLEGADA");
			String avion = rs.getString("AVION_ASIGNADO");
			String aerolinea = rs.getString("CODIATA_AEROLINEA");
			String aeropuertoSalida = rs.getString("CODIATA_AEROPUERTOPARTIDA");
			String aeropuertoLlegada = rs.getString("CODIATA_AEROPUERTOLLEGADA");
			int tipoVuelo = rs.getInt("TIPO_VUELO");
			int tipoViaje = rs.getInt("TIPO_VIAJE");
			Long duracion = Long.parseLong(rs.getString("DURACION"));
			Double distancia = rs.getDouble("DISTANCIA");
			int frecuencia = rs.getInt("FRECUENCIA");

			Vuelo act = new VueloPasajero(Vuelo.TipoViaje.INTERNACIONAL, idVuelo, horaSalida, horaLlegada, duracion, distancia, frecuencia, aeropuertoSalida, aeropuertoLlegada, aerolinea);
			lista.add(act);
		}

		return lista;
	}
	
	public Vuelo darVuelo(Long idVuelo) throws SQLException,Exception{
		String sql;
		sql = "SELECT * FROM ISIS2304A141620.AEROPUERTOS WHERE ID_VUELO=" + idVuelo;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		Vuelo v = null;
		
		if(rs.next()){
			Date horaSalida = rs.getDate("HORA_SALIDA");
			Date horaLlegada = rs.getDate("HORA_LLEGADA");
			String avion = rs.getString("AVION_ASIGNADO");
			String aerolinea = rs.getString("CODIATA_AEROLINEA");
			String aeropuertoSalida = rs.getString("CODIATA_AEROPUERTOPARTIDA");
			String aeropuertoLlegada = rs.getString("CODIATA_AEROPUERTOLLEGADA");
			int tipoVuelo = rs.getInt("TIPO_VUELO");
			int tipoViaje = rs.getInt("TIPO_VIAJE");
			Long duracion = Long.parseLong(rs.getString("DURACION"));
			Double distancia = rs.getDouble("DISTANCIA");
			int frecuencia = rs.getInt("FRECUENCIA");

			Vuelo act = new VueloPasajero(Vuelo.TipoViaje.INTERNACIONAL, idVuelo, horaSalida, horaLlegada, duracion, distancia, frecuencia, aeropuertoSalida, aeropuertoLlegada, aerolinea);
			v = act;
		}
		else{
			throw new Exception("No existe ese vuelo");
		}
		
		return v;
	}
	
	public double darCostoVueloPasajero(Long vuelo, boolean ejecutivo) throws SQLException,Exception{
		
		String sql;
		sql = "SELECT * FROM ISIS2304A141620.COSTOSPASAJEROS WHERE ID_VUELO=" + vuelo;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			
			if(ejecutivo){
				return rs.getDouble("COSTO_EJECUTIVO");
			}
			else{
				return rs.getDouble("COSTO_ECONOMICO");
			}
		}
		else{
			throw new Exception("No existe ese vuelo");
		}
		
	}
	public void asociarAeronaveAVuelo(Long v, String a)  throws SQLException, Exception{

		String sql = "SELECT TIPO_AVION,SILLAS_EJECUTIVAS,SILLAS_ECONOMICAS,CAPACIDAD FROM ISIS2304A141620.AVIONES WHERE NUMERO_DE_SERIE = '"+a+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()){

			int tipoAvion = rs.getInt("TIPO_AVION");
			int sillasEjecutivas = rs.getInt("SILLAS_EJECUTIVAS");
			int sillasEconomicas= rs.getInt("SILLAS_ECONOMICAS");
			int peso = rs.getInt("CAPACIDAD");

			//pasajeros
			if(tipoAvion == 1){
				sql = "SELECT ID_VUELO, COUNT(CASE WHEN EJECUTIVA='Y' THEN 1 END) AS EJEC, COUNT(CASE WHEN EJECUTIVA = 'N' THEN 1 END) AS ECON"
						+ " FROM ISIS2304A141620.VUELOS v LEFT OUTER JOIN ISIS2304A141620.RESERVASPASAJEROS r ON v.ID_VUELO=r.VUELO"
						+ " WHERE ID_VUELO = "+v+" AND TIPO_VUELO=1 GROUP BY ID_VUELO";

				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				if(rs.next()){
					int ejec = rs.getInt("EJEC");
					int econ = rs.getInt("ECON");

					if(ejec <= sillasEjecutivas && econ <= sillasEconomicas){
						sql = "UPDATE ISIS2304A141620.VUELOS SET AVION_ASIGNADO = '"+a+
								"' WHERE ID_VUELO=" + v;
						prepStmt = conn.prepareStatement(sql);
						recursos.add(prepStmt);
						rs = prepStmt.executeQuery();
					}
					else{
						throw new Exception("El avion no tiene sufiecentes sillas para la reservas");
					}
				}else{
					throw new Exception("No existe un vuelo con ese id");
				}
			}
			//cargas
			else if(tipoAvion == 2){
				sql = "SELECT ID_VUELO, COUNT(NVL(PESO,0)AS PESOTOT "
						+ "FROM ISIS2304A141620.VUELOS v "
						+ "LEFT OUTER JOIN ISIS2304A141620.RESERVASCARGAS r ON v.ID_VUELO=r.VUELO "
						+ "INNER JOIN ISIS2304A141620.CARGAS c ON r.CARGA = c.ID " + 
						"GROUP BY ID_VUELO WHERE ID_VUELO = '"+v+"' AND TIPO_VUELO=2";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				if(rs.next()){
					int pesoTot = rs.getInt("PESOTOT");

					if(pesoTot <= peso){
						sql = "UPDATE ISIS2304A141620.VUELOS SET AVION_ASIGNADO = "+a+
								"WHERE ID_VUELO=" + v;
					}
					else{
						throw new Exception("El avion no tiene sufiecentes capacidad para la reservas");
					}
				}else{
					throw new Exception("No existe un vuelo con ese id");
				}
			}
		}
		else{
			throw new Exception("No existe un avion con ese id");
		}
	}

	public void registrarVueloRealizado(Long id, String salida, String llegada) throws SQLException {

		String salidaSql = "TO_DATE("+ salida + ",'DD-MON-YY HH:MI')";
		String llegadaSql = "TO_DATE("+ llegada + ",'DD-MON-YY HH:MI')";
		
		String sql = "INSERT INTO ISIS2304A141620.VUELOSREAIZADOS VALUES("+salidaSql+","+llegadaSql+","+id+")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

	}

	public Double generarReporte(Long id) throws Exception {

		String sql = "SELECT TIPO_VUELO FROM ISIS2304A141620.VUELOS WHERE ID_VUELO = "+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next())
		{
			if(rs.getInt("TIPO_VUELO")== 1)
			{
				sql = "SELECT COSTO_EJECUTIVO,COSTO_ECONOMICO FROM ISIS2304A141620.COSTOSPASAJEROS WHERE ID_VUELO = "+id;

				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();

				if(rs.next())
				{
					int costoejec = rs.getInt("COSTO_EJECUTIVO");
					int costoecon = rs.getInt("COSTO_ECONOMICO");
					sql = "SELECT ID_VUELO, COUNT(CASE WHEN EJECUTIVA='Y' THEN 1 END) AS EJEC, COUNT(CASE WHEN EJECUTIVA = 'N' THEN 1 END) AS ECON"
							+ " FROM ISIS2304A141620.VUELOS v LEFT OUTER JOIN ISIS2304A141620.RESERVASPASAJEROS r ON v.ID_VUELO=r.VUELO"
							+ " WHERE ID_VUELO = "+id+" AND TIPO_VUELO=1GROUP BY ID_VUELO";

					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					rs = prepStmt.executeQuery();
					if(rs.next()){
						int ejec = rs.getInt("EJEC");
						int econ = rs.getInt("ECON");

						return new Double(costoejec*ejec + costoecon*econ);
					}

				}

			}
			else if(rs.getInt("TIPO_VUELO")== 2)
			{

				sql = "SELECT COSTO_DENSIDAD FROM ISIS2304A141620.COSTOSCARGAS WHERE ID_VUELO = "+id;
				if(rs.next())
				{
					int densidad = rs.getInt("COSTO_DENSIDAD");
					sql = "SELECT ID_VUELO, COUNT(NVL(PESO,0)AS PESOTOT "
							+ "FROM ISIS2304A141620.VUELOS v "
							+ "LEFT OUTER JOIN ISIS2304A141620.RESERVASCARGAS r ON v.ID_VUELO=r.VUELO "
							+ "INNER JOIN ISIS2304A141620.CARGAS c ON r.CARGA = c.ID " + 
							"GROUP BY ID_VUELO WHERE ID_VUELO = '"+id+"' AND TIPO_VUELO=2";
					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					rs = prepStmt.executeQuery();
					if(rs.next()){
						int pesoTot = rs.getInt("PESOTOT");

						return new Double(pesoTot*densidad);

					}
					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					rs = prepStmt.executeQuery();
				}

			}
		}
		else{
			throw new Exception("no existe ese vuelo");
		}
		return new Double(0);
	}

	public ArrayList<Vuelo> consultarIntinerario(String a,String fechaI,String fechaF,String aero, int tipo, String hSalida,String hLlegada) throws SQLException {
		String sql = "SELECT * FROM ISIS2304A141620.VUELOS v NATURAL JOIN VUELOSPROGRAMADOS vp WHERE (v.CODIATA_AEROPUERTOPARTIDA = '"+a+"' OR "+ "v.CODIATA_AEROPUERTOLLEGADA = '"+a+"')" ;
		
		if(!fechaI.isEmpty()){
			sql+=" AND vp.FECHA_SALIDA > TO_DATE(" + fechaI + ",'DD/MM/YYYY')";
		}
		if(!fechaF.isEmpty()){
			sql+=" AND vp.FECHA_SALIDA < TO_DATE(" + fechaF + ",'DD/MM/YYYY')";
		}
		if(!aero.isEmpty()){
			sql += " AND v.CODIATA_AEROLINEA = '" + aero + "'"; 
		}
		
		if( tipo != 0){
			sql += " AND v.TIPO_VUELO = " + tipo;
		}
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Vuelo> vuelos = new ArrayList();
		while(rs.next()){
			if(rs.getInt("TIPO_VUELO")==1)
			{
				
				if(rs.getInt("TIPO_VIAJE")==1)
				{
					Vuelo act = new VueloPasajero(Vuelo.TipoViaje.INTERNACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
						vuelos.add(act);	
				}
				else if(rs.getInt("TIPO_VIAJE")==2)
				{
					Vuelo act = new VueloPasajero(Vuelo.TipoViaje.NACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);
				}
			}
			else if (rs.getInt("TIPO_VUELO")==2)
			{
				if(rs.getInt("TIPO_VIAJE")==1)
				{
					Vuelo act = new VueloReserva(Vuelo.TipoViaje.INTERNACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);	
				}
				else if(rs.getInt("TIPO_VIAJE")==2)
				{
					Vuelo act = new VueloReserva(Vuelo.TipoViaje.NACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);
				}
			}
			
		}
		return vuelos;
		
	}
	
	public ArrayList<Vuelo> consultarIntinerarioNoCumple(String a,String fechaI,String fechaF,String aero,int tipo, String hSalida,String hLlegada) throws SQLException {
		String sql = "SELECT * FROM ISIS2304A141620.VUELOS v INNER JOIN VUELOSPROGRAMADOS vp WHERE (v.CODIATA_AEROPUERTOPARTIDA = '"+a+"' OR "+ "v.CODIATA_AEROPUERTOLLEGADA = '"+a+"')" ;
		
		if(!fechaI.isEmpty()){
			sql+=" AND vp.FECHA_SALIDA > TO_DATE(" + fechaI + ",'DD/MM/YYYY')";
		}
		if(!fechaF.isEmpty()){
			sql+=" AND vp.FECHA_SALIDA < TO_DATE(" + fechaF + ",'DD/MM/YYYY')";
		}
		if(!aero.isEmpty()){
			sql += " AND v.CODIATA_AEROLINEA != '" + aero + "'"; 
		}
		if( tipo != 0){
			sql += " AND v.TIPO_VUELO != " + tipo;
		}
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		ArrayList<Vuelo> vuelos = new ArrayList();
		while(rs.next()){
			if(rs.getInt("TIPO_VUELO")==1)
			{
				
				if(rs.getInt("TIPO_VIAJE")==1)
				{
					Vuelo act = new VueloPasajero(Vuelo.TipoViaje.INTERNACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
						vuelos.add(act);	
				}
				else if(rs.getInt("TIPO_VIAJE")==2)
				{
					Vuelo act = new VueloPasajero(Vuelo.TipoViaje.NACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);
				}
			}
			else if (rs.getInt("TIPO_VUELO")==2)
			{
				if(rs.getInt("TIPO_VIAJE")==1)
				{
					Vuelo act = new VueloReserva(Vuelo.TipoViaje.INTERNACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);	
				}
				else if(rs.getInt("TIPO_VIAJE")==2)
				{
					Vuelo act = new VueloReserva(Vuelo.TipoViaje.NACIONAL,Long.valueOf(rs.getInt("ID_VUELO")),rs.getDate("FECHA_SALIDA"),Long.valueOf(rs.getInt("DURACION")),Double.valueOf(rs.getInt("DISTANCIA")),rs.getInt("FRECUENCIA"),rs.getString("CODIATA_AEROPUERTOPARTIDA"),rs.getString("CODIATA_AEROPUERTOLLEGADA"),rs.getString("CODIATA_AEROLINEA"));
					vuelos.add(act);
				}
			}
			
		}
		return vuelos;
	}
	
	public List<InfoViaje> consultarViajesGenerales(int millas, String fechaI, String fechaF) throws SQLException{
		
		String sql = "SELECT * FROM VUELOSREALIZADOS vr NATURAL JOIN VUELOS v WHERE v.DISTANCIA >" + millas;
		
		if(!fechaI.isEmpty()){
			sql+=" AND vr.FECHA_SALIDA > TO_DATE(" + fechaI + ",'DD/MM/YYYY')";
		}
		if(!fechaF.isEmpty()){
			sql+=" AND vr.FECHA_LLEGADA < TO_DATE(" + fechaF + ",'DD/MM/YYYY')";
		}
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		List<InfoViaje> respuesta = new ArrayList<>();
		
		while(rs.next()){
			InfoViaje act = new InfoViaje();
			act.setOrigen(rs.getString("CODIATA_AEROPUERTOPARTIDA"));
			act.setDestino(rs.getString("CODIATA_AEROPUERTOLLEGADA"));
			act.setAerolinea(rs.getString("CODIATA_AEROLINEA"));
			act.setDuracion(rs.getInt("DURACION"));
			act.setMillas(rs.getInt("DISTANCIA"));
			respuesta.add(act);
		}
		
		return respuesta;
	}
	
	public List<InfoViaje> consultarViajesUsuario(int tipoId, long IdUsuario, int millas, String fechaI, String fechaF) throws SQLException{
		String sql = "SELECT * FROM RESERVASPASAJEROS rp INNER JOIN VUELOSREALIZADOS vr  ON rp.VUELO = vr.ID_VUELO"
				+ " NATURAL JOIN VUELOS v WHERE v.DISTANCIA >" + millas +" AND rp.TIPO_ID = "+ tipoId + " AND rp.ID_USUARIO = "+ IdUsuario;

		if(!fechaI.isEmpty()){
			sql+=" AND vr.FECHA_SALIDA > TO_DATE(" + fechaI + ",'DD/MM/YYYY')";
		}
		if(!fechaF.isEmpty()){
			sql+=" AND vr.FECHA_LLEGADA < TO_DATE(" + fechaF + ",'DD/MM/YYYY')";
		}

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<InfoViaje> respuesta = new ArrayList<>();

		while(rs.next()){
			InfoViaje act = new InfoViaje();
			act.setOrigen(rs.getString("CODIATA_AEROPUERTOPARTIDA"));
			act.setDestino(rs.getString("CODIATA_AEROPUERTOLLEGADA"));
			act.setAerolinea(rs.getString("CODIATA_AEROLINEA"));
			act.setDuracion(rs.getInt("DURACION"));
			act.setMillas(rs.getInt("DISTANCIA"));
			respuesta.add(act);
		}

		return respuesta;
	}
	
	public List<InfoViaje> consultarTraficoEntre(String c1, String c2, String fechaI, String fechaF) throws SQLException{
		String sql = "SELECT * FROM VUELOS v NATURAL JOIN VUELOSPROGRAMADOS vp "
				+ "WHERE (v.CODIATA_AEROPUERTOPARTIDA = '" + c1 + "' OR v.CODIATA_AEROPUERTOSALIDA = '" + c1 +") "
						+ "AND (v.CODIATA_AEROPUERTOPARTIDA = '" + c2 + "' OR v.CODIATA_AEROPUERTOSALIDA = '" + c2 +")";

		if(!fechaI.isEmpty()){
			sql+=" AND vr.FECHA_SALIDA > TO_DATE(" + fechaI + ",'DD/MM/YYYY')";
		}
		if(!fechaF.isEmpty()){
			sql+=" AND vr.FECHA_LLEGADA < TO_DATE(" + fechaF + ",'DD/MM/YYYY')";
		}

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		List<InfoViaje> respuesta = new ArrayList<>();

		while(rs.next()){
			InfoViaje act = new InfoViaje();
			act.setOrigen(rs.getString("CODIATA_AEROPUERTOPARTIDA"));
			act.setDestino(rs.getString("CODIATA_AEROPUERTOLLEGADA"));
			act.setAerolinea(rs.getString("CODIATA_AEROLINEA"));
			act.setDuracion(rs.getInt("DURACION"));
			act.setMillas(rs.getInt("DISTANCIA"));
			respuesta.add(act);
		}

		return respuesta;
	}
}

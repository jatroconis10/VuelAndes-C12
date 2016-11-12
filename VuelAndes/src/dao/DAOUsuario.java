package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import vos.*;

public class DAOUsuario extends AbstractDAO {

	public DAOUsuario(){
		super();
	}

	private int darIdNuevoReservasViajes() throws SQLException,Exception{
		String sql;
		sql = "SELECT COUNT(*) FROM ISIS2304A141620.RESERVASVIAJES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		rs.next();
		return rs.getInt("COUNT(*)") + 1;
	}

	public Date darFechaReserva(Long idReserva) throws SQLException,Exception{

		String sql;
		sql = "SELECT FECHA FROM ISIS2304A141620.RESERVASPASAJEROS WHERE ID="+idReserva;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()){

			return rs.getDate("FECHA");
		}
		else{
			throw new Exception("No existe esa reserva");
		}

	}
	public Date darFechaReservaViaje(Long idReserva) throws SQLException,Exception{

		String sql;
		sql = "SELECT ID FROM ISIS2304A141620.RESERVASVIAJES WHERE ID="+idReserva;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()){

			sql = "SELECT MIN(FECHA) FROM ISIS2304A141620.RESERVASPASAJEROSDEVIAJE rv "
					+ "INNER JOIN ISIS2304A141620.RESERVASPASAJEROS rp ON rv.ID_RESERVAPASAJERO = rp.ID"
					+ " WHERE rv.ID_RESERVAVIAJE = " + idReserva + " GROUP BY rv.ID_RESERVAVIAJE";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			
			rs.next();
			return rs.getDate("MIN(FECHA)");
		}
		else{
			throw new Exception("No existe esa reserva");
		}

	}
	
	public List<Long> darIdReservasViaje(Long idReserva) throws SQLException,Exception{
		
		List<Long> respuesta = new ArrayList<>();
		String sql;
		sql = "SELECT ID_RESERVAPASAJERO FROM ISIS2304A141620.RESERVASPASAJEROSDEVIAJE WHERE ID_RESERVAVIAJE="+idReserva;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()){
			
			long reservaId = rs.getLong("ID_RESERVAPASAJERO");
			respuesta.add(idReserva);
		}
		return respuesta;
	}
	
	public int darTipoUsuario(Integer tipoId, Long id) throws SQLException, Exception{
		String sql;
		sql = "SELECT ROL FROM ISIS2304A141620.USUARIOS WHERE TIPOID=" + tipoId + 
				" AND ID_USUARIO=" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			return rs.getInt("ROL");
		}
		else{
			throw new Exception("No existe un usario con esas credenciales");
		}
	}

	public ReservasPasajeros hacerReservaPasajero(Integer tipoId, Long id, ReservasPasajeros r)  throws SQLException, Exception{

		String sql;
		sql = "SELECT ID_VUELO, AVION_ASIGNADO, COUNT(CASE WHEN EJECUTIVA='Y' THEN 1 END) AS EJEC, COUNT(CASE WHEN EJECUTIVA = 'N' THEN 1 END) AS ECON"
				+ " FROM ISIS2304A141620.VUELOS v LEFT OUTER JOIN ISIS2304A141620.RESERVASPASAJEROS r ON v.ID_VUELO=r.VUELO"
				+ " WHERE ID_VUELO = "+r.getIdVuelo()+" AND TIPO_VUELO=1 GROUP BY ID_VUELO,AVION_ASIGNADO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()){
			int ejec = rs.getInt("EJEC");
			int econ = rs.getInt("ECON");
			String avion = rs.getString("AVION_ASIGNADO");
			sql = "SELECT COUNT(*) AS RESERVASMAX FROM RESERVASPASAJEROS";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			rs.next();
			int idNuevo = rs.getInt("RESERVASMAX")+1;
			r.setId(idNuevo);
			if(avion != null){

				sql = "SELECT SILLAS_EJECUTIVAS,SILLAS_ECONOMICAS FROM ISIS2304A141620.AVIONES WHERE NUMERO_DE_SERIE = '"+ avion+"'";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				if(rs.next()){
					if(r.getEjecutiva()){
						if(rs.getInt("SILLAS_EJECUTIVAS") > ejec){
							sql = "INSERT INTO RESERVASPASAJEROS VALUES("+idNuevo+","+ tipoId + ","+id+"," + r.getIdVuelo() +",'Y')";
							prepStmt = conn.prepareStatement(sql);
							recursos.add(prepStmt);
							rs = prepStmt.executeQuery();
							return r;
						}
						else{
							throw new Exception("No hay suficentes sillas ejecutivas");
						}
					}else{
						if(rs.getInt("SILLAS_ECONOMICAS") > econ){
							sql = "INSERT INTO RESERVASPASAJEROS VALUES(" + idNuevo + ","+ tipoId + ","+id+"," + r.getIdVuelo() +",'N')";
							prepStmt = conn.prepareStatement(sql);
							recursos.add(prepStmt);
							rs = prepStmt.executeQuery();
							return r;
						}
						else{
							throw new Exception("No hay suficentes sillas econimicas");
						}
					}
				}
				else{
					if(r.getEjecutiva()){
						sql = "INSERT INTO RESERVASPASAJEROS VALUES(" + idNuevo + ","+ tipoId + ","+id+"," + r.getIdVuelo() +",'Y')";
					}else{
						sql = "INSERT INTO RESERVASPASAJEROS VALUES(" + idNuevo + ","+ tipoId + ","+id+"," + r.getIdVuelo() +",'N')";
					}
					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					rs = prepStmt.executeQuery();
					return r;
				}
			}
			else{
				throw new Exception("No existe un vuelo con ese id");
			}
		}
		else{
			throw new Exception("No existe un vuelo con ese id");
		}	
	}

	public void hacerReservaCarga(Integer tipoId, Long id, ReservasCargas r)  throws SQLException, Exception{

		String sql;
		sql = "SELECT ID_VUELO, COUNT(NVL(PESO,0))AS PESOTOT "
				+ "FROM ISIS2304A141620.VUELOS v "
				+ "LEFT OUTER JOIN ISIS2304A141620.RESERVASCARGAS r ON v.ID_VUELO=r.VUELO "
				+ "INNER JOIN ISIS2304A141620.CARGAS c ON r.CARGA = c.ID " + 
				" WHERE ID_VUELO = '"+r.getIdVuelo()+"' AND TIPO_VUELO=2 GROUP BY ID_VUELO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if(rs.next()){
			int pesoTot = rs.getInt("PESOTOT");
			sql = "SELECT COUNT(*) AS RESERVASMAX FROM RESERVASCARGAS";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			rs.next();
			int idNuevo = rs.getInt("RESERVASMAX")+1;
			sql = "SELECT CAPACIDAD FROM ISIS2304A141620.AVIONES WHERE NUMERO_DE_SERIE='"
					+ r.getIdVuelo() + "'";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			if(rs.next()){
				int cap = rs.getInt("CAPACIDAD");
				sql = "SELECT PESO FROM ISIS2304A141620.CARGAS WHERE ID=" + r.getTrackingNumber();
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				if(rs.next()){
					int pesoCarga = rs.getInt("PESO");
					if(pesoCarga + pesoTot <= cap){
						sql = "INSERT INTO RESERVASCARGAS VALUES(idNuevo,"+ tipoId + ","+id+"," + r.getTrackingNumber()+"," + r.getIdVuelo() +")";
					}	
				}
				else{
					throw new Exception("No existe ninguna carga con ese id");
				}
			}
			else{
				throw new Exception("No existe ningun avion con ese id");
			}
		}	
		else{
			throw new Exception("No existe un vuelo o carga con ese id");
		}
	}
	
	private WeightedPseudograph<Aeropuerto, Vuelo> crearGrafo(boolean tiempo, boolean escalas, boolean costo, boolean ejecutiva) throws Exception{
		
		WeightedPseudograph<Aeropuerto, Vuelo> grafo = new WeightedPseudograph<>(new ClassBasedEdgeFactory<Aeropuerto, Vuelo>(Vuelo.class));
		DAOAeropuerto daoAeropuerto = new DAOAeropuerto();
		daoAeropuerto.setConn(conn);
		
		DAOVuelos daoVuelos = new DAOVuelos();
		daoVuelos.setConn(conn);
		
		List<Aeropuerto> aeropuertos = daoAeropuerto.darAeropuertos();
		
		for(Aeropuerto a: aeropuertos){
			grafo.addVertex(a);
		}
		
		List<Vuelo> vuelos = daoVuelos.darVuelos();
		for(Vuelo v: vuelos){
			
			grafo.addEdge(daoAeropuerto.darAeropuerto(v.getAeroPuertoSalida()), 
					daoAeropuerto.darAeropuerto(v.getAeroPuertoLlegada()), v);
			double costoArco = 1.0;
			
			if(tiempo){
				costoArco+=v.getDuracion();
			}
			if(escalas){
				costoArco++;
			}
			if(costo){
				costoArco += daoVuelos.darCostoVueloPasajero(v.getIdVuelo(), ejecutiva);
			}
			
			grafo.setEdgeWeight(v, costoArco);
		}
		
		return grafo;
		
	}
	
	public ReservaViaje hacerReservaPasajero(SolicitudReservaPasajero sr, int tipoId, long id) throws SQLException, Exception{
		
		WeightedPseudograph<Aeropuerto, Vuelo> g = crearGrafo(sr.isTiempo(), sr.isEscalas(), sr.isCosto(), sr.isEjecutiva());
		DAOAeropuerto daoAeropuerto = new DAOAeropuerto();
		daoAeropuerto.setConn(conn);
		
		DAOVuelos daoVuelos = new DAOVuelos();
		daoVuelos.setConn(conn);
		
		DijkstraShortestPath<Aeropuerto, Vuelo> diShortestPath = new DijkstraShortestPath<Aeropuerto, Vuelo>(g, daoAeropuerto.darAeropuerto(sr.getOrigen()), 
				daoAeropuerto.darAeropuerto(sr.getDestino()));
		
		List<Vuelo> camino = diShortestPath.getPathEdgeList();
		
		if(!camino.isEmpty()){
			conn.setAutoCommit(false);
			try{
				long idReservaViaje = darIdNuevoReservasViajes();
				ReservaViaje r = new ReservaViaje(idReservaViaje, tipoId, id, new ArrayList<Long>(), new ArrayList<Long>());
				String sql = "INSERT INTO ISIS2304A141620.RESERVASVIAJES VALUES(" + idReservaViaje +"," 
				+ tipoId + "," + id + ")";
				List<Long> reservas = new ArrayList<>();
				ReservasPasajeros rp;
				
				for(Vuelo v: camino){
					
					rp = new ReservasPasajeros(null, tipoId, id, v.getIdVuelo(), sr.isEjecutiva());
					ReservasPasajeros rn = hacerReservaPasajero(tipoId, id, rp);
					reservas.add(new Long(rn.getId()));
				}
				r.setReservasPasajeros(reservas);
				conn.commit();
				return  r;
			}
			catch(Exception e){
				conn.rollback();
				throw e;
			}
			
		}
		else{
			throw new Exception("Lamentablemente no existen vuelos para llegar a su destino");
		}
		
	}
	public void cancelarReservaVuelo(Long idReserva, int tipoId, Long id) throws SQLException, Exception{
		conn.setAutoCommit(false);
		String sql;
		sql = "SELECT ID FROM ISIS2304A141620.RESERVASPASAJEROS WHERE ID=" + idReserva
				+ " AND TIPO_ID=" + tipoId +" AND ID_USUARIO=" + id ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			sql = "DELETE FROM RESERVASPASAJEROS WHERE ID="+ idReserva;
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs = prepStmt.executeQuery();
			conn.commit();
		}
		else{
			throw new Exception("No tiene una reserva con ese id");
		}
		
		
	}
	
	public void cancelarReservaViaje(Long idReserva, int tipoId, Long id) throws SQLException, Exception{
		conn.setAutoCommit(false);
		String sql;
		sql = "SELECT ID FROM ISIS2304A141620.RESERVASVIAJES WHERE ID=" + idReserva
				+ " AND TIPO_ID=" + tipoId +" AND ID_USUARIO=" + id ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		if(rs.next()){
			List<Long> reservasParaCancelar = darIdReservasViaje(idReserva);
			try{
				for(Long r: reservasParaCancelar){

					cancelarReservaVuelo(idReserva, tipoId, id);
				}
				conn.commit();
			}
			catch(Exception e){
				conn.rollback();
				throw e;
			}
		}
		else{
			throw new Exception("No tiene una reserva con ese id");
		}
		
	}
}


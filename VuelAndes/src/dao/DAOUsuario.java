package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

public class DAOUsuario extends AbstractDAO {

	public DAOUsuario(){
		super();
	}

	public void hacerReservaPasajero(Integer tipoId, Integer id, ReservasPasajeros r)  throws SQLException, Exception{

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
						}
						else{
							throw new Exception("No hay suficentes sillas econimicas");
						}
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
			}
		}
		else{
			throw new Exception("No existe un vuelo con ese id");
		}
		
	}

	public void hacerReservaCarga(Integer tipoId, Integer id, ReservasCargas r)  throws SQLException, Exception{

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
}


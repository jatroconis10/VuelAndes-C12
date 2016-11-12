package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

public class DAOAeropuerto extends AbstractDAO {
	
	public DAOAeropuerto(){
		super();
	}
	
	public List<Aeropuerto> darAeropuertos() throws SQLException, Exception{
		
		String sql;
		sql = "SELECT * FROM ISIS2304A141620.AEROPUERTOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		List<Aeropuerto> lista = new ArrayList<>();
		
		while(rs.next()){
			String codIata = rs.getString("CODIATA");
			String nombre = rs.getString("NOMBRE");
			String ciudad = rs.getString("CIUDAD");
			int tipo = rs.getInt("CLASIFICACION");
			
			Aeropuerto act = new Aeropuerto(Aeropuerto.TipoAeropuerto.INTERNACIONAL, nombre, codIata);
			lista.add(act);
		}
		
		return lista;
	}
	
	public Aeropuerto darAeropuerto(String codIata) throws SQLException,Exception{
		String sql;
		sql = "SELECT * FROM ISIS2304A141620.AEROPUERTOS WHERE CODIATA='" + codIata+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		Aeropuerto a = null;
		
		if(rs.next()){
			String nombre = rs.getString("NOMBRE");
			String ciudad = rs.getString("CIUDAD");
			int tipo = rs.getInt("CLASIFICACION");
			
			Aeropuerto act = new Aeropuerto(Aeropuerto.TipoAeropuerto.INTERNACIONAL, nombre, codIata);
			a = act;
		}
		else{
			throw new Exception("No existe ese aeropuerto");
		}
		
		return a;
	}
}

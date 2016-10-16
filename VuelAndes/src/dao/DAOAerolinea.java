package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Usuario;

public class DAOAerolinea extends AbstractDAO {
	
	public DAOAerolinea(){
		super();
	}

	public void consultarInformacionAerolinea(String a, int rol) throws Exception {
		if(rol == Usuario.TipoUsuario.CLIENTE.ordinal())
		{
//			el gerente del aeropuerto
//			podr� ver la informaci�n de todos los viajes que �tocan� su aeropuerto 
			String sql = "SELECT * FROM ISIS2304A141620.VUELOS WHERE NUMERO_DE_SERIE = '"+a+"'";

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();
			throw new Exception("No existe una aerolinea con ese id");
			


		}
			//cargas
		else if(rol == Usuario.TipoUsuario.AEROLINEA.ordinal())
		{
//			usuario aerol�nea hace esta consulta, 
//			la respuesta s�lo contiene la informaci�n de sus vuelos 
			
			
			throw new Exception("No existe una aerolinea con ese id");
		}
		else if (rol == Usuario.TipoUsuario.VUELANDES.ordinal())
		{
//			el gerente de VuelAndes puede ver toda la informaci�n.
			
//			El resultado incluye toda la informaci�n de una Aerol�nea, 
//			incluyendo sus datos b�sicos, 
//			al igual que el n�mero de veces que ha utilizado VuelAndes 
//			(vuelos realizados) y el detalle de los pasajeros y las cargas transportadas. 
//			Los resultados deben poder ser filtrados por tipo de vuelo (pasajeros o carga), 
//			caracter�sticas de las aeronaves, carga involucrada y en un rango de fechas, entre otros. 
//			Debe ofrecerse la posibilidad de agrupamiento y ordenamiento de las respuestas seg�n 
//			los intereses del usuario que consulta.
//			NOTA: Respetando la privacidad de los usuarios, cuando un 
			
			
			throw new Exception("No existe una aerolinea con ese id");
		
	}
	}
}
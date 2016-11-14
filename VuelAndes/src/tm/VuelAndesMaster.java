package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import dao.*;
import vos.*;

public class VuelAndesMaster {
	
	/**
	 * Atributo estático que contiene el path relativo del archivo que tiene los datos de la conexión
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estático que contiene el path absoluto del archivo que tiene los datos de la conexión
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;
	
	/**
	 * Conexión a la base de datos
	 */
	private Connection conn;
	
	/**
	 * Método constructor de la clase VideoAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logia de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto VideoAndesMaster, se inicializa el path absoluto de el archivo de conexión y se
	 * inicializa los atributos que se usan par la conexión a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public VuelAndesMaster(String contextPathP) {
		System.out.println(contextPathP);
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}
	
	/*
	 * Método que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexión a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que  retorna la conexión a la base de datos
	 * @return Connection - la conexión a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexión a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////
	
	public void asociarAeronaveAVuelo(Long v, String a)throws Exception{
		
		DAOVuelos dao = new DAOVuelos();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			dao.asociarAeronaveAVuelo(v,a);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public ReservaViaje hacerReservaP(SolicitudReservaPasajero sr, int tipoId, long idU) throws Exception{
		DAOUsuario dao = new DAOUsuario();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			int rolU = dao.darTipoUsuario(tipoId, idU);
			if(rolU >= 1 && rolU <= 3){
				return dao.hacerReservaPasajero(sr, tipoId, idU);
			}
			else{
				throw new Exception("No se tiene permiso para realizar la resrva");
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}
	
	public void hacerReservaP(ReservasPasajeros r) throws Exception{
		
		DAOUsuario dao = new DAOUsuario();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			dao.hacerReservaPasajero(r.getTipoId(), r.getIdUsuario(), r);;

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void hacerReservaC(ReservasCargas r) throws Exception{

		DAOUsuario dao = new DAOUsuario();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			dao.hacerReservaCarga(r.getTipoId(),new Long(r.getIdUsuario()),r);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public String cancelarReservaVuelo( Long idReserva,int tipoId, Long id) throws Exception{

		DAOUsuario dao = new DAOUsuario();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			
			Date fechaActual = Calendar.getInstance().getTime();
			Date fechaReserva = dao.darFechaReserva(idReserva);
			if(fechaReserva.getTime()-fechaActual.getTime() > 8.64e7){
			
				dao.cancelarReservaVuelo(idReserva,tipoId,id);
				return "La reserva se cancelo correctamente";
			}
			else{
				throw new Exception("No puede cancelar una reserva si faltan menos de 24 horas para su vuelo");
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public String cancelarReservaViaje( Long idReserva,int tipoId, Long id) throws Exception{

		DAOUsuario dao = new DAOUsuario();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			
			Date fechaActual = Calendar.getInstance().getTime();
			Date fechaViaje = dao.darFechaReservaViaje(idReserva);
			if(fechaViaje.getTime()-fechaActual.getTime() > 8.64e7){
			
				dao.cancelarReservaViaje(idReserva,tipoId,id);
				return "La reserva se cancelo correctamente";
			}
			else{
				throw new Exception("No puede cancelar una reserva si faltan menos de 24 horas para su vuelo");
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarVueloRealizado(Long id, String salida, String llegada) throws SQLException {
		DAOVuelos dao = new DAOVuelos();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			dao.registrarVueloRealizado(id,salida,llegada);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public Double generarReporte(Long id) throws Exception {
		DAOVuelos dao = new DAOVuelos();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			return dao.generarReporte(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public ArrayList<Vuelo> consultarIntinerario(String a,String fechaI,String fechaF,String aero,int tipo, String hSalida,String hLlegada) throws SQLException {
		DAOVuelos dao = new DAOVuelos();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			return dao.consultarIntinerario(a, fechaI, fechaF, aero,tipo, hSalida, hLlegada);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public ArrayList<Vuelo> consultarIntinerarioNoCumple(String a,String fechaI,String fechaF,String aero,int tipo, String hSalida,String hLlegada) throws SQLException {
		DAOVuelos dao = new DAOVuelos();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			return dao.consultarIntinerario(a, fechaI, fechaF, aero, tipo, hSalida, hLlegada);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void consultarInformacionAerolinea(String a, int rol) throws SQLException,Exception {
		DAOAerolinea dao = new DAOAerolinea();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			dao.consultarInformacionAerolinea(a,rol);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public ReporteCancelacion cancelarViaje(Long idViaje, int tipoId, Long idUsuario)throws SQLException,Exception{
		DAOAerolinea dao = new DAOAerolinea();
		try 
		{
			//////Transacción
			this.conn = darConexion();
			dao.setConn(conn);
			return null;

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<InfoViaje> consultarViajes(int tipoId, long idUsario, int millas, String fechaI, String fechaF) throws SQLException,Exception {
		DAOVuelos daoV = new DAOVuelos();
		DAOUsuario daoU = new DAOUsuario();
		
		try 
		{
			//////Transacción
			this.conn = darConexion();
			daoV.setConn(conn);
			daoU.setConn(conn);
			
			int rolU = daoU.darTipoUsuario(tipoId, idUsario);
			if(rolU == 1){
				return daoV.consultarViajesGenerales(millas, fechaI, fechaF);
			}
			else if(rolU == 3){
				return daoV.consultarViajesUsuario(tipoId, idUsario, millas, fechaI, fechaF);
			}
			else{
				throw new Exception("No tiene permisos para realizar esta accion");
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoU.cerrarRecursos();
				daoV.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<InfoViaje> consultarTraficoEntre(int tipoId, long idUsario,String c1, String c2, String fechaI, String fechaF) throws SQLException,Exception {
		DAOVuelos daoV = new DAOVuelos();
		DAOUsuario daoU = new DAOUsuario();
		
		try 
		{
			//////Transacción
			this.conn = darConexion();
			daoV.setConn(conn);
			daoU.setConn(conn);
			
			int rolU = daoU.darTipoUsuario(tipoId, idUsario);
			if(rolU == 1){
				return daoV.consultarTraficoEntre(c1, c2, fechaI, fechaF);
			}
			else{
				throw new Exception("No tiene permisos para realizar esta accion");
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoU.cerrarRecursos();
				daoV.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void hacerCommit() throws SQLException, Exception{
		conn.commit();
	}
}

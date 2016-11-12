package vos;

public abstract class Reservas {
	
	private Integer id;
	
	private Integer tipoId;

	private Long idUsuario;
	
	private Long idVuelo;

	
	public Reservas(Integer id, Integer tipoId, Long idUsuario, Long idVuelo) {
		super();
		this.id = id;
		this.tipoId = tipoId;
		this.idUsuario = idUsuario;
		this.idVuelo = idVuelo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTipoId() {
		return tipoId;
	}

	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdVuelo() {
		return idVuelo;
	}

	public void setIdVuelo(Long idVuelo) {
		this.idVuelo = idVuelo;
	}
}

package uy.com.agm.gaston.persistencia.excepciones;

public class DaoException extends Exception {
	private static final long serialVersionUID = 1L;

	public DaoException() {
		super("Error en persistencia");
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable t) {
		super(t);
	}

	public DaoException(String message, Throwable t) {
		super(message, t);
	}
}

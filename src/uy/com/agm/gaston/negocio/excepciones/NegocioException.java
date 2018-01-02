package uy.com.agm.gaston.negocio.excepciones;

public class NegocioException extends Exception {
	private static final long serialVersionUID = 1L;

	public NegocioException() {
		super("Error en persistencia");
	}

	public NegocioException(String message) {
		super(message);
	}

	public NegocioException(Throwable t) {
		super(t);
	}

	public NegocioException(String message, Throwable t) {
		super(message, t);
	}
}

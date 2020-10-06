package loteria.exceptions;

public class ApostaInvalidaException extends Exception{


	private static final long serialVersionUID = 1L;
	private String message;
	
	public ApostaInvalidaException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}

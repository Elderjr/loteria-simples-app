package loteria.exceptions;

/**
 * Exceção lançada caso:
 * <ul>
 * <li>loteria atual registrada no repositório ainda está aguardando novas apostas e foi realizada uma tentativa de iniciar outra loteria</li>
 * <li>loteria está em estado de finalizada e é feito uma tentativa para finalizar ela novamente</li>
 * </ul>
 * @author elderjr
 *
 */
public class EstadoLoteriaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public EstadoLoteriaException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}

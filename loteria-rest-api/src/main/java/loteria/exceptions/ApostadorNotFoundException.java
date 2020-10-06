package loteria.exceptions;

public class ApostadorNotFoundException extends Exception{


	private static final long serialVersionUID = 1L;
	private Integer id;
	
	public ApostadorNotFoundException(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getMessage() {
		return "Não foi possível encontrar o apostador com id = " + id;
	}
}

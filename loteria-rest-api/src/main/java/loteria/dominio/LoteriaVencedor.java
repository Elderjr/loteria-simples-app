package loteria.dominio;

public class LoteriaVencedor {

	public static enum Premio {
		SENA, QUINA, QUADRA;
	}
	
	private Premio premio;
	private Apostador apostador;
	
	
	public LoteriaVencedor(Apostador apostador, Premio premio) {
		this.premio = premio;
		this.apostador = apostador;
	}
	
	public Premio getPremio() {
		return this.premio;
	}
	
	public Apostador getApostador() {
		return this.apostador;
	}
}

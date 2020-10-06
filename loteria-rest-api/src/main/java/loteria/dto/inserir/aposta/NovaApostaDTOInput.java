package loteria.dto.inserir.aposta;



public class NovaApostaDTOInput {

	
	private Integer idApostador;

	private String nomeApostador;
	
	private int[] numerosApostados;

	public NovaApostaDTOInput(Integer idApostador, String nomeApostador, int numerosApostados[]) {
		this.idApostador = idApostador;
		this.nomeApostador = nomeApostador;
		this.numerosApostados = numerosApostados;
	}
	
	public Integer getIdApostador() {
		return idApostador;
	}

	public void setIdApostador(Integer idApostador) {
		this.idApostador = idApostador;
	}

	public String getNomeApostador() {
		return nomeApostador;
	}

	public void setNomeApostador(String nomeApostador) {
		this.nomeApostador = nomeApostador;
	}

	public int[] getNumerosApostados() {
		return numerosApostados;
	}

	public void setNumerosApostados(int[] numerosApostados) {
		this.numerosApostados = numerosApostados;
	}
	
	

}

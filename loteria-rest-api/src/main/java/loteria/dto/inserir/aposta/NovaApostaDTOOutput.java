package loteria.dto.inserir.aposta;

import loteria.dominio.Aposta;

public class NovaApostaDTOOutput {

	
	private Integer idApostador;
	
	private Integer idAposta;
	
	private String nomeApostador;
	
	private int[] numerosApostados;

	//Para propositos de conversão para JSON
	public NovaApostaDTOOutput() {
		
	}
	
	public NovaApostaDTOOutput(Integer idApostador, String nomeApostador, Integer idAposta, int numerosApostados[]) {
		this.idApostador = idApostador;
		this.nomeApostador = nomeApostador;
		this.idAposta = idAposta;
		this.numerosApostados = numerosApostados;
	}
	
	public NovaApostaDTOOutput(Aposta aposta) {
		this.idApostador = aposta.getApostador().getId();
		this.nomeApostador = aposta.getApostador().getNome();
		this.idAposta = aposta.getId();
		this.numerosApostados = aposta.getNumerosApostados();
	}
	
	public Integer getIdApostador() {
		return idApostador;
	}


	public Integer getIdAposta() {
		return idAposta;
	}

	public String getNomeApostador() {
		return nomeApostador;
	}

	public int[] getNumerosApostados() {
		return numerosApostados;
	}
	

}

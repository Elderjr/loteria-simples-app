package loteria.dto.buscar.apostadores;

import java.time.LocalDateTime;

import loteria.dominio.Aposta;

public class ApostaDTO {
	
	private Integer id;
	private int[] numerosApostados;
	private LocalDateTime dataCriacao;
	
	public ApostaDTO(Aposta aposta) {
		this.id = aposta.getId();
		this.numerosApostados = aposta.getNumerosApostados();
		this.dataCriacao = aposta.getDataCriacao();
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public int[] getNumerosApostados() {
		return this.numerosApostados;
	}
	
	public LocalDateTime getDataCriacao() {
		return this.dataCriacao;
	}
}

package loteria.dominio;

import java.time.LocalDateTime;

import loteria.dominio.LoteriaVencedor.Premio;

public class Aposta {
	
	private Integer id;
	private int[] numerosApostados;
	private LocalDateTime dataCriacao;
	private Apostador apostador;
	
	public Aposta(int[] numerosApostados, LocalDateTime dataAposta) {
		this.numerosApostados = numerosApostados;
		this.dataCriacao = dataAposta;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	/* package */ void setApostador(Apostador apostador){
		this.apostador = apostador;
	}
	
	public Apostador getApostador() {
		return this.apostador;
	}
	
	public Premio verificarPremio(int numerosSorteados[]) {
		int qtdNumerosIguais = 0;
		for (int i = 0; i < numerosSorteados.length; i++) {
			for(int j = 0; j < numerosApostados.length; j++) {
				if (numerosSorteados[i] == numerosApostados[j]) {
					qtdNumerosIguais++;
					break;
				}	
			}
		}
		switch (qtdNumerosIguais) {
		case 6:
			return LoteriaVencedor.Premio.SENA;
		case 5:
			return LoteriaVencedor.Premio.QUINA;
		case 4:
			return LoteriaVencedor.Premio.QUADRA;
		default:
			return null;
		}
	}
	
}

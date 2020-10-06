package loteria.dto.resultadosorteio;

import loteria.dominio.LoteriaVencedor;
import loteria.dominio.LoteriaVencedor.Premio;

public class LoteriaVencedorDTO {

	private String nome;
	
	private Integer id;
	
	private Premio premio;

	public LoteriaVencedorDTO(LoteriaVencedor vencedor) {
		this.id = vencedor.getApostador().getId();
		this.nome = vencedor.getApostador().getNome();
		this.premio = vencedor.getPremio();
	}
	
	public String getNome() {
		return this.nome;
	}

	public Integer getId() {
		return this.id;
	}

	public Premio getPremio() {
		return this.premio;
	}
	
	
}

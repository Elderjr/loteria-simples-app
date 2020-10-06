package loteria.dto.resultadosorteio;

import loteria.dominio.Loteria;
import loteria.dominio.LoteriaFinalizada;

public class LoteriaDTO {

	private String nome;
	
	private ResultadoLoteriaDTO resultado;
	
	public LoteriaDTO() {
		
	}
	
	public LoteriaDTO(Loteria loteria) {
		this.nome = loteria.getNome();
		if(loteria.isEncerrado()) {
			resultado = new ResultadoLoteriaDTO(((LoteriaFinalizada) loteria.getEstadoLoteria()).getResultado());
		}
	}

	public String getNome() {
		return nome;
	}
	
	public ResultadoLoteriaDTO getResultado() {
		return resultado;
	}
	
	
	
}

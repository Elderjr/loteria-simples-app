package loteria.dominio;


import java.util.List;

import loteria.exceptions.EstadoLoteriaException;

public class LoteriaFinalizada extends EstadoLoteria {

	private ResultadoLoteria resultado;

	public LoteriaFinalizada(ResultadoLoteria resultado) {
		this.resultado = resultado;
	}
	
	public ResultadoLoteria getResultado() {
		return this.resultado;
	}
	
	@Override
	public boolean isEncerrado() {
		return true;
	}

	@Override
	/* package */ ResultadoLoteria finalizarSorteio(List<Apostador> apostadores) {		
		throw new EstadoLoteriaException("O sorteio ja foi finalizado");
	}

}

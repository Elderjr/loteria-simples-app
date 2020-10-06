package loteria.dominio;

import java.util.List;
import java.util.stream.Stream;

public class ResultadoLoteria {

	private int numerosSorteados[];
	
	private List<LoteriaVencedor> vencedores;
	
	
	public ResultadoLoteria(int resultado[], List<LoteriaVencedor> vencedores){
		this.numerosSorteados = resultado;
		this.vencedores = vencedores;
	}
	
	public int[] getNumerosSorteados() {
		return this.numerosSorteados;
	}
	
	public Stream<LoteriaVencedor> getVencedores() {
		return this.vencedores.stream();
	}
}

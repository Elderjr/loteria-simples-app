package loteria.dto.resultadosorteio;

import java.util.List;
import java.util.stream.Collectors;

import loteria.dominio.ResultadoLoteria;

public class ResultadoLoteriaDTO {

	private int numerosSorteados[];
	private List<LoteriaVencedorDTO> vencedores;

	// construtor vazio para fins de serializacao/deserializacao JSON
	public ResultadoLoteriaDTO() {

	}

	public ResultadoLoteriaDTO(ResultadoLoteria resultado) {
		this.numerosSorteados = resultado.getNumerosSorteados();
		this.vencedores = resultado.getVencedores().map(LoteriaVencedorDTO::new).collect(Collectors.toList());

	}

	public int[] getNumerosSorteados() {
		return numerosSorteados;
	}

	public List<LoteriaVencedorDTO> getVencedores() {
		return vencedores;
	}

}

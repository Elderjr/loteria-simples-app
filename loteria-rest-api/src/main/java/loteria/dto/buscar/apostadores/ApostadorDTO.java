package loteria.dto.buscar.apostadores;

import java.util.List;
import java.util.stream.Collectors;

import loteria.dominio.Apostador;

public class ApostadorDTO {

	private Integer id;
	
	private String nome;
	
	private List<ApostaDTO> apostas;
	
	public ApostadorDTO() {
		
	}
	
	public ApostadorDTO(Apostador apostador) {
		this.id = apostador.getId();
		this.nome = apostador.getNome();
		this.apostas = apostador.getApostas()
				.map(aposta -> new ApostaDTO(aposta))
				.collect(Collectors.toList());
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
		
	public List<ApostaDTO> getApostas() {
		return this.apostas;
	}

}

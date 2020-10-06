package loteria.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import loteria.dominio.LoteriaVencedor.Premio;

public class Apostador {

	private Integer id;
	
	private String nome;
	
	private List<Aposta> apostas;
	
	public Apostador(String nome) {
		this.nome = nome;
		this.apostas = new ArrayList<>();
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
		
	public Stream<Aposta> getApostas() {
		return this.apostas.stream();
	}
	
	public void adicionarAposta(Aposta aposta) {
		this.apostas.add(aposta);
		aposta.setApostador(this);
	}
	
	public Premio verificarPremio(int numerosSorteados[]) {
		Premio premio = null;
		for(Aposta aposta : apostas) {
			Premio premioTmp = aposta.verificarPremio(numerosSorteados);
			if(premioTmp != null && premioTmp.equals(Premio.SENA)) {
				//caso o prêmio seja SENA, então é o maior prêmio e pode ser retornado direto
				return premioTmp;
			} else if(premio == null || (premioTmp != null && premioTmp.equals(Premio.QUINA))) {
				//caso prêmio seja nulo, então atribuimos o premio atual OU
				//caso premio não seja nulo (isso significa que é QUADRA ou QUINA) e o premioTmp seja QUINA
				//então atribuimos o premio QUINA
				premio = premioTmp;
			}
		}
		return premio;
	}

}

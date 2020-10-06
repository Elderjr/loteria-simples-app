package loteria.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Loteria {
	
	private String nome;
	private List<Apostador> apostadores;
	private EstadoLoteria estadoLoteria; //encerrada ou aguardando apostas
	
	public Loteria(String nome) {
		this.nome = nome;
		this.apostadores = new ArrayList<>();
		this.estadoLoteria = new LoteriaAguardandoApostas();
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public Stream<Apostador> getApostadores(){
		return this.apostadores.stream();
	}
	
	public int getTotalApostadores() {
		return this.apostadores.size();
	}
	
	public void adicionarApostador(Apostador apostador) {
		this.apostadores.add(apostador);
	}
	
	public EstadoLoteria getEstadoLoteria() {
		return this.estadoLoteria;
	}
	
	public boolean isEncerrado() {
		return estadoLoteria.isEncerrado();
	}
	
	public ResultadoLoteria finalizarSorteio() {
		ResultadoLoteria resultado = estadoLoteria.finalizarSorteio(this.apostadores);
		this.estadoLoteria = new LoteriaFinalizada(resultado);
		return resultado;
	}
	
	
}

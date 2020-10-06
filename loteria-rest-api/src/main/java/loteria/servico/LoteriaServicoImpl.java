package loteria.servico;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import loteria.dominio.Aposta;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.dominio.ResultadoLoteria;
import loteria.exceptions.ApostaInvalidaException;
import loteria.exceptions.ApostadorNotFoundException;
import loteria.exceptions.EstadoLoteriaException;
import loteria.repositorio.LoteriaRepositorio;
import loteria.simplepage.SimplePage;

@Component
public class LoteriaServicoImpl implements LoteriaServico {

	private LoteriaRepositorio loteriaRepositorio;

	@Autowired
	public LoteriaServicoImpl(LoteriaRepositorio megasenaRepository) {
		this.loteriaRepositorio = megasenaRepository;
	}

	@Override
	public Loteria obterLoteriaAtual() {
		return this.loteriaRepositorio.obterLoteria();
	}
	
	@Override
	public void vincularApostaAoApostador(int idApostador, Aposta aposta) throws ApostadorNotFoundException, ApostaInvalidaException {
		validarAposta(aposta);
		Apostador apostador = loteriaRepositorio.obterApostadorPorId(idApostador)
				.orElseThrow(() -> new ApostadorNotFoundException(idApostador));
		apostador.adicionarAposta(aposta);
		Loteria loteria = loteriaRepositorio.obterLoteria();
		//Salva a loteria e suas composições (a atribuição de id para a aposta é feita)
		//não é necessário adicionar o apostador a loteria, pois ele já foi registrado e o banco
		//é em memória
		loteriaRepositorio.salvarLoteria(loteria);
	}

	@Override
	public void vincularApostaAoApostador(String nomeApostador, Aposta aposta) throws ApostaInvalidaException {
		validarAposta(aposta);
		Apostador apostador = new Apostador(nomeApostador);
		apostador.adicionarAposta(aposta);
		Loteria loteria = loteriaRepositorio.obterLoteria();
		loteria.adicionarApostador(apostador);
		//Salva a loteria e suas composições (as atribuições de id para a aposta e apostador são feitas)
		loteriaRepositorio.salvarLoteria(loteria);
	}
	
	@Override
	public SimplePage<Apostador> buscarApostadores(int pagina, int itensPorPagina) {
		return loteriaRepositorio.buscarApostadores(pagina, itensPorPagina);
	}

	@Override
	public void iniciarNovaLoteria(Loteria loteria) {
		Loteria loteriaAtual = loteriaRepositorio.obterLoteria();
		if(!loteriaAtual.isEncerrado()) {
			throw new EstadoLoteriaException("A loteria atual ainda nao foi finalizada");
		}
		loteriaRepositorio.salvarLoteria(loteria);
	}

	@Override
	public ResultadoLoteria finalizarLoteria() {
		Loteria loteriaAtual = loteriaRepositorio.obterLoteria();
		return loteriaAtual.finalizarSorteio();
	}
	
	private static void validarAposta(Aposta aposta) throws ApostaInvalidaException {
		if(aposta.getNumerosApostados() == null) {
			throw new  ApostaInvalidaException("Ao adicionar uma nova aposta, informe os números apostados");
		} else if(aposta.getNumerosApostados().length != 6) {
			throw new  ApostaInvalidaException("A aposta deve conter seis números");
		}
		validarNumerosApostados(aposta.getNumerosApostados());
	}
	
	/**
	 * O vetor <code>numerosApostados</code> deve seguir as seguintes regras
	 * <ul>
	 * <li>Os números escolhidos devem estar entre 1 e 60</li>
	 * <li>Os números escolhidos não podem ser repetidos</li>
	 * </ul>
	 * @throws ApostaInvalidaException 
	 */
	private static void validarNumerosApostados(int numerosApostados[]) throws ApostaInvalidaException {
		for(int i = 0; i < numerosApostados.length; i++) {
			if(numerosApostados[i] < 1 || numerosApostados[i] > 60) {
				throw new  ApostaInvalidaException("Os números das apostas devem ser entre 1 e 60 (incluso 1 e 60). Encontrado: " + numerosApostados[i]);
			}
			for(int j = 0; j < numerosApostados.length; j++) {
				if(i != j && numerosApostados[i] == numerosApostados[j]) {
					throw new  ApostaInvalidaException("A aposta não pode conter números repetidos (o número " + numerosApostados[i] + " se repete)");
				}
			}
		}
	}



}

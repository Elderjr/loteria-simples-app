package loteria.servico;


import loteria.dominio.Aposta;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.dominio.ResultadoLoteria;
import loteria.exceptions.ApostaInvalidaException;
import loteria.exceptions.ApostadorNotFoundException;
import loteria.simplepage.SimplePage;

public interface LoteriaServico {

	public void vincularApostaAoApostador(int idApostador, Aposta aposta) throws ApostadorNotFoundException, ApostaInvalidaException;
	
	public void vincularApostaAoApostador(String nomeApostador, Aposta aposta) throws ApostaInvalidaException;
	
	public SimplePage<Apostador> buscarApostadores(int pagina, int itensPorPagina);
	
	public void iniciarNovaLoteria(Loteria loteria);
	
	public ResultadoLoteria finalizarLoteria();
	
	public Loteria obterLoteriaAtual();
	
}

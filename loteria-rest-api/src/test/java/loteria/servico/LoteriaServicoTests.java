package loteria.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import loteria.dominio.Aposta;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.exceptions.ApostaInvalidaException;
import loteria.exceptions.ApostadorNotFoundException;
import loteria.exceptions.EstadoLoteriaException;
import loteria.repositorio.LoteriaRepositorio;
import loteria.repositorio.LoteriaRepositorioImpl;

public class LoteriaServicoTests {
	
	private LoteriaServico obterServicoLoteria() {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("MegaSena");
		loteriaRepositorio.salvarLoteria(loteria);
		LoteriaServico loteriaServico = new LoteriaServicoImpl(loteriaRepositorio);
		return loteriaServico;
	}
	
	@Test
	public void teste_vincular_aposta_a_apostador() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {1, 2, 3, 4, 5, 6}, null);
		loteriaServico.vincularApostaAoApostador("Elder", apostaCorreta);
		//Ao vincular uma aposta ao apostador, a relação 1:1 de aposta com apostador é feita
		Apostador apostador = apostaCorreta.getApostador();
		assertNotNull(apostaCorreta.getApostador());
		assertEquals("Elder", apostaCorreta.getApostador().getNome());
		
		Aposta apostaCorreta2 = new Aposta(new int[] {1, 2, 3, 4, 5, 7}, null);
		loteriaServico.vincularApostaAoApostador(apostador.getId(), apostaCorreta2);
		
		assertEquals(2, apostador.getApostas().count());
		assertTrue(apostador.getApostas().anyMatch(aposta -> aposta == apostaCorreta));
		assertTrue(apostador.getApostas().anyMatch(aposta -> aposta == apostaCorreta2));
	}
	
	@Test
	public void teste_vincular_aposta_a_apostador_nao_encontrado() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {1, 2, 3, 4, 5, 6}, null);
		assertThrows(ApostadorNotFoundException.class, () -> {
			loteriaServico.vincularApostaAoApostador(1, apostaCorreta);	
		});
	}
	
	@Test
	public void teste_vincular_aposta_com_numeros_repetidos_nao_deve_ser_permitido() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {1, 2, 3, 4, 6, 6}, null);
		assertThrows(ApostaInvalidaException.class, () -> {
			loteriaServico.vincularApostaAoApostador("Elder", apostaCorreta);	
		});
	}
	
	@Test
	public void teste_vincular_aposta_com_numeros_menores_que_um_nao_deve_ser_permitido() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {0, 2, 3, 4, 5, 6}, null);
		assertThrows(ApostaInvalidaException.class, () -> {
			loteriaServico.vincularApostaAoApostador("Elder", apostaCorreta);	
		});
	}
	
	@Test
	public void teste_vincular_aposta_com_numeros_maiores_que_sessenta_nao_deve_ser_permitido() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {1, 2, 3, 4, 5, 61}, null);
		assertThrows(ApostaInvalidaException.class, () -> {
			loteriaServico.vincularApostaAoApostador("Elder", apostaCorreta);	
		});
	}
	
	@Test
	public void teste_vincular_aposta_com_menos_de_seis_numeros_nao_deve_ser_permitido() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		Aposta apostaCorreta = new Aposta(new int[] {1, 2, 3, 4, 5}, null);
		assertThrows(ApostaInvalidaException.class, () -> {
			loteriaServico.vincularApostaAoApostador("Elder", apostaCorreta);	
		});
	}
	
	@Test
	public void teste_iniciar_loteria_sequencialmente_deve_lancar_excecao() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria(); //o método já inicializa uma loteria
		assertThrows(EstadoLoteriaException.class, () -> {
			loteriaServico.iniciarNovaLoteria(new Loteria("Outra Loteria"));	
		});
	}
	
	@Test
	public void teste_finalizar_loteria_sequencialmente_deve_lancar_excecao() throws Exception{
		LoteriaServico loteriaServico = obterServicoLoteria();
		loteriaServico.finalizarLoteria();
		assertThrows(EstadoLoteriaException.class, () -> {
			loteriaServico.finalizarLoteria();	
		});
	}
}

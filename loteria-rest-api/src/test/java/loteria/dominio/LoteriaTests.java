package loteria.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

import loteria.exceptions.EstadoLoteriaException;
import loteria.utils.LoteriaUtils;

public class LoteriaTests {

	//a seed 42 gera os números [16, 34, 51, 21, 5, 10]
	@Test
	public void teste_loteria_vencedores() {
		LoteriaUtils.setSeed(42);
		System.out.println(Arrays.asList(LoteriaUtils.gerarResultado()));
		Apostador apostadorSena = new Apostador("Ganhador Sena");
		apostadorSena.adicionarAposta(new Aposta(new int[] { 16, 34, 51, 21, 5, 10 }, null));
		Apostador apostadorQuina = new Apostador("Ganhador Quina");
		apostadorQuina.adicionarAposta(new Aposta(new int[] { 16, 34, 51, 21, 5, 9 }, null));
		Apostador apostadorQuadra = new Apostador("Ganhador Quadra");
		apostadorQuadra.adicionarAposta(new Aposta(new int[] { 16, 34, 51, 21, 8, 9 }, null));
		Apostador apostadorNaoGanhou = new Apostador("Ganhou nada");
		apostadorNaoGanhou.adicionarAposta(new Aposta(new int[] { 16, 34, 51, 27, 8, 9 }, null));
		Loteria loteria = new Loteria("MegaSena");
		loteria.adicionarApostador(apostadorSena);
		loteria.adicionarApostador(apostadorQuina);
		loteria.adicionarApostador(apostadorQuadra);
		loteria.adicionarApostador(apostadorNaoGanhou);
		ResultadoLoteria resultado = loteria.finalizarSorteio();
		assertEquals(3, resultado.getVencedores().count());
		// Verifica se os vencedores são corretos
		resultado.getVencedores().forEach(vencedor -> {
			assertNotNull(vencedor.getPremio());
			switch (vencedor.getPremio()) {
			case SENA:
				assertEquals(vencedor.getApostador(), apostadorSena);
				break;
			case QUINA:
				assertEquals(vencedor.getApostador(), apostadorQuina);
				break;
			case QUADRA:
				assertEquals(vencedor.getApostador(), apostadorQuadra);
				break;
			}
		});
		// Verifica que o apostador que não obteve nenhum prêmio não está nos resultados
		assertFalse(resultado.getVencedores().anyMatch(vencedor -> vencedor.getApostador() == apostadorNaoGanhou));
	}

	// Verifica se ao finalizar o estado da loteria é o de finalizado
	@Test
	public void teste_estado_loteria_ao_finalizar() {
		Loteria loteria = new Loteria("MegaSena");
		loteria.finalizarSorteio();
		assertEquals(loteria.getEstadoLoteria().getClass(), LoteriaFinalizada.class);
	}

	// Verifica se ao inicializar a loteria seu estado é o de AguardandoAposta
	@Test
	public void teste_estado_loteria_ao_inicializar() {
		Loteria loteria = new Loteria("MegaSena");
		assertEquals(loteria.getEstadoLoteria().getClass(), LoteriaAguardandoApostas.class);
	}
	
	@Test
	public void teste_finalizar_loteria_sequencialmente_deve_lancar_excecao() {
		Loteria loteria = new Loteria("MegaSena");
		loteria.finalizarSorteio();
		assertThrows(EstadoLoteriaException.class, () -> {
			loteria.finalizarSorteio();
	    });
	}
}

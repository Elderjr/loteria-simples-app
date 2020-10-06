package loteria.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import loteria.dominio.LoteriaVencedor.Premio;

public class ApostaTests {

	
	@Test
	public void teste_premio_aposta_deve_ser_sena() {
		int numerosApostados[] = {1,2,3,4,5,6};
		int numerosSorteados[] = {1,2,3,5,6,4};
		Aposta aposta = new Aposta(numerosApostados, null);
		assertEquals(Premio.SENA, aposta.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_aposta_deve_ser_quina() {
		int numerosApostados[] = {1,2,3,4,5,6};
		int numerosSorteados[] = {1,2,3,5,6,8};
		Aposta aposta = new Aposta(numerosApostados, null);
		assertEquals(Premio.QUINA, aposta.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_aposta_deve_ser_quadra() {
		int numerosApostados[] = {1,2,3,4,5,6};
		int numerosSorteados[] = {15,2,3,5,6,8};
		Aposta aposta = new Aposta(numerosApostados, null);
		assertEquals(Premio.QUADRA, aposta.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_aposta_deve_ser_nulo() {
		int numerosApostados[] = {1,2,3,4,5,6};
		int numerosSorteados[] = {15,2,3,5,7,8};
		Aposta aposta = new Aposta(numerosApostados, null);
		assertNull(aposta.verificarPremio(numerosSorteados));
	}
}

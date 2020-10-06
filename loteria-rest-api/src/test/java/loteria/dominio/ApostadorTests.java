package loteria.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import loteria.dominio.LoteriaVencedor.Premio;

public class ApostadorTests {

	@Test
	public void teste_premio_apostador_deve_ser_sena() {
		Apostador apostador = new Apostador("teste");
		Aposta premioNull= new Aposta(new int[] {1,2,3,4,5,9}, null);
		Aposta premioQuadra = new Aposta(new int[] {1,2,3,4,7,8}, null);
		Aposta premioQuina = new Aposta(new int[] {1,2,3,4,5,7}, null);
		Aposta premioSena = new Aposta(new int[] {1,2,3,4,5,6}, null);
		apostador.adicionarAposta(premioNull);
		apostador.adicionarAposta(premioQuadra);
		apostador.adicionarAposta(premioQuina);
		apostador.adicionarAposta(premioSena);
		int numerosSorteados[] = {1,2,3,5,6,4};
		assertEquals(Premio.SENA, apostador.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_apostador_deve_ser_quina() {
		Apostador apostador = new Apostador("teste");
		Aposta premioQuadra = new Aposta(new int[] {1,2,3,4,7,8}, null);
		Aposta premioQuina = new Aposta(new int[] {1,2,3,4,5,7}, null); 
		Aposta premioNull= new Aposta(new int[] {1,2,3,4,5,9}, null);
		apostador.adicionarAposta(premioQuadra);
		apostador.adicionarAposta(premioQuina);
		apostador.adicionarAposta(premioNull);
		int numerosSorteados[] = {1,2,3,5,6,4};
		assertEquals(Premio.QUINA, apostador.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_apostador_deve_ser_quadra() {
		Apostador apostador = new Apostador("teste");
		Aposta premioNull = new Aposta(new int[] {1,2,3,14,15,17}, null); 
		Aposta premioNull2= new Aposta(new int[] {1,2,3,4,15,19}, null);
		Aposta premioQuadra = new Aposta(new int[] {1,2,3,4,7,8}, null);
		apostador.adicionarAposta(premioQuadra);
		apostador.adicionarAposta(premioNull);
		apostador.adicionarAposta(premioNull2);
		int numerosSorteados[] = {1,2,3,5,6,4};
		assertEquals(Premio.QUADRA, apostador.verificarPremio(numerosSorteados));
	}
	
	@Test
	public void teste_premio_apostador_deve_ser_null() {
		Apostador apostador = new Apostador("teste");
		Aposta premioNull = new Aposta(new int[] {1,2,3,14,15,17}, null); 
		Aposta premioNull2= new Aposta(new int[] {1,2,3,13,15,19}, null);
		apostador.adicionarAposta(premioNull);
		apostador.adicionarAposta(premioNull2);
		int numerosSorteados[] = {1,2,3,5,6,4};
		assertNull(apostador.verificarPremio(numerosSorteados));
	}
}

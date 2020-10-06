package loteria.repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

import loteria.dominio.Aposta;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.simplepage.SimplePage;

public class LoteriaRepositorioTests {

	
	@Test
	public void salvar_loteria_deve_criar_identificadores() {
		Apostador apostadorA = new Apostador("A");
		Aposta apostaA = new Aposta(new int[] {1,2,3,4,5,6}, null);
		Aposta apostaB = new Aposta(new int[] {1,2,3,4,5,6}, null);
		apostadorA.adicionarAposta(apostaA);
		apostadorA.adicionarAposta(apostaB);
		Apostador apostadorB = new Apostador("B");
		Aposta apostaC = new Aposta(new int[] {1,2,3,4,5,6}, null);
		apostadorB.adicionarAposta(apostaC);
		
		//Todos os identificadores devem ser nulos pois ainda não foram salvados no banco
		assertNull(apostadorA.getId());
		assertNull(apostadorB.getId());
		assertNull(apostaA.getId());
		assertNull(apostaB.getId());
		assertNull(apostaC.getId());
		
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("MegaSena");
		loteria.adicionarApostador(apostadorA);
		loteria.adicionarApostador(apostadorB);
		loteriaRepositorio.salvarLoteria(loteria);
		
		//Todos os Identificadores não podem ser mais nulo pois foram salvos
		assertNotNull(apostadorA.getId());
		assertNotNull(apostadorB.getId());
		assertNotNull(apostaA.getId());
		assertNotNull(apostaB.getId());
		assertNotNull(apostaC.getId());
		
		//Identificadores de apostadores devem ser diferentes entre si (idem aposta)
		assertNotEquals(apostadorA.getId(), apostadorB.getId());
		assertNotEquals(apostaA.getId(), apostaB.getId());
		assertNotEquals(apostaA.getId(), apostaC.getId());
		assertNotEquals(apostaB.getId(), apostaC.getId());
	}
	
	@Test
	public void teste_pagina_loteria() {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("MegaSena");
		Apostador apostadorA = new Apostador("A");
		Apostador apostadorB = new Apostador("B");
		Apostador apostadorC = new Apostador("C");
		Apostador apostadorD = new Apostador("D");
		Apostador apostadorE = new Apostador("E");
		loteria.adicionarApostador(apostadorA);//pagina 0
		loteria.adicionarApostador(apostadorB);//pagina 0
		loteria.adicionarApostador(apostadorC);//pagina 0
		loteria.adicionarApostador(apostadorD);//pagina 1
		loteria.adicionarApostador(apostadorE);//pagina 1
		loteriaRepositorio.salvarLoteria(loteria);
		SimplePage<Apostador> paginaApostadores = loteriaRepositorio.buscarApostadores(1, 3);
		assertEquals(5, paginaApostadores.getTotalItens());
		assertEquals(3, paginaApostadores.getItensPorPagina());
		assertEquals(1, paginaApostadores.getPagina());
		assertEquals(2, paginaApostadores.getTotalPaginas());
		assertEquals(2, paginaApostadores.getItens().size());
		assertTrue(paginaApostadores.getItens().contains(apostadorD));
		assertTrue(paginaApostadores.getItens().contains(apostadorE));
	}
	
	@Test
	public void teste_obter_loteria() {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteriaCriada = new Loteria("MegaSena");
		Apostador apostadorA = new Apostador("A");
		Apostador apostadorB = new Apostador("B");
		loteriaCriada.adicionarApostador(apostadorA);
		loteriaCriada.adicionarApostador(apostadorB);
		loteriaRepositorio.salvarLoteria(loteriaCriada);
		Loteria loteria = loteriaRepositorio.obterLoteria();
		assertEquals(loteria.getNome(), "MegaSena");
		assertEquals(loteria.getTotalApostadores(), 2);
		assertTrue(loteria.getApostadores().anyMatch(apostador -> apostador == apostadorA));
		assertTrue(loteria.getApostadores().anyMatch(apostador -> apostador == apostadorB));
	}
	
	@Test
	public void teste_obter_apostador_por_id() {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("MegaSena");
		Apostador apostadorA = new Apostador("A");
		Apostador apostadorB = new Apostador("B");
		loteria.adicionarApostador(apostadorA);
		loteria.adicionarApostador(apostadorB);
		loteriaRepositorio.salvarLoteria(loteria);
		assertEquals(loteriaRepositorio.obterApostadorPorId(apostadorA.getId()).get(), apostadorA);
		assertEquals(loteriaRepositorio.obterApostadorPorId(apostadorB.getId()).get(), apostadorB);
	}
	
}

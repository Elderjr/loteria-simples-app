package loteria.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import loteria.LoteriaApplication;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.dto.buscar.apostadores.ApostadorDTO;
import loteria.dto.inserir.NovaLoteriaDTO;
import loteria.dto.inserir.aposta.NovaApostaDTOInput;
import loteria.dto.inserir.aposta.NovaApostaDTOOutput;
import loteria.dto.resultadosorteio.LoteriaDTO;
import loteria.repositorio.LoteriaRepositorio;
import loteria.repositorio.LoteriaRepositorioImpl;
import loteria.simplepage.SimplePage;

@SpringBootTest(classes = LoteriaApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class LoteriaControllerTests {

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
		return objectMapper.readValue(json, clazz);
	}

	//url: POST /loteria/apostas
	@Test
	void teste_vincular_aposta_a_novo_apostador() throws Exception {
		String uri = "/loteria/apostas";
		int numerosApostados[] = new int[] {1,2,3,4,5,6};
		String nomeApostador = "Tester";
		NovaApostaDTOInput input = new NovaApostaDTOInput(null, nomeApostador, numerosApostados);
		String inputJson = mapToJson(input);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.CREATED.value());
		NovaApostaDTOOutput output = mapFromJson(mvcResult.getResponse().getContentAsString(), NovaApostaDTOOutput.class);
		assertNotNull(output.getIdAposta());
		assertNotNull(output.getIdApostador());
		assertTrue(Arrays.equals(input.getNumerosApostados(), output.getNumerosApostados()));
	}
	
	//url: POST /loteria/apostas
	@Test
	void teste_vincular_aposta_a_apostador_existente() throws Exception {
		//insere apostador manualmente no repositorio
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("MegaSena");
		Apostador apostador = new Apostador("A");
		loteria.adicionarApostador(apostador);
		loteriaRepositorio.salvarLoteria(loteria);
		
		//realiza a requisição de adicionar nova aposta ao apostador
		String uri = "/loteria/apostas";
		int numerosApostados[] = new int[] {1,2,3,4,5,6};
		String nomeApostador = "Tester";
		NovaApostaDTOInput input = new NovaApostaDTOInput(apostador.getId(), nomeApostador, numerosApostados);
		String inputJson = mapToJson(input);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		//verifica dados
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.CREATED.value());
		NovaApostaDTOOutput output = mapFromJson(mvcResult.getResponse().getContentAsString(), NovaApostaDTOOutput.class);
		assertNotNull(output.getIdAposta());
		assertNotNull(output.getIdApostador());
		assertEquals(output.getIdApostador(), apostador.getId());
		apostador = loteriaRepositorio.obterApostadorPorId(output.getIdApostador()).get();
		//verifica se de fato foi inserido a nova aposta a às apostas do apostador
		assertTrue(apostador.getApostas()
				.anyMatch(aposta -> aposta.getId().equals(output.getIdAposta()) 
						&& Arrays.equals(aposta.getNumerosApostados(), output.getNumerosApostados())));
		assertTrue(Arrays.equals(input.getNumerosApostados(), output.getNumerosApostados()));
	}
	
	//url: POST /loteria/apostas
	@Test
	void teste_vincular_aposta_a_apostador_nao_existente() throws Exception {
		String uri = "/loteria/apostas";
		int numerosApostados[] = new int[] {1,2,3,4,5,6};
		String nomeApostador = "Tester";
		NovaApostaDTOInput input = new NovaApostaDTOInput(520, nomeApostador, numerosApostados);
		String inputJson = mapToJson(input);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		assertNotNull(mvcResult.getResponse().getErrorMessage());
	}
	
	//url: GET /loteria/apostas?page=2&itensPorPagina=3
	@Test
	void teste_buscar_apostadores() throws Exception{
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
		String uri = "/loteria/apostas?pagina=2&itensPorPagina=3";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
		//Converte o JSON de saída para SimplePage<Apostador>
		ObjectMapper mapper = new ObjectMapper(); 
	    TypeReference<SimplePage<ApostadorDTO>> typeRef 
	            = new TypeReference<SimplePage<ApostadorDTO>>() {};
	    SimplePage<ApostadorDTO> paginaApostadores = 
	    		mapper.readValue(mvcResult.getResponse().getContentAsString(), typeRef);
		assertEquals(5, paginaApostadores.getTotalItens());
		assertEquals(3, paginaApostadores.getItensPorPagina());
		assertEquals(1, paginaApostadores.getPagina());
		assertEquals(2, paginaApostadores.getTotalPaginas());
		assertEquals(2, paginaApostadores.getItens().size());
		assertTrue(paginaApostadores.getItens().stream()
				.anyMatch(apostador -> apostador.getId().equals(apostadorD.getId())));
		assertTrue(paginaApostadores.getItens().stream()
				.anyMatch(apostador -> apostador.getId().equals(apostadorE.getId())));
	}
	
	//url: GET /loteria
	@Test
	void teste_obter_informacoes_loteria_simples() throws Exception{
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("LoteriaTest");
		loteriaRepositorio.salvarLoteria(loteria);
		String uri = "/loteria";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
		//Converte o JSON de saída para LoteriaDTO
		LoteriaDTO loteriaDTO = mapFromJson(mvcResult.getResponse().getContentAsString(), LoteriaDTO.class);
		assertEquals("LoteriaTest", loteriaDTO.getNome());
		assertNull(loteriaDTO.getResultado());
	}
	
	//url: PUT /loteria/finalizar
	@Test
	void teste_finalizar_loteria() throws Exception {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("LoteriaTest");
		loteriaRepositorio.salvarLoteria(loteria);
		String uri = "/loteria/finalizar";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
		loteria = loteriaRepositorio.obterLoteria();
		assertTrue(loteria.isEncerrado());
	}
	
	//url: POST /loteria
	@Test
	void teste_iniciar_nova_loteria_com_loteria_atual_nao_finalizada() throws Exception {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		Loteria loteria = new Loteria("LoteriaTest");
		loteriaRepositorio.salvarLoteria(loteria);
		NovaLoteriaDTO novaLoteria = new NovaLoteriaDTO("NovaLoteria");
		String inputJson = mapToJson(novaLoteria);
		String uri = "/loteria";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		assertNotNull(mvcResult.getResponse().getErrorMessage());
	}
	
	//url: POST /loteria
	@Test
	void teste_iniciar_nova_loteria() throws Exception {
		LoteriaRepositorio loteriaRepositorio = new LoteriaRepositorioImpl();
		loteriaRepositorio.obterLoteria().finalizarSorteio(); //finaliza sorteio atual para poder iniciar outra
		NovaLoteriaDTO novaLoteria = new NovaLoteriaDTO("NovaLoteria");
		String inputJson = mapToJson(novaLoteria);
		String uri = "/loteria";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		//Converte o JSON de saída para LoteriaDTO
		LoteriaDTO loteriaDTO = mapFromJson(mvcResult.getResponse().getContentAsString(), LoteriaDTO.class);
		assertEquals("NovaLoteria", loteriaDTO.getNome());
		assertNull(loteriaDTO.getResultado());
	}

}

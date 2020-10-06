package loteria.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import loteria.dto.buscar.apostadores.ApostadorDTO;
import loteria.dto.inserir.NovaLoteriaDTO;
import loteria.dto.inserir.aposta.NovaApostaDTOInput;
import loteria.dto.inserir.aposta.NovaApostaDTOOutput;
import loteria.dto.resultadosorteio.LoteriaDTO;
import loteria.dto.resultadosorteio.ResultadoLoteriaDTO;
import loteria.exceptions.ApostaInvalidaException;
import loteria.exceptions.ApostadorNotFoundException;
import loteria.exceptions.EstadoLoteriaException;
import loteria.servico.LoteriaServico;
import loteria.simplepage.SimplePage;
import loteria.dominio.Aposta;
import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.dominio.ResultadoLoteria;

@Controller
@RequestMapping("/loteria")
public class LoteriaController {

	@Autowired
	private LoteriaServico service;

	@CrossOrigin
	@RequestMapping(value = "/apostas", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NovaApostaDTOOutput> adicionarAposta(@RequestBody NovaApostaDTOInput apostaDTO) {
		try {
			Aposta aposta = new Aposta(apostaDTO.getNumerosApostados(), LocalDateTime.now());
			if (apostaDTO.getIdApostador() != null) {
				service.vincularApostaAoApostador(apostaDTO.getIdApostador(), aposta);
			} else if (apostaDTO.getNomeApostador() != null) {
				service.vincularApostaAoApostador(apostaDTO.getNomeApostador(), aposta);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Nome ou id do apostador deve ser especificado");
			}
			NovaApostaDTOOutput out = new NovaApostaDTOOutput(aposta);
			return new ResponseEntity<>(out, HttpStatus.CREATED);
		} catch (ApostaInvalidaException | ApostadorNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/apostas", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> obterApostadores(@RequestParam(defaultValue = "1") int pagina,
			@RequestParam(defaultValue = "5") int itensPorPagina) {
		SimplePage<Apostador> apostadoresPagina = service.buscarApostadores(pagina - 1, itensPorPagina);
		// Converte a pagina de Apostador para uma pagina de ApostadorDTO
		List<ApostadorDTO> apostadoresDTO = apostadoresPagina.getItens().stream().map(ApostadorDTO::new)
				.collect(Collectors.toList());
		SimplePage<ApostadorDTO> apostadoresPaginaDTO = new SimplePage<>(apostadoresDTO,
				apostadoresPagina.getTotalItens(), apostadoresPagina.getTotalPaginas(), apostadoresPagina.getPagina(),
				apostadoresPagina.getItensPorPagina());
		return new ResponseEntity<>(apostadoresPaginaDTO, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/finalizar", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResultadoLoteriaDTO> finalizar() {
		try {
			ResultadoLoteria resultadoLoteria = service.finalizarLoteria();
			ResultadoLoteriaDTO dto = new ResultadoLoteriaDTO(resultadoLoteria);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} catch (EstadoLoteriaException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoteriaDTO> getLoteriaAtual() {
		Loteria loteria = service.obterLoteriaAtual();
		LoteriaDTO dto = new LoteriaDTO(loteria);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> criarNovaMegaSena(@RequestBody NovaLoteriaDTO novaLoteria) {
		try {
			Loteria loteria = new Loteria(novaLoteria.getNome());
			service.iniciarNovaLoteria(loteria);
			LoteriaDTO dto = new LoteriaDTO(loteria);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (EstadoLoteriaException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

}

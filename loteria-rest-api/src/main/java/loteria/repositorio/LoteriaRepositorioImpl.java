package loteria.repositorio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.simplepage.SimplePage;

@Component
public class LoteriaRepositorioImpl implements LoteriaRepositorio{

	private static int contadorIdApostador = 0;
	private static int contadorIdAposta = 0;
	private static Loteria loteria = new Loteria("Mega Sena");
	

	@Override
	public void salvarLoteria(Loteria novaLoteria) {
		loteria = novaLoteria;
		loteria.getApostadores().forEach(apostador -> {
			if(apostador.getId() == null) {
				contadorIdApostador++;
				apostador.setId(contadorIdApostador);
			}
			apostador.getApostas().forEach(aposta -> {
				if(aposta.getId() == null) {
					contadorIdAposta++;
					aposta.setId(contadorIdAposta);
				}
			});
		});
	}

	@Override
	public Loteria obterLoteria() {
		return loteria;
	}

	@Override
	public Optional<Apostador> obterApostadorPorId(Integer id) {
		return loteria.getApostadores()
				.filter(apostador -> apostador.getId() != null && apostador.getId().equals(id))
				.findFirst();
	}

	@Override
	public SimplePage<Apostador> buscarApostadores(int pagina, int itensPorPagina) {
		int inicio = pagina * itensPorPagina;
		int fim = inicio + itensPorPagina;
		int totalApostadores = loteria.getTotalApostadores();
		int totalPaginas = (int) Math.ceil(((double) totalApostadores / itensPorPagina));
		
		List<Apostador> apostadores;
		if(inicio < loteria.getTotalApostadores()) {
			if(fim >= loteria.getTotalApostadores()) {
				fim = loteria.getTotalApostadores();
			}
			apostadores = loteria.getApostadores().collect(Collectors.toList()).subList(inicio, fim);
		} else {
			apostadores = Collections.emptyList();
		}
		return new SimplePage<>(apostadores, totalApostadores, 
				totalPaginas, pagina, itensPorPagina);
	}

}

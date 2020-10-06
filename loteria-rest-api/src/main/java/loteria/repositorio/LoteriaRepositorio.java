package loteria.repositorio;

import java.util.Optional;

import loteria.dominio.Apostador;
import loteria.dominio.Loteria;
import loteria.simplepage.SimplePage;

public interface LoteriaRepositorio {
	
	public void salvarLoteria(Loteria loteria);

	public Loteria obterLoteria();
	
	public Optional<Apostador> obterApostadorPorId(Integer id);
	
	public SimplePage<Apostador> buscarApostadores(int pagina, int itensPorPagina);

}

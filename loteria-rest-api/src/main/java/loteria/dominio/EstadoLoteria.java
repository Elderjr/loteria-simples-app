package loteria.dominio;

import java.util.List;

public abstract class EstadoLoteria {

	public abstract boolean isEncerrado();
	
	/* package */ abstract ResultadoLoteria finalizarSorteio(List<Apostador> apostadores);
	
}

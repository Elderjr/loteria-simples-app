package loteria.dominio;

import java.util.ArrayList;
import java.util.List;

import loteria.dominio.LoteriaVencedor.Premio;
import loteria.utils.LoteriaUtils;

public class LoteriaAguardandoApostas extends EstadoLoteria {

	@Override
	public boolean isEncerrado() {
		return false;
	}

	@Override
	/* package */ ResultadoLoteria finalizarSorteio(List<Apostador> apostadores) {
		List<LoteriaVencedor> vencedores = new ArrayList<>();		
		int numerosSorteados[] = LoteriaUtils.gerarResultado();
		for(Apostador apostador : apostadores) {
			Premio premio = apostador.verificarPremio(numerosSorteados);
			if(premio != null) {
				vencedores.add(new LoteriaVencedor(apostador, premio));
			}
		}
		return new ResultadoLoteria(numerosSorteados, vencedores); 
	}

}

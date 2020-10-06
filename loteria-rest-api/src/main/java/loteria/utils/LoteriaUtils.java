package loteria.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LoteriaUtils {

	
	private static Long seed;
	
	private LoteriaUtils() {

	}
	
	//unicamente para propositos de testes, em que setamos a seed para saber o resultado
	public static void setSeed(long novaSeed) {
		seed = novaSeed;
	}

	/**
	 * Gera os números sorteados de uma loteria, isto é, um vetor de inteiros de tamanho 6 contendo
	 * números de 1 a 60 e que não se repetem no vetor
	 */
	public static int[] gerarResultado() {
		Random r;
		if(seed != null) {
			r = new Random(seed);
		} else {
			r = new Random();
		}
		Set<Integer> numerosGerados = new HashSet<>();
		while(numerosGerados.size() < 6) {
			numerosGerados.add(r.nextInt(60 - 1) + 1);
		}
		return numerosGerados.stream().mapToInt(Integer::intValue).toArray();	
	}
	

	
}

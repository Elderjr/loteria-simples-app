package loteria.simplepage;

import java.util.Collection;

public class SimplePage<T> {

	private Collection<T> itens;
	private int itensPorPagina;
	private int totalItens;
	private int pagina;
	private int totalPaginas;
	
	//para propositos de conversão para JSON
	public SimplePage() {
		
	}
	
	public SimplePage(Collection<T> items, int totalItems, int totalPages, int page, int itemsPerPage) {
		this.itens = items;
		this.totalItens = totalItems;
		this.pagina = page;
		this.itensPorPagina = itemsPerPage;
		this.totalPaginas = totalPages;
	}


	public Collection<T> getItens() {
		return itens;
	}

	public int getTotalPaginas() {
		return this.totalPaginas;
	}

	public int getItensPorPagina() {
		return itensPorPagina;
	}


	public int getTotalItens() {
		return totalItens;
	}


	public int getPagina() {
		return pagina;
	}
	
	
}

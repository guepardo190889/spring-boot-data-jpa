package com.blackdeath.springboot.app.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author blackdeath
 *
 */
public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int totalElementosPorPagina;
	private int paginaActual;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		this.totalElementosPorPagina = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (totalPaginas <= totalElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= totalElementosPorPagina / 2) {
				desde = 1;
				hasta = totalElementosPorPagina;
			} else if (paginaActual >= totalPaginas - totalElementosPorPagina / 2) {
				desde = totalPaginas - totalElementosPorPagina + 1;
				hasta = totalElementosPorPagina;
			} else {
				desde = paginaActual - totalElementosPorPagina / 2;
				hasta = totalElementosPorPagina;
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getTotalElementosPorPagina() {
		return totalElementosPorPagina;
	}

	public void setTotalElementosPorPagina(int totalElementosPorPagina) {
		this.totalElementosPorPagina = totalElementosPorPagina;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public void setPaginas(List<PageItem> paginas) {
		this.paginas = paginas;
	}

}

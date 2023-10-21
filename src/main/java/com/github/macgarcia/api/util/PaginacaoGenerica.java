package com.github.macgarcia.api.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginacaoGenerica<T> {
	
	private List<T> dados;
	private Integer totalDeRegistros;
	private Integer totalDePaginas;
	private Integer totalRegistroPorPagina;
	
	public PaginacaoGenerica(List<T> dados) {
		this.dados = dados;
		this.totalDeRegistros = dados.size();
		this.totalRegistroPorPagina = RegraRepository.TOTAL_DE_REGISTROS;
		this.totalDePaginas = (int) Math.ceil((double) totalDeRegistros / totalRegistroPorPagina);
		
	}
	
}

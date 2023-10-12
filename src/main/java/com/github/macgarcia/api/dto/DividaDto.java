package com.github.macgarcia.api.dto;

import java.io.Serializable;

import com.github.macgarcia.web.model.Divida;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DividaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String descricao;
	private String valor;
	private String dataDivida;
	private String mes;
	private Integer ano;

	public DividaDto(final Divida divida) {
		this.id = divida.getId();
		this.descricao = divida.getDescricao();
		this.valor = divida.getValor().toString();
		this.dataDivida = divida.getDataDivida().toString();
		this.mes = divida.getMes().name();
		this.ano = divida.getAno();
	}

}

package com.github.macgarcia.web.reports;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

import com.github.macgarcia.web.model.Divida;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividaVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String descricao;
	private String valor;
	private String categoria;
	private String dataDivida;
	
	public DividaVo(Divida divida) {
		this.descricao = divida.getDescricao();
		this.valor = divida.getValorFormatado();
		this.categoria = divida.getCategoria().name();
		this.dataDivida = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(divida.getDataDivida());
	}
}

package com.github.macgarcia.web.reports;

public enum Relatorio {
	
	RELATORIO_DE_FECHAMENTO_MENSAL("/reports/ResumoMensal.jrxml");
	
	private final String caminho;
	
	Relatorio(final String caminho) {
		this.caminho = caminho;
	}
	
	public String getCaminho() {
		return caminho;
	}
}

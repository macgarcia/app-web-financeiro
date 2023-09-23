package com.github.macgarcia.web.enums;

public enum ProcessosArmazenados {
	
	PROCESSAR_FECHAMENTO_MES("processar_fechamento_mes", "call processar_fechamento_mes(:mes_selecionado_p, :valor_saldo_mensal_p)"),
	DESFAZER_FECHAMENTO_MES("desfazer_fechamento_mes", "call desfazer_fechamento_mes(:id_calculo_mensal_p)")
	;

	private final String nomeProcesso;
	private final String chamadaNativa;

	ProcessosArmazenados(String nomeProcesso, String chamadaNativa) {
		this.nomeProcesso = nomeProcesso;
		this.chamadaNativa = chamadaNativa;
	}
	
	public String getNomeProcesso() {
		return nomeProcesso;
	}
	
	public String getChamadaNativa() {
		return chamadaNativa;
	}
	
}

package com.github.macgarcia.web.enums;

public enum CategoriaDivida {
	
	CARTAO_CREDITO, ESTUDO, CASA, SAUDE, DIVERSOS;
	
    public static CategoriaDivida get(int index) {
        switch (index) {
            case 0:
                return CARTAO_CREDITO;
            case 1:
                return ESTUDO;
            case 2:
                return CASA;
            case 3:
                return SAUDE;
            case 4:
                return DIVERSOS;
            default:
                throw new IllegalArgumentException("Índice incorreto");
        }
    }
	
}

package com.github.macgarcia.web.enums;

public enum Mes {

	JANEIRO, FEVEREIRO, MARCO, ABRIL, MAIO, JUNHO, JULHO, AGOSTO, SETEMBRO, OUTUBRO, NOVEMBRO, DEZEMBRO;

	   public static Mes getMesComDigito(int digito) {
	        switch (digito) {
	            case 0:
	                return JANEIRO;
	            case 1:
	                return FEVEREIRO;
	            case 2:
	                return MARCO;
	            case 3:
	                return ABRIL;
	            case 4:
	                return MAIO;
	            case 5:
	                return JUNHO;
	            case 6:
	                return JULHO;
	            case 7:
	                return AGOSTO;
	            case 8:
	                return SETEMBRO;
	            case 9:
	                return OUTUBRO;
	            case 10:
	                return NOVEMBRO;
	            case 11:
	                return DEZEMBRO;
	            default:
	                throw new IllegalArgumentException("Digito incorreto");
	        }
	    }

}

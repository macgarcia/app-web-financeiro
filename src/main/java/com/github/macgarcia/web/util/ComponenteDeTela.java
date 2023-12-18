package com.github.macgarcia.web.util;

import com.github.macgarcia.web.enums.Mes;

public final class ComponenteDeTela {
	
	private ComponenteDeTela() {}
	
	public static Mes[] meses() {
		return Mes.values();
	}
}

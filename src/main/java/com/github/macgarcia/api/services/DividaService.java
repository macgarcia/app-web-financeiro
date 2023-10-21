package com.github.macgarcia.api.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.github.macgarcia.api.dto.DividaDto;
import com.github.macgarcia.api.util.PaginacaoGenerica;
import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.DividaRepository;
import com.google.gson.Gson;

public class DividaService {
	
	private final String PAGINA_ZERO = "0";

	@Inject
	private DividaRepository repository;

	public String getTodasAsDividas(String pagina) {
		int numeroDaPagina = numeroDaPagina(pagina);
		List<Divida> todasAsDividas = repository.getAll(numeroDaPagina);
		List<DividaDto> collect = todasAsDividas.stream().map(DividaDto::new).collect(Collectors.toList());
		PaginacaoGenerica<DividaDto> dadosPaginados = new PaginacaoGenerica<DividaDto>(collect);
		return new Gson().toJson(dadosPaginados);
	}

	private Integer numeroDaPagina(String pagina) {
		return Objects.isNull(pagina) || PAGINA_ZERO.equals(pagina) ? 1 : Integer.valueOf(pagina);
	}

	public String getDividaPorId(final Integer id) {
		Divida divida = repository.consultarPorId(Divida.class, id);
		if (Objects.isNull(divida)) {
			return "";
		}
		DividaDto dto = new DividaDto(divida);
		return new Gson().toJson(dto);
	}

}

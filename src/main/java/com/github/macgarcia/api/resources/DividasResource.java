package com.github.macgarcia.api.resources;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.macgarcia.api.dto.DividaDto;
import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.DividaRepository;
import com.google.gson.Gson;

@WebServlet("/divida")
public class DividasResource extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private DividaRepository repository;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Divida> todasAsDividas = repository.getAll();
		List<DividaDto> collect = todasAsDividas.stream().map(DividaDto::new).collect(Collectors.toList());
		String json = new Gson().toJson(collect);
		
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		resp.getWriter().write(json);
	}

}

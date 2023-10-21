package com.github.macgarcia.api.resources;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.macgarcia.api.services.DividaService;

@WebServlet(name = "DividaController", urlPatterns = { "/divida", "/divida/all", "/divida/one" })
public class DividasResource extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final String TODAS_AS_DIVIDAS = "/divida/all";
	private final String UMA_DIVIDA = "/divida/one";
	private final String APPLICATION_JSON = "application/json";

	@Inject
	private DividaService service;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final String path = req.getServletPath();
		String json = null;

		switch (path) {
		case TODAS_AS_DIVIDAS:
			String paginaReqest = req.getParameter("pagina");
			json = service.getTodasAsDividas(paginaReqest);
			break;
		case UMA_DIVIDA:
			final Integer id = Integer.valueOf(req.getParameter("id"));
			json = service.getDividaPorId(id);
			break;
		default:
			break;
		}
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType(APPLICATION_JSON);
		resp.getWriter().write(json);
	}

}

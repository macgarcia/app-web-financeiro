import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.DividaRepository;

@WebServlet("/teste")
public class TestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private DividaRepository dao;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Divida d = dao.consultarPorId(Divida.class, 22);
		resp.getWriter().print(d);
	}
}

import javax.inject.Inject;

import com.github.macgarcia.web.model.Divida;
import com.github.macgarcia.web.repository.DividaRepository;

public class Main {
	@Inject
	static DividaRepository dao;
	
	public static void main(String[] args) {
		//DividaRepository dao = new DividaRepository();
		Divida d = dao.consultarPorId(Divida.class, 22);
		System.out.println(d);
	}

}

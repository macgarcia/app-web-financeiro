package com.github.macgarcia.web.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import com.github.macgarcia.api.util.RegraRepository;
import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.interceptador.Transacional;
import com.github.macgarcia.web.model.Divida;

public class DividaRepository extends JPARepository<Divida> implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Divida> todasAsDividasDoMes(Mes mes) {
		TypedQuery<Divida> query = getEntityManager().createNamedQuery("Divida.todasAsDividasDoMesCorrente", Divida.class);
		query.setParameter("ano", LocalDate.now().getYear())
				.setParameter("mes", mes);
		return query.getResultList();
	}
	
	@Transacional
	public List<Divida> getAll(int pagina) {
		TypedQuery<Divida> query = getEntityManager().createNamedQuery("Divida.todasAsDividas", Divida.class);
		query.setFirstResult((pagina - 1) * RegraRepository.TOTAL_DE_REGISTROS);
		query.setMaxResults(RegraRepository.TOTAL_DE_REGISTROS);
		return query.getResultList();
	}

}

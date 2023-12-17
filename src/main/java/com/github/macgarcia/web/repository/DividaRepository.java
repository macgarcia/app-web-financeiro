package com.github.macgarcia.web.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.interceptador.Transacional;
import com.github.macgarcia.web.model.Divida;

public class DividaRepository extends JPARepository<Divida> implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int ANO_CORRENTE = LocalDate.now().getYear();

	public List<Divida> todasAsDividasDoMes(Mes mes) {
		TypedQuery<Divida> query = getEntityManager().createNamedQuery("Divida.todasAsDividasDoMesCorrente", Divida.class);
		query.setParameter("ano", ANO_CORRENTE)
				.setParameter("mes", mes);
		return query.getResultList();
	}
	
	public List<Double> valoresDeDividasDeUmMes(Mes mes) {
		TypedQuery<Double> query = getEntityManager().createNamedQuery("Divida.todosValoresDasDividas", Double.class);
		query.setParameter("mes", mes).setParameter("ano", ANO_CORRENTE);
		return query.getResultList();
	}
	
	public List<Integer> getListIdsDasDividasDeUmCalculoMensal(Integer calculoMensalId) {
		TypedQuery<Integer> query = getEntityManager().createNamedQuery("Divida.todasAsDividasDeUmCalculoMensal", Integer.class);
		query.setParameter("calculoMensalId", calculoMensalId);
		return query.getResultList();
	}
	
	@Transacional
	public void updateDasDividasDoFechamentoDesfeito(List<Integer> ids) {
		Query query = getEntityManager().createNamedQuery("Divida.atualizarDividas");
		query.setParameter("ids", ids);
		query.executeUpdate();
	}
}

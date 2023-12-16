package com.github.macgarcia.web.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.enums.ProcessosArmazenados;
import com.github.macgarcia.web.enums.Situacao;
import com.github.macgarcia.web.interceptador.Transacional;
import com.github.macgarcia.web.model.CalculoMensal;

public class CalculoMensalRepository extends JPARepository<CalculoMensal> implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int ANO_CORRENTE = LocalDate.now().getYear();

	public List<CalculoMensal> getCalculos() {
		final TypedQuery<CalculoMensal> query = getEntityManager().createNamedQuery("CalculoMensal.todosOsCalculos",
				CalculoMensal.class);
		query.setParameter("ano", ANO_CORRENTE);
		return query.getResultList();
	}

	public boolean existeFechamentoMensal(Mes mes) {
		try {
			final TypedQuery<CalculoMensal> query = getEntityManager()
					.createNamedQuery("CalculoMensal.existeFechamentoMensal", CalculoMensal.class);
			query.setParameter("situacao", Situacao.FECHADO).setParameter("mes", mes).setParameter("ano", ANO_CORRENTE);
			CalculoMensal singleResult = query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean existeCalculoMensalParaOMes(Mes mes) {
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("CalculoMensal.exiteCalculoMesal", Long.class);
			query.setParameter("mes", mes).setParameter("ano", ANO_CORRENTE);
			Long contagem = query.getSingleResult();
			if(contagem > 0) {
				return true;
			} else {
				return false;
			}
		} catch(NoResultException e) {
			return false;
		}
	}

	@Transacional
	public boolean executeProcedure(ProcessosArmazenados procedure, Map<String, Object> parametros) {
		try {
			final Query callProcedure = getEntityManager().createNativeQuery(procedure.getChamadaNativa());
			for (Map.Entry<String, Object> entry : parametros.entrySet()) {
				callProcedure.setParameter(entry.getKey(), entry.getValue());
			}
			callProcedure.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

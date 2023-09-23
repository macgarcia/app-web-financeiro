package com.github.macgarcia.web.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.macgarcia.web.interceptador.Transacional;

/**
 *
 * @author macgarcia
 * @param <T>
 */
public class JPARepository<T extends EntidadeBase> {

	@Inject
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Transacional
	public boolean salvarEntidade(T t) {
		try {
			if (t.getId() != null) {
				entityManager.merge(t);
			} else {
				entityManager.persist(t);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transacional
	public boolean apagarEntidade(final Class<T> classe, final Integer id) {
		final T t = entityManager.find(classe, id);
		entityManager.remove(t);
		return true;
	}

	public T consultarPorId(final Class<T> classe, final Integer id) {
		T t = null;
		t = entityManager.find(classe, id);
		return t;
	}

	@Transacional
	public void persistAll(final List<T> list) {
		final int bachSize = 50;
		final int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			entityManager.persist(list.get(i));
			if (i % bachSize == 0 || i == listSize - 1) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}
}

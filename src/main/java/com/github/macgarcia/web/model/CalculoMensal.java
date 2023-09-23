package com.github.macgarcia.web.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.enums.Situacao;
import com.github.macgarcia.web.repository.EntidadeBase;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "calculo_mensal")
@Data
@NoArgsConstructor
@NamedQueries({
		@NamedQuery(name = "CalculoMensal.todosOsCalculos", query = "select c from CalculoMensal c where c.ano = :ano"),
		@NamedQuery(name = "CalculoMensal.existeFechamentoMensal",
				query = "select c from CalculoMensal c where c.situacao = :situacao and c.mes = :mes and c.ano = :ano")
})
public class CalculoMensal implements Serializable, EntidadeBase {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "valor_saldo_mensal", nullable = false, precision = 10, scale = 2)
	private Double valorSaldoMensal;

	@Column(name = "mes")
	@Enumerated(EnumType.STRING)
	private Mes mes;

	@Column(name = "valor_total_divida", nullable = false, precision = 10, scale = 2)
	private Double valorTotalDividas;

	@Column(name = "valor_resultante", nullable = false, precision = 10, scale = 2)
	private Double valorResultante;

	@Column(name = "situacao")
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	@Column(name = "ano")
	private Integer ano;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "calculoMensal", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE })
	private List<Divida> dividas;

}

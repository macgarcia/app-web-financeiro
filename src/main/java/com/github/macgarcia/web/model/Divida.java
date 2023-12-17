package com.github.macgarcia.web.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.github.macgarcia.web.enums.CategoriaDivida;
import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.repository.EntidadeBase;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "divida")
@Data
@NoArgsConstructor
@NamedQueries({
	@NamedQuery(name = "Divida.todasAsDividasDoMesCorrente", query = "select d from Divida d left join fetch d.calculoMensal where d.ano = :ano and d.mes = :mes"),
	@NamedQuery(name = "Divida.todasAsDividas", query = "select d from Divida d left join fetch d.calculoMensal"),
	@NamedQuery(name = "Divida.todosValoresDasDividas", query = "select d.valor from Divida d where d.mes = :mes and d.ano = :ano"),
	@NamedQuery(name = "Divida.todasAsDividasDeUmCalculoMensal", query = "select d.id from Divida d where d.calculoMensal.id = :calculoMensalId"),
	@NamedQuery(name = "Divida.atualizarDividas", query = "update Divida d set d.calculoMensal = null where d.id in :ids")
})
public class Divida implements Serializable, EntidadeBase {

	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;
    
    @Column(name = "valor", nullable = false)
    private Double valor;
    
    @Column(name = "data_divida", nullable = false)
    private LocalDate dataDivida;
    
    @Column(name = "mes_divida")
    @Enumerated(EnumType.STRING)
    private Mes mes;
    
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaDivida categoria;
    
    @Column(name = "ano")
    private Integer ano = LocalDate.now().getYear();
    
    @ManyToOne
    @JoinColumn(name = "calculo_mensal_id")
    private CalculoMensal calculoMensal;
	
    public String getDataFormatada() {
    	return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.dataDivida);
    }
    
    public String getValorFormatado() {
    	return "R$ " + new DecimalFormat("#,##0.00").format(valor);
    }
}

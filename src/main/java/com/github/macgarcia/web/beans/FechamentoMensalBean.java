package com.github.macgarcia.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.macgarcia.web.enums.Mes;
import com.github.macgarcia.web.model.CalculoMensal;
import com.github.macgarcia.web.processos.DesfazerFechamentoMensal;
import com.github.macgarcia.web.processos.ProcessarFechamentoMensal;
import com.github.macgarcia.web.repository.CalculoMensalRepository;
import com.github.macgarcia.web.util.ComponenteDeTela;

import lombok.Getter;
import lombok.Setter;

@Named(value = "fechamentoMensalBean")
@SessionScoped
public class FechamentoMensalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IndexBean indexBean;

	@Inject
	private ProcessarFechamentoMensal fechamentoMensal;

	@Inject
	private DesfazerFechamentoMensal desfazerFechamentoMensal;

	@Inject
	private CalculoMensalRepository calculoMensalRepository;

	@Getter
	private List<CalculoMensal> calculos;

	@Getter
	@Setter
	private Mes mesFechamento = Mes.getMesComDigito(LocalDate.now().getMonthValue() - 1);

	@Getter
	@Setter
	private Double valorRendaMensal;

	@PostConstruct
	public void init() {
		setCalculos();
	}

	private void setCalculos() {
		this.calculos = calculoMensalRepository.getCalculos();
	}

	public String telaInicial() {
		return "index?faces-redirect=true";
	}

	/* Gets para a tela */
	public Mes[] getMeses() {
		return ComponenteDeTela.meses();
	}
	/**/

	/* Comportamentos da tela */
	public void desfazerFechamento(Integer idCalculo) {
		try {
			desfazerFechamentoMensal.defazerFechamentoMensal(idCalculo, indexBean.getDividaRepository(),
					calculoMensalRepository);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Fechamento mensal", "Fechamento mensal desfeito com sucesso..."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fechamento mensal", e.getMessage()));
		}
		setCalculos();
		indexBean.setDividas();
		indexBean.existeCalculoMensal();
	}

	public void executarFechamentoMensal() {
		if (Objects.isNull(valorRendaMensal)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Fechamento mensal", "Digite o valor"));
		} else {
			try {
				fechamentoMensal.processarFechamentoMensal(mesFechamento, valorRendaMensal,
						this.calculoMensalRepository, indexBean.getDividaRepository());
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Fechamento mensal", "Fechamento executado com sucesso."));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Fechamento mensal", e.getMessage()));
			}
			this.valorRendaMensal = null;
			setCalculos();
			indexBean.setDividas();
			indexBean.existeCalculoMensal();
		}
	}
	/**/

}

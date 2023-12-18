package com.github.macgarcia.web.reports;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.github.macgarcia.web.model.CalculoMensal;
import com.github.macgarcia.web.repository.CalculoMensalRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> parameters;

	private CalculoMensal calculoMensal;
	private JasperPrint jasperPrint;

	public void criarRelatorioFechamentoMensalSelecionado(Integer calculoMensalId,
			CalculoMensalRepository calculoMensalRepository) throws JRException, IOException {

		this.buscarCalculoMensalParaRelatorio(calculoMensalId, calculoMensalRepository);
		this.montarParametrosDoRelatorio();
		this.compilarJasperComValores();
		this.converterEmPdfParaTela();
		this.finalizar();
	}

	private void buscarCalculoMensalParaRelatorio(Integer calculoMensalId,
			CalculoMensalRepository calculoMensalRepository) {
		this.calculoMensal = calculoMensalRepository.consultarPorId(CalculoMensal.class, calculoMensalId);
	}

	private void montarParametrosDoRelatorio() {
		this.parameters = new HashMap<>();
		this.parameters.put("MES_SELECIONADO", this.calculoMensal.getMes().name());
		this.parameters.put("VALOR_RENDA_MENSAL", this.calculoMensal.getValorSaldoMensalFormatado());
		this.parameters.put("VALOR_TOTAL_DIVIDAS", this.calculoMensal.getValorTotalDividaFormatado());
		this.parameters.put("SALDO", this.calculoMensal.getValorResultanteFormatado());
	}

	private void compilarJasperComValores() throws JRException {
		InputStream source = Relatorio.class.getResourceAsStream(Relatorio.RELATORIO_DE_FECHAMENTO_MENSAL.getCaminho());
		JasperReport report = JasperCompileManager.compileReport(source);
		List<DividaVo> dividas = this.calculoMensal.getDividas().stream().map(DividaVo::new)
				.collect(Collectors.toList());
		this.jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(dividas));
	}

	private void converterEmPdfParaTela() throws JRException, IOException {
		byte[] relatorioPdf = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=relatorio.pdf");
		response.setContentLength(relatorioPdf.length);

		// Enviar o relatório como resposta
		response.getOutputStream().write(relatorioPdf);
		response.getOutputStream().flush();
		response.getOutputStream().close();

		// Finalizar a resposta JSF
		FacesContext.getCurrentInstance().responseComplete();
	}

	private void finalizar() {
		this.parameters = null;
		this.calculoMensal = null;
		this.jasperPrint = null;
	}
}

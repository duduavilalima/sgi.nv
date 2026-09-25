package br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

/**
 * TO de envio de dados ao backend para a geração de gráficos na tela "Custos > Gráfico Faturamento/Despesas"
 */
public class GraficoFaturamentoDespesasTO extends BaseTO {

	private Integer ano;
	private List<Long> listaCodEmpresa;
	private Long codigoRegional;
	private Long codigoGerencia;
	private Long codigoCl;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<Long> getListaCodEmpresa() {
		return listaCodEmpresa;
	}

	public void setListaCodEmpresa(List<Long> listaCodEmpresa) {
		this.listaCodEmpresa = listaCodEmpresa;
	}

	public Long getCodigoRegional() {
		return codigoRegional;
	}

	public void setCodigoRegional(Long codigoRegional) {
		this.codigoRegional = codigoRegional;
	}

	public Long getCodigoGerencia() {
		return codigoGerencia;
	}

	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}

	public Long getCodigoCl() {
		return codigoCl;
	}

	public void setCodigoCl(Long codigoCl) {
		this.codigoCl = codigoCl;
	}
}

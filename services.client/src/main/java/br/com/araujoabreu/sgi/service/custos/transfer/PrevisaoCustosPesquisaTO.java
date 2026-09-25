package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSimpleTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Objects;

public class PrevisaoCustosPesquisaTO extends BaseSearchTO {

	private DespesaCustosTO despesaCustos;
	private UnidadeSimpleTO unidade;
	private DiretoriaSimpleTO diretoria;
	private GerenciaSimpleTO gerencia;
	private CentroFinanceiroSimpleTO centroFinanceiro;
	private Integer referenciaAno;
	private Integer referenciaMes;
	private BigDecimal valor;

	public PrevisaoCustosPesquisaTO() {
	}

	public DespesaCustosTO getDespesaCustos() {
		return despesaCustos;
	}

	public void setDespesaCustos(DespesaCustosTO despesaCustos) {
		this.despesaCustos = despesaCustos;
	}

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public GerenciaSimpleTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(GerenciaSimpleTO gerencia) {
		this.gerencia = gerencia;
	}

	public CentroFinanceiroSimpleTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroSimpleTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Integer getReferenciaAno() {
		return referenciaAno;
	}

	public void setReferenciaAno(Integer referenciaAno) {
		this.referenciaAno = referenciaAno;
	}

	public Integer getReferenciaMes() {
		return referenciaMes;
	}

	public void setReferenciaMes(Integer referenciaMes) {
		this.referenciaMes = referenciaMes;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PrevisaoCustosPesquisaTO)) return false;
		if (!super.equals(o)) return false;
		PrevisaoCustosPesquisaTO that = (PrevisaoCustosPesquisaTO) o;
		return Objects.equals(getReferenciaAno(), that.getReferenciaAno()) &&
			Objects.equals(getReferenciaMes(), that.getReferenciaMes()) &&
			Objects.equals(getValor(), that.getValor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getReferenciaAno(), getReferenciaMes(), getValor());
	}

	@Override
	public String toString() {
		return "PrevisaoCustosPesquisaTO{" +
			"referenciaAno=" + referenciaAno +
			", referenciaMes=" + referenciaMes +
			", valor=" + valor +
			'}';
	}
}

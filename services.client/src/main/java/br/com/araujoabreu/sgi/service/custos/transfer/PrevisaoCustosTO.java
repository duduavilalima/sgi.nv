package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSimpleTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;

public class PrevisaoCustosTO extends BaseTO {

	private DespesaCustosTO despesaCustos;
	private UnidadeSimpleTO unidade;
	private DiretoriaSimpleTO diretoria;
	private GerenciaSimpleTO gerencia;
	private CentroFinanceiroSimpleTO centroFinanceiro;
	private Integer referenciaAno;
	private Integer referenciaMes;
	private BigDecimal valor;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public PrevisaoCustosTO() {
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

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public static PrevisaoCustosTO.Builder builder() {
		return new PrevisaoCustosTO.Builder();
	}

	public static class Builder {

		private DespesaCustosTO despesaCustos;
		private UnidadeSimpleTO unidade;
		private DiretoriaSimpleTO diretoria;
		private GerenciaSimpleTO gerencia;
		private CentroFinanceiroSimpleTO centroFinanceiro;
		private Integer referenciaAno;
		private Integer referenciaMes;
		private BigDecimal valor;

		public Builder() {
		}

		public PrevisaoCustosTO.Builder despesaCustos(DespesaCustosTO despesaCustos) {
			this.despesaCustos = despesaCustos;
			return this;
		}

		public PrevisaoCustosTO.Builder unidade(UnidadeSimpleTO unidade) {
			this.unidade = unidade;
			return this;
		}

		public PrevisaoCustosTO.Builder diretoria(DiretoriaSimpleTO diretoria) {
			this.diretoria = diretoria;
			return this;
		}

		public PrevisaoCustosTO.Builder gerencia(GerenciaSimpleTO gerencia) {
			this.gerencia = gerencia;
			return this;
		}

		public PrevisaoCustosTO.Builder centroFinanceiro(CentroFinanceiroSimpleTO centroFinanceiro) {
			this.centroFinanceiro = centroFinanceiro;
			return this;
		}

		public PrevisaoCustosTO.Builder referenciaAno(Integer referenciaAno) {
			this.referenciaAno = referenciaAno;
			return this;
		}

		public PrevisaoCustosTO.Builder referenciaMes(Integer referenciaMes) {
			this.referenciaMes = referenciaMes;
			return this;
		}

		public PrevisaoCustosTO.Builder valor(BigDecimal valor) {
			this.valor = valor;
			return this;
		}

		public PrevisaoCustosTO build() {
			PrevisaoCustosTO obj = new PrevisaoCustosTO();
			obj.despesaCustos = this.despesaCustos;
			obj.unidade = this.unidade;
			obj.diretoria = this.diretoria;
			obj.gerencia = this.gerencia;
			obj.centroFinanceiro = this.centroFinanceiro;
			obj.referenciaAno = this.referenciaAno;
			obj.referenciaMes = this.referenciaMes;
			obj.valor = this.valor;
			return obj;
		}

	}
}

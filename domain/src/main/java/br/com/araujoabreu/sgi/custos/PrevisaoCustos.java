package br.com.araujoabreu.sgi.custos;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "PREVISAO_CUSTOS")
@SequenceGenerator(name = "SEQ_DESPESA_CUSTOS", sequenceName = "SEQ_DESPESA_CUSTOS", allocationSize = 1)
public class PrevisaoCustos extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -1222370147761100453L;

	private DespesaCustos despesaCustos;
	private Unidade unidade;
	private Diretoria diretoria;
	private Gerencia gerencia;
	private CentroFinanceiro centroFinanceiro;
	private Integer referenciaAno;
	private Integer referenciaMes;
	private BigDecimal valor;

	public PrevisaoCustos() {
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_despesa_custos", nullable = false)
	public DespesaCustos getDespesaCustos() {
		return despesaCustos;
	}

	public void setDespesaCustos(DespesaCustos despesaCustos) {
		this.despesaCustos = despesaCustos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_unidade")
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_diretoria")
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_gerencia")
	public Gerencia getGerencia() {
		return gerencia;
	}

	public void setGerencia(Gerencia gerencia) {
		this.gerencia = gerencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_centro_financeiro")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@NotNull
	@Column(name = "referencia_ano", nullable = false)
	public Integer getReferenciaAno() {
		return referenciaAno;
	}

	public void setReferenciaAno(Integer referenciaAno) {
		this.referenciaAno = referenciaAno;
	}

	@NotNull
	@Column(name = "referencia_mes", nullable = false)
	public Integer getReferenciaMes() {
		return referenciaMes;
	}

	public void setReferenciaMes(Integer referenciaMes) {
		this.referenciaMes = referenciaMes;
	}

	@NotNull
	@Column(name = "valor", nullable = false)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PrevisaoCustos)) return false;
		if (!super.equals(o)) return false;
		PrevisaoCustos that = (PrevisaoCustos) o;
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
		return "PrevisaoCustos{" +
			"referenciaAno=" + referenciaAno +
			", referenciaMes=" + referenciaMes +
			", valor=" + valor +
			'}';
	}

	public static PrevisaoCustos.Builder builder() {
		return new PrevisaoCustos.Builder();
	}

	public static class Builder {

		private DespesaCustos despesaCustos;
		private Unidade unidade;
		private Diretoria diretoria;
		private Gerencia gerencia;
		private CentroFinanceiro centroFinanceiro;
		private Integer referenciaAno;
		private Integer referenciaMes;
		private BigDecimal valor;

		public Builder() {
		}

		public PrevisaoCustos.Builder despesaCustos(DespesaCustos despesaCustos){
			this.despesaCustos = despesaCustos;
			return this;
		}

		public PrevisaoCustos.Builder unidade(Unidade unidade){
			this.unidade = unidade;
			return this;
		}

		public PrevisaoCustos.Builder diretoria(Diretoria diretoria){
			this.diretoria = diretoria;
			return this;
		}

		public PrevisaoCustos.Builder gerencia(Gerencia gerencia){
			this.gerencia = gerencia;
			return this;
		}

		public PrevisaoCustos.Builder centroFinanceiro(CentroFinanceiro centroFinanceiro){
			this.centroFinanceiro = centroFinanceiro;
			return this;
		}

		public PrevisaoCustos.Builder referenciaAno(Integer referenciaAno){
			this.referenciaAno = referenciaAno;
			return this;
		}

		public PrevisaoCustos.Builder referenciaMes(Integer referenciaMes){
			this.referenciaMes = referenciaMes;
			return this;
		}

		public PrevisaoCustos.Builder valor(BigDecimal valor){
			this.valor = valor;
			return this;
		}

		public PrevisaoCustos build() {
			PrevisaoCustos obj = new PrevisaoCustos();
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

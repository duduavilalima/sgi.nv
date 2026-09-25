package br.com.araujoabreu.sgi.custos;

import br.com.araujoabreu.sgi.custos.enums.OperacaoDespesaCustos;
import br.com.araujoabreu.sgi.custos.enums.SituacaoDespesaCustos;
import br.com.araujoabreu.sgi.custos.enums.TipoDespesaCustos;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "DESPESA_CUSTOS")
@SequenceGenerator(name = "SEQ_PREVISAO_CUSTOS", sequenceName = "SEQ_PREVISAO_CUSTOS", allocationSize = 1)
public class DespesaCustos extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 1184854786485132951L;

	private DespesaCustos pai;
	private String descricao;
	private Integer ordem = 1;
	private SituacaoDespesaCustos situacao = SituacaoDespesaCustos.ATIVA;
	private TipoDespesaCustos tipo;
	private OperacaoDespesaCustos operacao;

	public DespesaCustos() {
	}

	public DespesaCustos(DespesaCustos pai, String descricao, Integer ordem, SituacaoDespesaCustos situacao, TipoDespesaCustos tipo, OperacaoDespesaCustos operacao) {
		this.pai = pai;
		this.descricao = descricao;
		this.ordem = ordem;
		this.situacao = situacao;
		this.tipo = tipo;
		this.operacao = operacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_pai", nullable = false)
	public DespesaCustos getPai() {
		return pai;
	}

	public void setPai(DespesaCustos pai) {
		this.pai = pai;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "ORDEM", nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name = "SITUACAO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	public SituacaoDespesaCustos getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoDespesaCustos situacao) {
		this.situacao = situacao;
	}

	@Column(name = "TIPO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	public TipoDespesaCustos getTipo() {
		return tipo;
	}

	public void setTipo(TipoDespesaCustos tipo) {
		this.tipo = tipo;
	}

	@Column(name = "OPERACAO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	public OperacaoDespesaCustos getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacaoDespesaCustos operacao) {
		this.operacao = operacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DespesaCustos)) return false;
		if (!super.equals(o)) return false;
		DespesaCustos that = (DespesaCustos) o;
		return Objects.equals(getDescricao(), that.getDescricao()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			getSituacao() == that.getSituacao() &&
			getTipo() == that.getTipo() &&
			getOperacao() == that.getOperacao();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao(), getOrdem(), getSituacao(), getTipo(), getOperacao());
	}

	@Override
	public String toString() {
		return "DespesaCustos{" +
			"pai=" + pai +
			", descricao='" + descricao + '\'' +
			", ordem=" + ordem +
			", situacao=" + situacao +
			", tipo=" + tipo +
			", operacao=" + operacao +
			'}';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private DespesaCustos pai;
		private String descricao;
		private Integer ordem = 1;
		private SituacaoDespesaCustos situacao = SituacaoDespesaCustos.ATIVA;
		private TipoDespesaCustos tipo;
		private OperacaoDespesaCustos operacao;

		public Builder() {
		}

		public Builder pai(DespesaCustos despesaCustos){
			this.pai = despesaCustos;
			return this;
		}

		public Builder descricao(String descricao){
			this.descricao = descricao;
			return this;
		}

		public Builder ordem(Integer ordem){
			this.ordem = ordem;
			return this;
		}

		public Builder situacao(SituacaoDespesaCustos situacao){
			this.situacao = situacao;
			return this;
		}

		public Builder tipo(TipoDespesaCustos tipo){
			this.tipo = tipo;
			return this;
		}

		public Builder operacao(OperacaoDespesaCustos operacao){
			this.operacao = operacao;
			return this;
		}
		public DespesaCustos build() {
			DespesaCustos obj = new DespesaCustos();
			obj.pai = this.pai;
			obj.descricao = this.descricao;
			obj.ordem = this.ordem;
			obj.situacao = this.situacao;
			obj.tipo = this.tipo;
			obj.operacao = this.operacao;
			return obj;
		}

	}
}

package br.com.araujoabreu.sgi.fluxoCaixa.domain;

import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "DESPESA_FLUXO_CAIXA")
@SequenceGenerator(name="SEQ_DESPESA_FLUXO_CAIXA", sequenceName="SEQ_DESPESA_FLUXO_CAIXA", allocationSize=1)
public class DespesaFluxoCaixa extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -394889716777774193L;
	private String sigla;
	private String descricao;
	private Situacao situacao;

	public DespesaFluxoCaixa() {

	}

	@Column(name = "SIGLA", length = 2)
	@NotNull
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "DESCRICAO", length = 30)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SITUACAO")
	@Enumerated(EnumType.ORDINAL)
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DespesaFluxoCaixa)) return false;
		if (!super.equals(o)) return false;
		DespesaFluxoCaixa that = (DespesaFluxoCaixa) o;
		return Objects.equals(getSigla(), that.getSigla()) &&
			Objects.equals(getDescricao(), that.getDescricao()) &&
			getSituacao() == that.getSituacao();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSigla(), getDescricao(), getSituacao());
	}
}

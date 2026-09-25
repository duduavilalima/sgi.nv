package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "RELACAO_NF_NF")
@SequenceGenerator(name="SEQ_ITEM_RELACAO_NF", sequenceName="SEQ_ITEM_RELACAO_NF", allocationSize=1)
public class ItemRelacaoNotaFiscal extends BaseEntity implements Detail {

	private static final long serialVersionUID = -8188870312016611521L;
	private NotaFiscal notaFiscal;
	private RelacaoNotaFiscal relacaoNotaFiscal;
	private Boolean recebida = false;
	private Boolean selecao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA", nullable = false)
	@NotNull
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RELACAO_NF", nullable = false)
	@NotNull
	public RelacaoNotaFiscal getRelacaoNotaFiscal() {
		return relacaoNotaFiscal;
	}

	public void setRelacaoNotaFiscal(RelacaoNotaFiscal relacaoNotaFiscal) {
		this.relacaoNotaFiscal = relacaoNotaFiscal;
	}

	@Column(name = "RECEBIDA", nullable = false)
	public Boolean getRecebida() {
		return recebida;
	}

	public void setRecebida(Boolean recebida) {
		this.recebida = recebida;
	}

	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	public void setPrincipal(Boolean principal) {
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Transient
	public boolean isCentroFinanceiroUnico() {
		if (notaFiscal != null) {
			return notaFiscal.isCentroFinanceiroUnico();
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(ItemRelacaoNotaFiscal.class.isAssignableFrom(Hibernate.getClass(o)))) {
			return false;
		}
		if (!(o instanceof ItemRelacaoNotaFiscal)) return false;
		ItemRelacaoNotaFiscal that = (ItemRelacaoNotaFiscal) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getRecebida(), that.getRecebida()) &&
			Objects.equals(getSelecao(), that.getSelecao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getRecebida(), getSelecao());
	}
}

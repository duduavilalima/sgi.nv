package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ITEM_RELACAO_PAGAMENTO")
@SequenceGenerator(name = "SEQ_ITEM_RELACAO_PAGAMENTO", sequenceName = "SEQ_ITEM_RELACAO_PAGAMENTO", allocationSize = 1)
public class ItemRelacaoPagamento extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = -6841437379862260075L;

	private Pagamento pagamento;
	private ItemRelacao itemRelacao;

	public ItemRelacaoPagamento() {
	}

	public ItemRelacaoPagamento(final Pagamento pagamento, final ItemRelacao itemRelacao) {
		this.pagamento = pagamento;
		this.itemRelacao = itemRelacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	@NotNull
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(final Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_RELACAO")
	@NotNull
	public ItemRelacao getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(final ItemRelacao itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ItemRelacaoPagamento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemRelacaoPagamento other = (ItemRelacaoPagamento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}

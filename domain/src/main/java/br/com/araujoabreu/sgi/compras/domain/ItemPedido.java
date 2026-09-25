package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o Item de um Pedido que será enviado para aquisição.
 *
 * @author Rodrigo Barreiros
 * @since 1.0
 */
@Entity
@Table(name = "ITEM_PEDIDO")
@SequenceGenerator(name="SEQ_ITEM_PEDIDO", sequenceName="SEQ_ITEM_PEDIDO", allocationSize=1)
public class ItemPedido extends BaseEntity {

	private static final long serialVersionUID = 6060866280244247359L;
	private BigDecimal diferenca;
	private ItemProposta itemProposta;
	private Pedido pedido;
	private List<ItemRecebimento> itens = new ArrayList<>(0);

	@Column(name = "DIFERENCA", precision = 11, scale = 2)
	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_PROPOSTA")
	@NotNull
	public ItemProposta getItemProposta() {
		return itemProposta;
	}

	public void setItemProposta(ItemProposta itemProposta) {
		this.itemProposta = itemProposta;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PEDIDO")
	@NotNull
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemPedido")
	public List<ItemRecebimento> getItens() {
		return itens;
	}

	public void setItens(List<ItemRecebimento> itens) {
		this.itens = itens;
	}


	@Transient
	public BigDecimal getQuantidade() {
		BigDecimal total = BigDecimal.ZERO;
		if (getQuantidadeRecebida() != null && itemProposta != null) {
			total = itemProposta.getQuantidadeSolicitada().subtract(getQuantidadeRecebida());
		}
		return total;
	}

	@Transient
	public BigDecimal getValor() {
		if (itemProposta.getPreco() != null) {
			return itemProposta.getPreco().multiply(getQuantidade());
		}
		return null;
	}


	@Transient
	public BigDecimal getQuantidadeRecebida() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemRecebimento item : getItens()) {
			total = total.add((item.getQuantidade() == null ? BigDecimal.ZERO : item.getQuantidade()));
		}

		return total;
	}

	@Transient
	public BigDecimal getQuantidadeRecebidaOutrosRecebimentos(ItemRecebimento irec) {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemRecebimento item : getItens()) {
			if (!item.equals(irec))
				total = total.add((item.getQuantidade() == null ? BigDecimal.ZERO : item.getQuantidade()));
		}

		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ItemPedido.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemPedido other = (ItemPedido) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemPedido [codigo=" + getCodigo() + ", diferenca=" + diferenca + "]";
	}

}

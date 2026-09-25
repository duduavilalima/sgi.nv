package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Representa o recebimento de um determinado Pedido.
 * Um recebimento poderá ser total, quando existe apenas um recebimento para todo o Pedido,
 * ou um recebimento Parcial, onde se faz necessário realizar vários recebimentos para compor
 * Pedido.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "RECEBIMENTO")
@SequenceGenerator(name="SEQ_RECEBIMENTO", sequenceName="SEQ_RECEBIMENTO", allocationSize=1)
public class Recebimento extends AuditEntity {

	private static final long serialVersionUID = -249911337323839643L;
	private String observacao;
	private Status status;
	private Colaborador recebedor;
	private NotaFiscal notaFiscal;
	private Pedido pedido;
	private List<ItemRecebimento> itens = new ArrayList<>(0);
	private int version;

	public Recebimento() {
	}

	public Recebimento(Long recebimentoCodigo) {
		setCodigo(recebimentoCodigo);
	}

	public void addItem(ItemRecebimento itemRecebimento) {
		itemRecebimento.setRecebimento(this);
		itens.add(itemRecebimento);
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "STATUS")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RECEBEDOR")
	public Colaborador getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(Colaborador recebedor) {
		this.recebedor = recebedor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_FISCAL")
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recebimento")
	public List<ItemRecebimento> getItens() {
		if (itens != null && !itens.isEmpty()) {
			Comparator<ItemRecebimento> comparator = (o1, o2) -> o1.getItemPedido().getItemProposta().getDescricao().compareToIgnoreCase(o2.getItemPedido().getItemProposta().getDescricao());

			itens.sort(comparator);
		}
		return itens;
	}

	public void setItens(List<ItemRecebimento> itens) {
		this.itens = itens;
	}

	/**
	 * Atributo utilizado para controle de concorrencia
	 *
	 * @return
	 */
	@Version
	public int getVersion() {
		return version;
	}

	/**
	 * Determina a versão a entidade FundoFixo
	 *
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Transient
	public Date getDataRecebimento() {
		return getDataCriacao();
	}

	@Transient
	public boolean isFinalizado() {
		return Status.FINALIZADO.equals(this.status);
	}

	/**
	 * Retorna a soma dos valores de cada item do recebimento.
	 *
	 * @return o valor total do recebimento
	 */
	@Transient
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemRecebimento itemRecebimento : itens) {
			if (itemRecebimento.getValor() != null) {
				total = total.add(itemRecebimento.getValor());

			}
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof Recebimento)) {
			return false;
		}

		Recebimento other = (Recebimento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	public enum Status {RECEBIDO, FINALIZADO}

}

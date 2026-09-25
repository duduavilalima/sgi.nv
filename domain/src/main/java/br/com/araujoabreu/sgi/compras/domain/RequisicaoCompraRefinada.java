package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a Requisição de Compra refinada por natureza de fornecimento. O objeto é
 * agrupar os itens da requisição por natureza. Assim, se
 *
 * @author Administrator
 */
@Entity
@Table(name = "REQUISICAO_COMPRA_REFINADA")
@SequenceGenerator(name="SEQ_REQUISICAO_COMPRA_REFINADA", sequenceName="SEQ_REQUISICAO_COMPRA_REFINADA", allocationSize=1)
public class RequisicaoCompraRefinada extends AuditEntity implements Detail {

	private static final long serialVersionUID = 8814039275541169470L;
	private RequisicaoCompra requisicaoCompra;
	private NaturezaFornecimento naturezaFornecimento;
	private OrdemCompra ordemCompra;
	private List<ItemRequisicaoCompra> itens = new ArrayList<>(0);

	public void addItem(ItemRequisicaoCompra itemRequisicaoCompra) {
		itemRequisicaoCompra.setRequisicaoCompraRefinada(this);
		itens.add(itemRequisicaoCompra);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_REQUISICAO_COMPRA")
	@NotNull
	public RequisicaoCompra getRequisicaoCompra() {
		return requisicaoCompra;
	}

	public void setRequisicaoCompra(RequisicaoCompra requisicaoCompra) {
		this.requisicaoCompra = requisicaoCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_FORNECIMENTO")
	@NotNull
	public NaturezaFornecimento getNaturezaFornecimento() {
		return naturezaFornecimento;
	}

	public void setNaturezaFornecimento(NaturezaFornecimento naturezaFornecimento) {
		this.naturezaFornecimento = naturezaFornecimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ORDEM_COMPRA")
	public OrdemCompra getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompra ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requisicaoCompraRefinada")
	public List<ItemRequisicaoCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemRequisicaoCompra> itens) {
		this.itens = itens;
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
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
		if (!(RequisicaoCompraRefinada.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		RequisicaoCompraRefinada other = (RequisicaoCompraRefinada) obj;
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
		return "RequisicaoCompraRefinada [codigo=" + getCodigo() + ", requisicaoCompra=" + requisicaoCompra + ", naturezaFornecimento="
			+ naturezaFornecimento + "]";
	}

}

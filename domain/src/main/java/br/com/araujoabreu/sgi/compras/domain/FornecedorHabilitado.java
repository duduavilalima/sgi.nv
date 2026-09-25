package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Identifica um fornecedor habilitado em um processo de cotação.
 * <br>
 * A natureza de fornecimento da Ordem de Compra relaciona os fornecedores habilitados.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "FORNECEDOR_HABILITADO")
@SequenceGenerator(name="SEQ_FORNECEDOR_HABILITADO", sequenceName="SEQ_FORNECEDOR_HABILITADO", allocationSize=1)
public class FornecedorHabilitado extends BaseEntity implements Detail {

	private static final long serialVersionUID = 1542318406875118686L;
	private OrdemCompra ordemCompra;
	private Pessoa fornecedor;
	private Boolean notificadoCotacao = Boolean.FALSE;
	private Boolean selected = false;
	private int indice;
	private BigDecimal desempenho;

	/**
	 * Referencia a ordem de compra que define a natureza de fornecimento
	 *
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ORDEM")
	@NotNull
	public OrdemCompra getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompra ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	/**
	 * Referencia ao fornecedor habilitado na natureza de fornecimento da Ordem de Compra
	 *
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FORNECEDOR")
	@NotNull
	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * Indica se o fornecedor foi notificado do processo de cotação.
	 *
	 * @return
	 */
	@Column(name = "NOTIFICADO_COTACAO", nullable = false, length = 1)
	@NotNull
	public Boolean getNotificadoCotacao() {
		return notificadoCotacao;
	}

	public void setNotificadoCotacao(Boolean notificado) {
		this.notificadoCotacao = notificado;
	}

	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	public void setPrincipal(Boolean principal) {
	}

	@Transient
	public BigDecimal getDesempenho() {
		return desempenho;
	}

	public void setDesempenho(BigDecimal desempenho) {
		this.desempenho = desempenho;
	}

	@Transient
	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}


	@Transient
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FornecedorHabilitado)) return false;
		if (!super.equals(o)) return false;
		FornecedorHabilitado that = (FornecedorHabilitado) o;
		return getIndice() == that.getIndice() &&
			Objects.equals(getNotificadoCotacao(), that.getNotificadoCotacao()) &&
			Objects.equals(getSelected(), that.getSelected()) &&
			Objects.equals(getDesempenho(), that.getDesempenho());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNotificadoCotacao(), getSelected(), getIndice(), getDesempenho());
	}
}

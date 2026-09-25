package br.com.araujoabreu.sgi.fundoFixo.domain;

import br.com.araujoabreu.sgi.administracao.domain.NaturezaDespesa;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representa um item de um demonstrativo de despesa do fundo fixo. Cada item está atrelado a uma Despesa e tem
 * um classificador que determina a natureza dessa despesa.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "ITEM_DEMONSTRATIVO")
@SequenceGenerator(name = "SEQ_ITEM_DEMONSTRATIVO", sequenceName = "SEQ_ITEM_DEMONSTRATIVO", allocationSize = 1)
public class ItemDemonstrativo extends BaseEntity implements Detail {

	private static final long serialVersionUID = 2965556187724999525L;
	private NaturezaDespesa naturezaDespesa;
	private Demonstrativo demonstrativo;
	private NotaFiscal notaFiscal;
	private Pessoa pessoa;
	private int comprovante;
	private BigDecimal valorAgrupado;
	private Date dataDespesa;
	private BigDecimal valor;
	private BigDecimal valorHomologado;
	private String descricao;

	public ItemDemonstrativo() {
		super();
	}

	public ItemDemonstrativo(Long codigo) {
		super();
		setCodigo(codigo);
	}

	public ItemDemonstrativo(NotaFiscal notaFiscal) {
		super();
		this.notaFiscal = notaFiscal;
	}

	public ItemDemonstrativo(NaturezaDespesa naturezaDespesa,
							 Demonstrativo demonstrativo, NotaFiscal notaFiscal, Pessoa pessoa,
							 int comprovante) {
		super();
		this.naturezaDespesa = naturezaDespesa;
		this.demonstrativo = demonstrativo;
		this.notaFiscal = notaFiscal;
		this.pessoa = pessoa;
		this.comprovante = comprovante;
	}

	/**
	 * Demonstrativo dessa despesa
	 *
	 * @return O demonstrativo dessa despesa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_demonstrativo")
	@NotNull
	public Demonstrativo getDemonstrativo() {
		return demonstrativo;
	}

	/**
	 * Ajustar o demonstrativo dessa despesa.
	 *
	 * @param demostrativo O demonstrativo dessa despesa.
	 */
	public void setDemonstrativo(Demonstrativo demostrativo) {
		this.demonstrativo = demostrativo;
	}

	/**
	 * Classificação que determina a natureza da despesa.
	 *
	 * @return A classificação que determina a natureza da despesa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_DESPESA")
	@NotNull
	public NaturezaDespesa getNaturezaDespesa() {
		return naturezaDespesa;
	}

	/**
	 * Ajustar a classificação que determina a natureza da despesa.
	 *
	 * @param natureza A classificação que determina a natureza da despesa.
	 */
	public void setNaturezaDespesa(final NaturezaDespesa natureza) {
		this.naturezaDespesa = natureza;
	}

	/**
	 * Obter o fornecedor desta nota.
	 *
	 * @return O fornecedor desta nota.
	 */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Pessoa.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
	@JoinColumn(name = "CODIGO_FORNECEDOR")
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * Ajustar o fornecedor desta nota.
	 *
	 * @param pessoa O fornecedor desta nota.
	 */
	public void setPessoa(final Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * O tipo de comprovante do item de demonstrativo.
	 *
	 * @return O tipo de comprovante do item de demonstrativo.
	 */
	@Column(name = "comprovante", nullable = false)
	@NotNull
	public int getComprovante() {
		return comprovante;
	}

	/**
	 * Ajustar o tipo de comprovante do item de demonstrativo.
	 *
	 * @param comprovante O tipo de comprovante do item de demonstrativo.
	 */
	public void setComprovante(final int comprovante) {
		this.comprovante = comprovante;
	}

	/**
	 * Obter descrição do comprovante.
	 *
	 * @return A descrição do comprovante.
	 */
	@Transient
	public String getComprovanteTipo() {
		switch (this.comprovante) {
			case 0:
				return ComprovanteEnum.OU.getDescricao();
			case 1:
				return ComprovanteEnum.CF.getDescricao();
			case 2:
				return ComprovanteEnum.NF.getDescricao();
			default:
				return "";
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_DESPESA", length = 7)
	public Date getDataDespesa() {
		return dataDespesa;
	}

	public void setDataDespesa(Date dataDespesa) {
		this.dataDespesa = dataDespesa;
	}

	@Column(name = "VALOR", precision = 9, scale = 2)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "VALOR_HOMOLOGADO", precision = 9, scale = 2)
	public BigDecimal getValorHomologado() {
		return valorHomologado;
	}

	public void setValorHomologado(BigDecimal valorHomologado) {
		this.valorHomologado = valorHomologado;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(final Boolean principal) {

	}

	/**
	 * Verifica se item é um Cupom Fiscal.
	 *
	 * @return <code>true</code> caso afirmativo. <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isCupomFiscal() {
		return ComprovanteEnum.CF.getCodigo() == getComprovante();
	}

	/**
	 * Verifica se item é um comprovante "Outro".
	 *
	 * @return <code>true</code> caso afirmativo. <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isOutro() {
		return ComprovanteEnum.OU.getCodigo() == getComprovante();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + comprovante;
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
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
		if (!(ItemDemonstrativo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemDemonstrativo other = (ItemDemonstrativo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (comprovante != other.getComprovante()) {
			return false;
		}
		if (pessoa == null) {
			if (other.getPessoa() != null) {
				return false;
			}
		} else if (!pessoa.equals(other.getPessoa())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		return "ItemDemonstrativo [codigo=" + getCodigo()
			+ ", tipo= " + naturezaDespesa.getTipoNaturezaDespesa()
			+ ", natureza= " + naturezaDespesa.getNome()
			+ ", data= " + (dataDespesa == null ? "" : sdf.format(dataDespesa))
			+ ", valor= " + valor
			+ ", comprovante=" + getComprovanteTipo() + "]";
	}

	@Transient
	public BigDecimal getValorAgrupado() {
		return valorAgrupado;
	}

	public void setValorAgrupado(BigDecimal valorAgrupado) {
		this.valorAgrupado = valorAgrupado;
	}

	/**
	 * Tipo do comprovante.
	 *
	 * @author daniel.oliveira
	 */
	public enum ComprovanteEnum {
		/**
		 * Outro.
		 */
		OU(0, "Outros"),
		/**
		 * Cupom Fiscal.
		 */
		CF(1, "Cupom Fiscal"),
		/**
		 * Nota Fiscal.
		 */
		NF(2, "Nota Fiscal");

		private final int codigo;
		private final String descricao;

		private ComprovanteEnum(final int codigo, final String desc) {
			this.codigo = codigo;
			this.descricao = desc;
		}

		/**
		 * Obter o código do comprovante.
		 *
		 * @return O código do comprovante.
		 */
		public int getCodigo() {
			return this.codigo;
		}

		/**
		 * Obter a descrição do comprovante.
		 *
		 * @return A descrição do comprovante.
		 */
		public String getDescricao() {
			return this.descricao;
		}

		@Override
		public String toString() {
			return this.descricao;
		}
	}

}

package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento;
import br.com.araujoabreu.sgi.administracao.domain.SubTipoServico;
import br.com.araujoabreu.sgi.administracao.domain.TipoItemCompra;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.ItemCompraContaContabil.TipoRelacionamentoItemCompra;
import br.com.araujoabreu.sgi.compras.interf.Material;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ITEM_COMPRA")
@SequenceGenerator(name="SEQ_ITEM_COMPRA", sequenceName="SEQ_ITEM_COMPRA", allocationSize=1)
public class ItemCompra extends AuditEntity implements Material, Cloneable {

	private static final long serialVersionUID = -5063774403885006522L;
	private String sigla;
	private String nome;
	private NaturezaFornecimento naturezaFornecimento;
	private CategoriaItemCompra categoriaItemCompra;
	private UnidadeMedida unidadeMedida;
	private String codigoBarra;
	private TipoItemCompra tipoItem;
	private TipoItemCompra tipoItemAmplo;
	private TipoItemCompra tipoItemPadrao;
	private SubTipoServico subTipoServico;
	private Ncm ncm;
	private Boolean situacao;
	private Date dataHomologacao;
	private ContaContabil contaContabilCentroCusto;
	private ContaContabil contaContabilCentroLucro;
	private List<ItemRequisicaoCompra> itensRequisicaoCompra;
	private Boolean permitirRC;
	private Boolean selecao;
	private Boolean movimentacao;
	private List<ItemCompraContaContabil> listaContaContabil = new ArrayList<>();

	@Column(name = "SIGLA", length = 10)
	@Length(max = 10)
	@NotNull
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "NOME")
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
	@JoinColumn(name = "CODIGO_CATEGORIA_ITEM_COMPRA")
	@NotNull
	public CategoriaItemCompra getCategoriaItemCompra() {
		return categoriaItemCompra;
	}

	public void setCategoriaItemCompra(CategoriaItemCompra categoriaItemCompra) {
		this.categoriaItemCompra = categoriaItemCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE_MEDIDA")
	@NotNull
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	@Transient
	public TipoMaterial getTipo() {
		return TipoMaterial.PRODUTO;
	}

	@Transient
	public CategoriaItemCompra getPai() {
		return this.categoriaItemCompra;
	}

	@Transient
	public List<Material> getChildren() {
		return null;
	}

	public void setChildren(List<Material> list) {
	}

	@Column(name = "CODIGO_BARRA", length = 50)
	@Length(max = 50)
	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_ITEM_COMPRA")
	public TipoItemCompra getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(TipoItemCompra tipoItem) {
		this.tipoItem = tipoItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_ITEM_COMPRA_AMPLO")
	public TipoItemCompra getTipoItemAmplo() {
		return tipoItemAmplo;
	}

	public void setTipoItemAmplo(TipoItemCompra tipoItemAmplo) {
		this.tipoItemAmplo = tipoItemAmplo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SUB_TIPO_SERVICO")
	public SubTipoServico getSubTipoServico() {
		return subTipoServico;
	}

	public void setSubTipoServico(SubTipoServico subTipoServico) {
		this.subTipoServico = subTipoServico;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NCM")
	public Ncm getNcm() {
		return ncm;
	}

	public void setNcm(Ncm ncm) {
		this.ncm = ncm;
	}

	@Column(name = "situacao")
	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_homologacao")
	public Date getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(Date dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	@Transient
	public Boolean isLoaded() {
		return true;
	}

	@Transient
	public ContaContabil getContaContabilCentroLucro() {
		return contaContabilCentroLucro;
	}

	public void setContaContabilCentroLucro(ContaContabil contaContabilCentroLucro) {
		this.contaContabilCentroLucro = contaContabilCentroLucro;
	}

	@Transient
	public ContaContabil getContaContabilCentroCusto() {
		return contaContabilCentroCusto;
	}

	public void setContaContabilCentroCusto(ContaContabil contaContabilCentroCusto) {
		this.contaContabilCentroCusto = contaContabilCentroCusto;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemCompra")
	public List<ItemRequisicaoCompra> getItensRequisicaoCompra() {
		return itensRequisicaoCompra;
	}

	public void setItensRequisicaoCompra(List<ItemRequisicaoCompra> itensRequisicaoCompra) {
		this.itensRequisicaoCompra = itensRequisicaoCompra;
	}

	@Column(name = "PERMITIR_RC")
	@NotNull
	public Boolean getPermitirRC() {
		return permitirRC;
	}

	public void setPermitirRC(Boolean permitirRC) {
		this.permitirRC = permitirRC;
	}

	@Column(name = "MOVIMENTACAO")
	public Boolean getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Boolean movimentacao) {
		this.movimentacao = movimentacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemCompra")
	public List<ItemCompraContaContabil> getListaContaContabil() {
		return listaContaContabil;
	}

	public void setListaContaContabil(List<ItemCompraContaContabil> listaContaContabil) {
		this.listaContaContabil = listaContaContabil;
	}

	@Transient
	public ContaContabil getContaContabilPorEmpresa(Unidade empresa) {
		for (ItemCompraContaContabil icc : this.listaContaContabil) {
			if (icc.getContaContabil().getEmpresa().equals(empresa))
				return icc.getContaContabil();
		}
		return null;
	}

	@Transient
	public ContaContabil getContaContabilPorEmpresaTipo(Unidade empresa, TipoRelacionamentoItemCompra tipoRelacionamentoItemCompra) {
		for (ItemCompraContaContabil icc : this.listaContaContabil) {
			if (icc.getContaContabil().getEmpresa().equals(empresa) && icc.getTipo().equals(tipoRelacionamentoItemCompra))
				return icc.getContaContabil();
		}
		return null;
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Transient
	public String getDescricaoPermitirRC() {
		if (permitirRC != null && permitirRC) {
			return "Sim";
		}
		return "Não";
	}

	@Transient
	public String getDescricaoSituacao() {
		if (situacao != null && situacao) {
			return "Ativo";
		}
		return "Inativo";
	}

	@Transient
	public TipoItemCompra getTipoItemPadrao() {
		return tipoItemPadrao;
	}

	public void setTipoItemPadrao(TipoItemCompra tipoItemPadrao) {
		this.tipoItemPadrao = tipoItemPadrao;
	}

	@Override
	public ItemCompra clone() throws CloneNotSupportedException {
		return (ItemCompra) super.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemCompra)) return false;
		if (!super.equals(o)) return false;
		ItemCompra that = (ItemCompra) o;
		return Objects.equals(getSigla(), that.getSigla()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getCodigoBarra(), that.getCodigoBarra()) &&
			Objects.equals(getSituacao(), that.getSituacao()) &&
			Objects.equals(getDataHomologacao(), that.getDataHomologacao()) &&
			Objects.equals(getPermitirRC(), that.getPermitirRC()) &&
			Objects.equals(getSelecao(), that.getSelecao()) &&
			Objects.equals(getMovimentacao(), that.getMovimentacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSigla(), getNome(), getCodigoBarra(), getSituacao(), getDataHomologacao(), getPermitirRC(), getSelecao(), getMovimentacao());
	}
}

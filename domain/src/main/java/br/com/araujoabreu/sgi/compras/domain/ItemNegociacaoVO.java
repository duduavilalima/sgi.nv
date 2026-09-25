package br.com.araujoabreu.sgi.compras.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemNegociacaoVO implements Comparable<ItemNegociacaoVO> {

	private Long codigo;
	private String descricaoAbreviada;
	private String descricao;
	private String nomeCadastro;
	private String unidade;
	private String marca;
	private BigDecimal quantidade;
	private String fornecedorMenorPrecoNaoSelecionado = null;
	private BigDecimal valorMenorPrecoNapSelecionado = new BigDecimal(0);
	private ItemFornecedorVO itemFornecedor01 = new ItemFornecedorVO();
	private ItemFornecedorVO itemFornecedor02 = new ItemFornecedorVO();
	private ItemFornecedorVO itemFornecedor03 = new ItemFornecedorVO();
	private ItemFornecedorVO itemFornecedor04 = new ItemFornecedorVO();
	private ItemFornecedorVO itemFornecedor05 = new ItemFornecedorVO();
	private ItemCompra itemCompra;

	public ItemNegociacaoVO(Long codigo, String descricaoAbreviada, String descricao, String nomeCadastro, String unidade, String marca, BigDecimal quantidade, ItemCompra itemCompra) {
		super();
		this.codigo = codigo;
		this.descricaoAbreviada = descricaoAbreviada;
		this.descricao = descricao;
		this.nomeCadastro = nomeCadastro;
		this.unidade = unidade;
		this.quantidade = quantidade;
		this.marca = marca;
		this.itemCompra = itemCompra;
	}

	public ItemFornecedorVO obterItemSelecionado() {
		for (ItemFornecedorVO itemFornecedorVO : obterFornecedores()) {
			if (itemFornecedorVO.isSelecionado()) {
				return itemFornecedorVO;
			}
		}
		return null;
	}

	public void addItemFornecedorVO(int posicaoFornecedor, Proposta proposta, int posicaoItem) {
		switch (posicaoFornecedor) {
			case 0:
				itemFornecedor01 = montarItemFornecedorVO(proposta, posicaoItem);
				break;
			case 1:
				itemFornecedor02 = montarItemFornecedorVO(proposta, posicaoItem);
				break;
			case 2:
				itemFornecedor03 = montarItemFornecedorVO(proposta, posicaoItem);
				break;
			case 3:
				itemFornecedor04 = montarItemFornecedorVO(proposta, posicaoItem);
				break;
			case 4:
				itemFornecedor05 = montarItemFornecedorVO(proposta, posicaoItem);
				break;
			default:
				break;
		}
	}

	public List<ItemFornecedorVO> obterFornecedores() {
		List<ItemFornecedorVO> list = new ArrayList<>();
		list.add(itemFornecedor01);
		list.add(itemFornecedor02);
		list.add(itemFornecedor03);
		list.add(itemFornecedor04);
		list.add(itemFornecedor05);
		return list;
	}

	private ItemFornecedorVO montarItemFornecedorVO(Proposta proposta, int posicaoItem) {
		if (proposta.getCodigo() == null) {
			return new ItemFornecedorVO();
		}
		return new ItemFornecedorVO(
			proposta.getFornecedor().getCodigo(),
			proposta.getFornecedor().getNome(),
			proposta.getItens().get(posicaoItem).getItemCompra().getCodigo(),
			proposta.getItens().get(posicaoItem).getPrecoNegociado(),
			proposta.getItens().get(posicaoItem).getQuantidadeSolicitada(),
			proposta.getItens().get(posicaoItem).getQuantidadeInformada()
		);
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeCadastro() {
		return nomeCadastro;
	}

	public void setNomeCadastro(String nomeCadastro) {
		this.nomeCadastro = nomeCadastro;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public String getFornecedorMenorPrecoNaoSelecionado() {
		return fornecedorMenorPrecoNaoSelecionado;
	}

	public void setFornecedorMenorPrecoNaoSelecionado(String fornecedorMenorPrecoNaoSelecionado) {
		this.fornecedorMenorPrecoNaoSelecionado = fornecedorMenorPrecoNaoSelecionado;
	}

	public BigDecimal getValorMenorPrecoNapSelecionado() {
		return valorMenorPrecoNapSelecionado;
	}

	public void setValorMenorPrecoNapSelecionado(BigDecimal valorMenorPrecoNapSelecionado) {
		this.valorMenorPrecoNapSelecionado = valorMenorPrecoNapSelecionado;
	}

	public ItemFornecedorVO getItemFornecedor01() {
		return itemFornecedor01;
	}

	public void setItemFornecedor01(ItemFornecedorVO ItemFornecedor01) {
		this.itemFornecedor01 = ItemFornecedor01;
	}

	public ItemFornecedorVO getItemFornecedor02() {
		return itemFornecedor02;
	}

	public void setItemFornecedor02(ItemFornecedorVO ItemFornecedor02) {
		this.itemFornecedor02 = ItemFornecedor02;
	}

	public ItemFornecedorVO getItemFornecedor03() {
		return itemFornecedor03;
	}

	public void setItemFornecedor03(ItemFornecedorVO ItemFornecedor03) {
		this.itemFornecedor03 = ItemFornecedor03;
	}

	public ItemFornecedorVO getItemFornecedor04() {
		return itemFornecedor04;
	}

	public void setItemFornecedor04(ItemFornecedorVO ItemFornecedor04) {
		this.itemFornecedor04 = ItemFornecedor04;
	}

	public ItemFornecedorVO getItemFornecedor05() {
		return itemFornecedor05;
	}

	public void setItemFornecedor05(ItemFornecedorVO ItemFornecedor05) {
		this.itemFornecedor05 = ItemFornecedor05;
	}

	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((descricaoAbreviada == null) ? 0 : descricaoAbreviada.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		ItemNegociacaoVO other = (ItemNegociacaoVO) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (descricaoAbreviada == null) {
			if (other.descricaoAbreviada != null) {
				return false;
			}
		} else if (!descricaoAbreviada.equals(other.descricaoAbreviada)) {
			return false;
		}
		if (quantidade == null) {
			if (other.quantidade != null) {
				return false;
			}
		} else if (!quantidade.equals(other.quantidade)) {
			return false;
		}
		if (unidade == null) {
			if (other.unidade != null) {
				return false;
			}
		} else if (!unidade.equals(other.unidade)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemNegociacaoVO [codigo=" + codigo + ", descricaoAbreviada=" + descricaoAbreviada + ", quantidade=" + quantidade + "]";
	}

	@Override
	public int compareTo(ItemNegociacaoVO o) {
		return 0;
	}

}

package br.com.araujoabreu.sgi.compras.domain;

import java.math.BigDecimal;

public class ItemFornecedorVO implements Comparable<ItemFornecedorVO> {

	private Long codigoItem;
	private Long codigoFornecedor;
	private String nomeFornecedor;
	private BigDecimal quantidadeSolicidata;
	private BigDecimal quantidadeInformada;
	private BigDecimal valorItem;
	private boolean selecionado;

	public ItemFornecedorVO() {
		super();
	}

	public ItemFornecedorVO(Long codigoFornecedor, String nomeFornecedor, Long codigoItem, BigDecimal valorItem, BigDecimal quantidadeSolicitada, BigDecimal quantidade) {
		super();
		this.codigoFornecedor = codigoFornecedor;
		this.nomeFornecedor = nomeFornecedor;
		this.codigoItem = codigoItem;
		this.valorItem = valorItem;
		this.quantidadeSolicidata = quantidadeSolicitada;
		this.quantidadeInformada = quantidade;
	}

	public Long getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(Long codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Long getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(Long codigoItem) {
		this.codigoItem = codigoItem;
	}

	public BigDecimal getValorItem() {
		return valorItem;
	}

	public void setValorItem(BigDecimal valorItem) {
		this.valorItem = valorItem;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public BigDecimal getQuantidadeSolicidata() {
		return quantidadeSolicidata;
	}

	public void setQuantidadeSolicidata(BigDecimal quantidadeSolicidata) {
		this.quantidadeSolicidata = quantidadeSolicidata;
	}

	public BigDecimal getQuantidadeInformada() {
		return quantidadeInformada;
	}

	public void setQuantidadeInformada(BigDecimal quantidadeInformada) {
		this.quantidadeInformada = quantidadeInformada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoFornecedor == null) ? 0 : codigoFornecedor.hashCode());
		result = prime * result + ((codigoItem == null) ? 0 : codigoItem.hashCode());
		result = prime * result + ((quantidadeInformada == null) ? 0 : quantidadeInformada.hashCode());
		result = prime * result + (selecionado ? 1231 : 1237);
		result = prime * result + ((valorItem == null) ? 0 : valorItem.hashCode());
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
		ItemFornecedorVO other = (ItemFornecedorVO) obj;
		if (codigoFornecedor == null) {
			if (other.codigoFornecedor != null) {
				return false;
			}
		} else if (!codigoFornecedor.equals(other.codigoFornecedor)) {
			return false;
		}
		if (codigoItem == null) {
			if (other.codigoItem != null) {
				return false;
			}
		} else if (!codigoItem.equals(other.codigoItem)) {
			return false;
		}
		if (quantidadeInformada == null) {
			if (other.quantidadeInformada != null) {
				return false;
			}
		} else if (!quantidadeInformada.equals(other.quantidadeInformada)) {
			return false;
		}
		if (selecionado != other.selecionado) {
			return false;
		}
		if (valorItem == null) {
			if (other.valorItem != null) {
				return false;
			}
		} else if (!valorItem.equals(other.valorItem)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ItemFornecedorVO [codigoFornecedor=" + codigoFornecedor + ", codigoItem=" + codigoItem + ", quantidadeInformada="
			+ quantidadeInformada + ", valorItem=" + valorItem + ", selecionado=" + selecionado + "]";
	}

	@Override
	public int compareTo(ItemFornecedorVO o) {
		return 0;
	}

}

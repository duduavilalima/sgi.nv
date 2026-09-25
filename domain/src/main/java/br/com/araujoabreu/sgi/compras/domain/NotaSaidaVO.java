package br.com.araujoabreu.sgi.compras.domain;

import java.io.Serializable;

public class NotaSaidaVO implements Serializable {

	private static final long serialVersionUID = 8982263243889257790L;

	private NotaSaidaParametro tipoDocumento;
	private NotaSaida notaSaida;
	private NotaSaidaParametro formaPagamento;
	private NotaSaidaParametro formaEmissao;
	private NotaSaidaParametro finalidadeEmissao;
	private NotaSaidaParametro danfe;

	private Long codigoNotaOriginal;
	private int listaProdutosOriginal = 0;

	private String chaveAcessoPrimeiro;
	private String chaveAcessoSegundo;
	private String chaveAcessoTerceiro;

	private Long cfopTransporteCodigo;

	public NotaSaidaVO() {
		super();
	}

	public NotaSaidaVO(NotaSaida notaSaida) {
		super();
		this.notaSaida = notaSaida;
	}

	public NotaSaidaParametro getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(NotaSaidaParametro tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public NotaSaida getNotaSaida() {
		return notaSaida;
	}

	public void setNotaSaida(NotaSaida notaSaida) {
		this.notaSaida = notaSaida;
	}

	public NotaSaidaParametro getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(NotaSaidaParametro formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public NotaSaidaParametro getFormaEmissao() {
		return formaEmissao;
	}

	public void setFormaEmissao(NotaSaidaParametro formaEmissao) {
		this.formaEmissao = formaEmissao;
	}

	public NotaSaidaParametro getFinalidadeEmissao() {
		return finalidadeEmissao;
	}

	public void setFinalidadeEmissao(NotaSaidaParametro finalidadeEmissao) {
		this.finalidadeEmissao = finalidadeEmissao;
	}

	public NotaSaidaParametro getDanfe() {
		return danfe;
	}

	public void setDanfe(NotaSaidaParametro danfe) {
		this.danfe = danfe;
	}

	public Long getCodigoNotaOriginal() {
		return codigoNotaOriginal;
	}

	public void setCodigoNotaOriginal(Long codigoNotaOriginal) {
		this.codigoNotaOriginal = codigoNotaOriginal;
	}

	public int getListaProdutosOriginal() {
		return listaProdutosOriginal;
	}

	public void setListaProdutosOriginal(int listaProdutosOriginal) {
		this.listaProdutosOriginal = listaProdutosOriginal;
	}

	public String getChaveAcessoPrimeiro() {
		return chaveAcessoPrimeiro;
	}

	public void setChaveAcessoPrimeiro(String chaveAcessoPrimeiro) {
		this.chaveAcessoPrimeiro = chaveAcessoPrimeiro;
	}

	public String getChaveAcessoSegundo() {
		return chaveAcessoSegundo;
	}

	public void setChaveAcessoSegundo(String chaveAcessoSegundo) {
		this.chaveAcessoSegundo = chaveAcessoSegundo;
	}

	public String getChaveAcessoTerceiro() {
		return chaveAcessoTerceiro;
	}

	public void setChaveAcessoTerceiro(String chaveAcessoTerceiro) {
		this.chaveAcessoTerceiro = chaveAcessoTerceiro;
	}

	public Long getCfopTransporteCodigo() {
		return cfopTransporteCodigo;
	}

	public void setCfopTransporteCodigo(Long cfopTransporteCodigo) {
		this.cfopTransporteCodigo = cfopTransporteCodigo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoNotaOriginal == null) ? 0 : codigoNotaOriginal.hashCode());
		result = prime * result + ((danfe == null) ? 0 : danfe.hashCode());
		result = prime * result + ((finalidadeEmissao == null) ? 0 : finalidadeEmissao.hashCode());
		result = prime * result + ((formaEmissao == null) ? 0 : formaEmissao.hashCode());
		result = prime * result + ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
		result = prime * result + listaProdutosOriginal;
		result = prime * result + ((notaSaida == null) ? 0 : notaSaida.hashCode());
		result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
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
		NotaSaidaVO other = (NotaSaidaVO) obj;
		if (codigoNotaOriginal == null) {
			if (other.codigoNotaOriginal != null) {
				return false;
			}
		} else if (!codigoNotaOriginal.equals(other.codigoNotaOriginal)) {
			return false;
		}
		if (danfe == null) {
			if (other.danfe != null) {
				return false;
			}
		} else if (!danfe.equals(other.danfe)) {
			return false;
		}
		if (finalidadeEmissao == null) {
			if (other.finalidadeEmissao != null) {
				return false;
			}
		} else if (!finalidadeEmissao.equals(other.finalidadeEmissao)) {
			return false;
		}
		if (formaEmissao == null) {
			if (other.formaEmissao != null) {
				return false;
			}
		} else if (!formaEmissao.equals(other.formaEmissao)) {
			return false;
		}
		if (formaPagamento == null) {
			if (other.formaPagamento != null) {
				return false;
			}
		} else if (!formaPagamento.equals(other.formaPagamento)) {
			return false;
		}
		if (listaProdutosOriginal != other.listaProdutosOriginal) {
			return false;
		}
		if (notaSaida == null) {
			if (other.notaSaida != null) {
				return false;
			}
		} else if (!notaSaida.equals(other.notaSaida)) {
			return false;
		}
		if (tipoDocumento == null) {
			if (other.tipoDocumento != null) {
				return false;
			}
		} else if (!tipoDocumento.equals(other.tipoDocumento)) {
			return false;
		}
		return true;
	}

}

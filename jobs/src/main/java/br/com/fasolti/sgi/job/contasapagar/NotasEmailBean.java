package br.com.fasolti.sgi.job.contasapagar;

import java.math.BigDecimal;
import java.util.List;

public class NotasEmailBean {

	private Integer total = 0;
	private Integer totalNfs = 0;
	private String strDataCriacao;
	private String numero;
	private String nomeCredor;
	private String documentoCredor;
	private BigDecimal valor;
	private String cl;
	private String criador;
	private String strValor;

	private List<NotasEmailBean> nps;
	private List<NotasEmailBean> nfs;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalNfs() {
		return totalNfs;
	}

	public void setTotalNfs(Integer totalNfs) {
		this.totalNfs = totalNfs;
	}

	public String getStrDataCriacao() {
		return strDataCriacao;
	}

	public void setStrDataCriacao(String strDataCriacao) {
		this.strDataCriacao = strDataCriacao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNomeCredor() {
		return nomeCredor;
	}

	public void setNomeCredor(String nomeCredor) {
		this.nomeCredor = nomeCredor;
	}

	public String getDocumentoCredor() {
		return documentoCredor;
	}

	public void setDocumentoCredor(String documentoCredor) {
		this.documentoCredor = documentoCredor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getCl() {
		return cl;
	}

	public void setCl(String cl) {
		this.cl = cl;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public String getStrValor() {
		return strValor;
	}

	public void setStrValor(String strValor) {
		this.strValor = strValor;
	}

	public List<NotasEmailBean> getNps() {
		return nps;
	}

	public void setNps(List<NotasEmailBean> nps) {
		this.nps = nps;
	}

	public List<NotasEmailBean> getNfs() {
		return nfs;
	}

	public void setNfs(List<NotasEmailBean> nfs) {
		this.nfs = nfs;
	}

	/*
	Este método é utilizado no comparator, para a ordenação das NFs.
	Os números das NPs são únicos, mas a numeração das NFs pode se repetir.
	 */
	public String getNumeroECredor() {
		return this.getNumero().concat(this.getNomeCredor());
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("NotasEmailBean{");
		sb.append("total=").append(total);
		sb.append(", totalNfs=").append(totalNfs);
		sb.append(", strDataCriacao='").append(strDataCriacao).append('\'');
		sb.append(", numero='").append(numero).append('\'');
		sb.append(", nomeCredor='").append(nomeCredor).append('\'');
		sb.append(", documentoCredor='").append(documentoCredor).append('\'');
		sb.append(", valor=").append(valor);
		sb.append(", cl='").append(cl).append('\'');
		sb.append(", criador='").append(criador).append('\'');
		sb.append(", strValor='").append(strValor).append('\'');
		sb.append(", nps=").append(nps);
		sb.append(", nfs=").append(nfs);
		sb.append('}');
		return sb.toString();
	}
}

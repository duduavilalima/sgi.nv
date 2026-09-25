package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ItemRelacaoPesquisaTO extends BaseSearchTO {
	private boolean selecionado = true;
	private boolean pagamentoSemNumeroBoleto = false;
	private boolean vencimentoPosterior = false;
	private boolean notaFiscalComTributoErrado = false;
	private RelacaoBancariaTO relacaoBancaria;
	private List<PagamentoTO> listaPgtos;
	private String status;
	private Long cheque;
	private BigDecimal total;
	private Integer qtdPagamentos;
	private String caminhoComprovante;
	private BancoTO banco;
	private String agencia;
	private String digitoAgencia;
	private String conta;
	private String digito;
	private Date dataPagamento;
	private boolean pgtoAgrupadoCodigoBarras;
	private Integer ordemParcelaNF;
	private int totalPgtos;
	private BigDecimal totalPago;
	private RetornoBancarioTO retornoBancario;
	private List<ItemRelacaoPagamentoTO> listaPagamentos;
	private Boolean faltandoDadosBancariosEnvio;

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isPagamentoSemNumeroBoleto() {
		return pagamentoSemNumeroBoleto;
	}

	public void setPagamentoSemNumeroBoleto(boolean pagamentoSemNumeroBoleto) {
		this.pagamentoSemNumeroBoleto = pagamentoSemNumeroBoleto;
	}

	public boolean isVencimentoPosterior() {
		return vencimentoPosterior;
	}

	public void setVencimentoPosterior(boolean vencimentoPosterior) {
		this.vencimentoPosterior = vencimentoPosterior;
	}

	public boolean isNotaFiscalComTributoErrado() {
		return notaFiscalComTributoErrado;
	}

	public void setNotaFiscalComTributoErrado(boolean notaFiscalComTributoErrado) {
		this.notaFiscalComTributoErrado = notaFiscalComTributoErrado;
	}

	public RelacaoBancariaTO getRelacaoBancaria() {
		return relacaoBancaria;
	}

	public void setRelacaoBancaria(RelacaoBancariaTO relacaoBancaria) {
		this.relacaoBancaria = relacaoBancaria;
	}

	public List<PagamentoTO> getListaPgtos() {
		return listaPgtos;
	}

	public void setListaPgtos(List<PagamentoTO> listaPgtos) {
		this.listaPgtos = listaPgtos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCheque() {
		return cheque;
	}

	public void setCheque(Long cheque) {
		this.cheque = cheque;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(Integer qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	public String getCaminhoComprovante() {
		return caminhoComprovante;
	}

	public void setCaminhoComprovante(String caminhoComprovante) {
		this.caminhoComprovante = caminhoComprovante;
	}

	public BancoTO getBanco() {
		return banco;
	}

	public void setBanco(BancoTO banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public boolean isPgtoAgrupadoCodigoBarras() {
		return pgtoAgrupadoCodigoBarras;
	}

	public void setPgtoAgrupadoCodigoBarras(boolean pgtoAgrupadoCodigoBarras) {
		this.pgtoAgrupadoCodigoBarras = pgtoAgrupadoCodigoBarras;
	}

	public Integer getOrdemParcelaNF() {
		return ordemParcelaNF;
	}

	public void setOrdemParcelaNF(Integer ordemParcelaNF) {
		this.ordemParcelaNF = ordemParcelaNF;
	}

	public List<ItemRelacaoPagamentoTO> getListaPagamentos() {
		return listaPagamentos;
	}

	public void setListaPagamentos(List<ItemRelacaoPagamentoTO> listaPagamentos) {
		this.listaPagamentos = listaPagamentos;
	}

	public int getTotalPgtos() {
		return totalPgtos;
	}

	public void setTotalPgtos(int totalPgtos) {
		this.totalPgtos = totalPgtos;
	}

	public RetornoBancarioTO getRetornoBancario() {
		return retornoBancario;
	}

	public void setRetornoBancario(RetornoBancarioTO retornoBancario) {
		this.retornoBancario = retornoBancario;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public Boolean getFaltandoDadosBancariosEnvio() {
		return faltandoDadosBancariosEnvio;
	}

	public void setFaltandoDadosBancariosEnvio(Boolean faltandoDadosBancariosEnvio) {
		this.faltandoDadosBancariosEnvio = faltandoDadosBancariosEnvio;
	}

	@Override
	public String toString() {
		return "ItemRelacaoTO{" +
			"codigo=" + getCodigo() +
			", selecionado=" + selecionado +
			", pagamentoSemNumeroBoleto=" + pagamentoSemNumeroBoleto +
			", vencimentoPosterior=" + vencimentoPosterior +
			", notaFiscalComTributoErrado=" + notaFiscalComTributoErrado +
			", relacaoBancaria=" + relacaoBancaria +
			", listaPgtos=" + listaPgtos +
			", status='" + status + '\'' +
			", cheque=" + cheque +
			", total=" + total +
			", qtdPagamentos=" + qtdPagamentos +
			", caminhoComprovante='" + caminhoComprovante + '\'' +
			", banco=" + banco +
			", agencia='" + agencia + '\'' +
			", digitoAgencia='" + digitoAgencia + '\'' +
			", conta='" + conta + '\'' +
			", digito='" + digito + '\'' +
			", dataPagamento=" + dataPagamento +
			", pgtoAgrupadoCodigoBarras=" + pgtoAgrupadoCodigoBarras +
			", ordemParcelaNF=" + ordemParcelaNF +
			", totalPgtos=" + totalPgtos +
			", totalPago=" + totalPago +
			", retornoBancario=" + retornoBancario +
			", listaPagamentos=" + listaPagamentos +
			'}';
	}
}

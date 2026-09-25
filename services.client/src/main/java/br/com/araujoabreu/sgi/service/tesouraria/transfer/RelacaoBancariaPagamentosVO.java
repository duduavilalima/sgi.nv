package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.banco.transfer.BancoTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoContaContabilTO;
import br.com.araujoabreu.sgi.service.dadosBancarios.transfer.DadosBancariosTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelacaoBancariaPagamentosVO implements Serializable, Comparable {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private String numero;
	private PessoaTO credor;
	private List<DadosBancariosTO> listDadosBancarios = new ArrayList<>();
	private Integer quantidadeContaCorrente;
	private BancoTO bancoSelecionado;
	private String nomeBanco;
	private String tributo;
	private String tipoPagamento;
	private String tipoDocumentoPagamento;
	private Date dataPagamento;
	private Date dataVencimento;
	private boolean tAorTR;
	private ItemRelacaoTO itemRelacao;
	private String statusPagamento;
	private boolean pgtoAgrupadoCodigoBarras;
	private String nomeBancoDoCodigoDeBarrasELinhaDigitavel;
	private int qtdadePagamentos;
	private BigDecimal total;
	private BigDecimal valorMulta;
	private BigDecimal valorJuros;
	private BigDecimal valorDesconto;
	private BigDecimal valorDevolucao;
	private BigDecimal valorOutrosDescontos;
	private BigDecimal valorAdiantamento;
	private BigDecimal valorAPagar;
	private BigDecimal valorNota;
	private BigDecimal valorTributos;
	private BigDecimal valorLiquido;
	private BigDecimal valor;
	private String linhaDigitavel;
	private String codigoBarras;
	private String statusConciliacaoBancaria;
	private String cheque;
	private CentroFinanceiroTO centroFinanceiro;
	private ContaContabilTO contaContabil;
	private DadosBancariosTO dadosBancarios;
	private List<PagamentoContaContabilTO> listaPgtoCC = new ArrayList<>();
	private RetornoBancarioTO retornoBancario;
	private Long codigoPagamento; // somente utilizado quando o item de relação da RB possui um único pagamento
	private String infoBoleto;
	private BancoTO bancoBoleto;
	private Boolean faltandoDadosBancariosEnvio;
	private String caminhoComprovante;

	private Boolean selecionado;

	public RelacaoBancariaPagamentosVO() {
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public PessoaTO getCredor() {
		return credor;
	}

	public void setCredor(PessoaTO credor) {
		this.credor = credor;
	}

	public List<DadosBancariosTO> getListDadosBancarios() {
		return listDadosBancarios;
	}

	public void setListDadosBancarios(List<DadosBancariosTO> listDadosBancarios) {
		this.listDadosBancarios = listDadosBancarios;
	}

	public Integer getQuantidadeContaCorrente() {
		return quantidadeContaCorrente;
	}

	public void setQuantidadeContaCorrente(Integer quantidadeContaCorrente) {
		this.quantidadeContaCorrente = quantidadeContaCorrente;
	}

	public BancoTO getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(BancoTO bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public String getTributo() {
		return tributo;
	}

	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(String tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public boolean istAorTR() {
		return tAorTR;
	}

	public void settAorTR(boolean tAorTR) {
		this.tAorTR = tAorTR;
	}

	public ItemRelacaoTO getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(ItemRelacaoTO itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public int getQtdadePagamentos() {
		return qtdadePagamentos;
	}

	public void setQtdadePagamentos(int qtdadePagamentos) {
		this.qtdadePagamentos = qtdadePagamentos;
	}

	public void setListaDadosBancariosRB(ItemRelacaoTO itemRelacao) {
		if (itemRelacao != null) {
			if (existemDadosBancariosRB(itemRelacao)) {
				this.setBancoSelecionado(itemRelacao.getBanco());
				DadosBancariosTO dadosBancarios = new DadosBancariosTO();
				dadosBancarios.setBanco(itemRelacao.getBanco());
				dadosBancarios.setAgencia(itemRelacao.getAgencia());
				dadosBancarios.setDigitoAgencia(itemRelacao.getDigitoAgencia());
				dadosBancarios.setConta(itemRelacao.getConta());
				dadosBancarios.setDigito(itemRelacao.getDigito());
//				if ("Conta poupança".equals(itemRelacao.getTipoConta())) {
//					dadosBancarios.setTipoConta(TipoConta.POUPANCA);
//				}
//				if ("Conta corrente".equals(itemRelacao.getTipoConta())) {
//					dadosBancarios.setTipoConta(TipoConta.CONTA_CORRENTE);
//				}
//				tipoDocumentoPagamento = itemRelacao.getTipoDocumentoPagamento();
				dataPagamento = itemRelacao.getDataPagamento();
				if (listDadosBancarios == null) {
					listDadosBancarios = new ArrayList<>();
				}
				listDadosBancarios.add(dadosBancarios);
			}
		}
	}

	@Transient
	public boolean existemDadosBancariosRB(ItemRelacaoTO itemRelacao) {
		if (itemRelacao.getBanco() != null && itemRelacao.getBanco().getCodigo() != null
			&& itemRelacao.getAgencia() != null && !itemRelacao.getAgencia().isEmpty()
			&& itemRelacao.getConta() != null && !itemRelacao.getConta().isEmpty()
			&& itemRelacao.getDigito() != null && !itemRelacao.getDigito().isEmpty()
			&& tipoDocumentoPagamento != null
			&& dataPagamento != null)
			return true;
		return false;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isPgtoAgrupadoCodigoBarras() {
		return pgtoAgrupadoCodigoBarras;
	}

	public void setPgtoAgrupadoCodigoBarras(boolean pgtoAgrupadoCodigoBarras) {
		this.pgtoAgrupadoCodigoBarras = pgtoAgrupadoCodigoBarras;
	}

	public String getNomeBancoDoCodigoDeBarrasELinhaDigitavel() {
		return nomeBancoDoCodigoDeBarrasELinhaDigitavel;
	}

	public void setNomeBancoDoCodigoDeBarrasELinhaDigitavel(String nomeDoBanco) {
		this.nomeBancoDoCodigoDeBarrasELinhaDigitavel = nomeDoBanco;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getStatusConciliacaoBancaria() {
		return statusConciliacaoBancaria;
	}

	public void setStatusConciliacaoBancaria(String statusConciliacaoBancaria) {
		this.statusConciliacaoBancaria = statusConciliacaoBancaria;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	public BigDecimal getValorOutrosDescontos() {
		return valorOutrosDescontos;
	}

	public void setValorOutrosDescontos(BigDecimal valorOutrosDescontos) {
		this.valorOutrosDescontos = valorOutrosDescontos;
	}

	public BigDecimal getValorAdiantamento() {
		return valorAdiantamento;
	}

	public void setValorAdiantamento(BigDecimal valorAdiantamento) {
		this.valorAdiantamento = valorAdiantamento;
	}

	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(BigDecimal valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

	public BigDecimal getValorNota() {
		return valorNota;
	}

	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	public BigDecimal getValorTributos() {
		return valorTributos;
	}

	public void setValorTributos(BigDecimal valorTributos) {
		this.valorTributos = valorTributos;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public DadosBancariosTO getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancariosTO dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public List<PagamentoContaContabilTO> getListaPgtoCC() {
		return listaPgtoCC;
	}

	public void setListaPgtoCC(List<PagamentoContaContabilTO> listaPgtoCC) {
		this.listaPgtoCC = listaPgtoCC;
	}

	public RetornoBancarioTO getRetornoBancario() {
		return retornoBancario;
	}

	public void setRetornoBancario(RetornoBancarioTO retornoBancario) {
		this.retornoBancario = retornoBancario;
	}

	public Long getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(Long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getInfoBoleto() {
		return infoBoleto;
	}

	public void setInfoBoleto(String infoBoleto) {
		this.infoBoleto = infoBoleto;
	}

	public BancoTO getBancoBoleto() {
		return bancoBoleto;
	}

	public void setBancoBoleto(BancoTO bancoBoleto) {
		this.bancoBoleto = bancoBoleto;
	}

	public Boolean isDadosBancariosInformados() {
		Boolean dadosBancariosInformados = false;
		return dadosBancariosInformados;
	}

	public Boolean getFaltandoDadosBancariosEnvio() {
		return this.faltandoDadosBancariosEnvio;
	}

	public void setFaltandoDadosBancariosEnvio(Boolean faltandoDadosBancariosEnvio) {
		this.faltandoDadosBancariosEnvio = faltandoDadosBancariosEnvio;
	}

	public String getCaminhoComprovante() {
		return caminhoComprovante;
	}

	public void setCaminhoComprovante(String caminhoComprovante) {
		this.caminhoComprovante = caminhoComprovante;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RelacaoBancariaPagamentosVO)) return false;

		RelacaoBancariaPagamentosVO that = (RelacaoBancariaPagamentosVO) o;

		if (getCodigo() != null ? !getCodigo().equals(that.getCodigo()) : that.getCodigo() != null) return false;
		return getCheque() != null ? getCheque().equals(that.getCheque()) : that.getCheque() == null;
	}

	@Override
	public int hashCode() {
		int result = getCodigo() != null ? getCodigo().hashCode() : 0;
		result = 31 * result + (getCheque() != null ? getCheque().hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(Object o) {
		return ((RelacaoBancariaPagamentosVO) o).getValorAPagar().compareTo(getValorAPagar());
	}
}

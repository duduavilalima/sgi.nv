package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.Status;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao.StatusItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao.TipoContaBancaria;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios.TipoConta;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Objeto de exibição dos pagamentos de uma {@link RelacaoBancaria}.
 *
 * @author Daniel Oliveira
 */
public class RelacaoBancariaPagamentosVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Número do tributo, da nota ou pagamento (quando houver apenas um pg).
	 */
	private String numero;
	/**
	 * Nome do fornecedor. Quando houver;
	 */
	private Pessoa credor = new Pessoa();
	/**
	 * Lista de dados bancários do credor.
	 */
	private List<DadosBancarios> listDadosBancarios = new ArrayList<>();
	/**
	 * Banco selecionado.
	 */
	private Banco bancoSelecionado;
	/**
	 * Nome do tributo. Quando for tributo.
	 */
	private String tributo;
	/**
	 * Tipo do pagamento.
	 */
	private TipoPagamento tipoPagamento;
	/**
	 * Tipo do documento do pagamento.
	 */
	private TipoDocumentoPagamento tipoDocumentoPagamento;
	/**
	 * Data do pagamento.
	 */
	private Date dataPagamento;
	/**
	 * Informa se é um TA ou TR.
	 */
	private boolean tAorTR;

	/**
	 * A instância do {@link ItemRelacao}.
	 */
	private ItemRelacao itemRelacao;
	/**
	 * Status do pagamento.
	 */
	private Status statusPagamento;

	private boolean pgtoAgrupadoCodigoBarras;

	/**
	 * Nome do banco associado a um código de barras ou linha digitável.
	 */
	private String nomeBancoDoCodigoDeBarrasELinhaDigitavel;

	public RelacaoBancariaPagamentosVO() {
		super();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(final String numero) {
		this.numero = numero;
	}

	public Pessoa getCredor() {
		return credor;
	}

	public void setCredor(final Pessoa credor) {
		this.credor = credor;
	}

	public List<DadosBancarios> getListDadosBancarios() {
		return listDadosBancarios;
	}

	public void setListDadosBancarios(final List<DadosBancarios> listDadosBancarios) {
		this.listDadosBancarios = listDadosBancarios;
	}

	public Banco getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(final Banco bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public String getTributo() {
		return tributo;
	}

	public void setTributo(final String tributo) {
		this.tributo = tributo;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(final TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public TipoDocumentoPagamento getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(final TipoDocumentoPagamento tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public int getNumeroPagamentos() {
		return itemRelacao.getPagamentos() != null ? itemRelacao.getPagamentos().size() : 0;
	}

	public BigDecimal getValorPagar() {
		// Valor a Pagar
		return itemRelacao.getTotalPagamentos();
	}

	public Long getCheque() {
		return getItemRelacao().getCheque();
	}

	public void setCheque(final Long cheque) {
		this.itemRelacao.setCheque(cheque);
	}

	public boolean istAorTR() {
		return tAorTR;
	}

	public void settAorTR(final boolean tAorTR) {
		this.tAorTR = tAorTR;
	}

	public ItemRelacao getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(final ItemRelacao itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	public StatusItemRelacao getStatus() {
		return getItemRelacao().getStatus();
	}

	public void setStatus(final StatusItemRelacao status) {
		this.itemRelacao.setStatus(status);
	}

	public boolean isSelecionado() {
		return getItemRelacao().isSelecionado();
	}

	public void setSelecionado(final boolean selecionado) {
		this.itemRelacao.setSelecionado(selecionado);
	}

	public Status getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(Status statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RelacaoBancariaPagamentosVO that = (RelacaoBancariaPagamentosVO) o;
		return tAorTR == that.tAorTR &&
			pgtoAgrupadoCodigoBarras == that.pgtoAgrupadoCodigoBarras &&
			numero.equals(that.numero) &&
			tributo.equals(that.tributo) &&
			dataPagamento.equals(that.dataPagamento) &&
			nomeBancoDoCodigoDeBarrasELinhaDigitavel.equals(that.nomeBancoDoCodigoDeBarrasELinhaDigitavel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, tributo, dataPagamento, tAorTR, pgtoAgrupadoCodigoBarras, nomeBancoDoCodigoDeBarrasELinhaDigitavel);
	}

	@Override
	public String toString() {
		return "RelacaoBancariaPagamentosVO [numero=" + numero + ", cheque=" + getItemRelacao().getCheque() + ", tAorTR=" + tAorTR + ", status="
			+ getItemRelacao().getStatus() + ", selecionado=" + getItemRelacao().isSelecionado() + "]";
	}

	public void setListaDadosBancariosRB(ItemRelacao itemRelacao) {
		if (itemRelacao != null) {
			if (itemRelacao.existemDadosBancariosRB()) {
				this.setBancoSelecionado(itemRelacao.getBanco());
				DadosBancarios dadosBancarios = new DadosBancarios();
				dadosBancarios.setBanco(itemRelacao.getBanco());
				dadosBancarios.setAgencia(itemRelacao.getAgencia());
				dadosBancarios.setDigitoAgencia(itemRelacao.getDigitoAgencia());
				dadosBancarios.setConta(itemRelacao.getConta());
				dadosBancarios.setDigito(itemRelacao.getDigito());
				if (TipoContaBancaria.CONTA_POUPANCA.equals(itemRelacao.getTipoConta()))
					dadosBancarios.setTipoConta(TipoConta.POUPANCA);
				if (TipoContaBancaria.CONTA_CORRENTE.equals(itemRelacao.getTipoConta()))
					dadosBancarios.setTipoConta(TipoConta.CONTA_CORRENTE);
				tipoDocumentoPagamento = itemRelacao.getTipoDocumentoPagamento();
				dataPagamento = itemRelacao.getDataPagamento();
				if (listDadosBancarios == null)
					listDadosBancarios = new ArrayList<>();
				listDadosBancarios.add(dadosBancarios);
			}
		}
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

}

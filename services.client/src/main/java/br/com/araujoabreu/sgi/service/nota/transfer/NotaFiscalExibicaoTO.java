package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class NotaFiscalExibicaoTO extends BaseTO {
	//FIXME Não tem ID?
	private String numero;
	private Integer numParcelas;
	private String situacao;
	private BigDecimal valorPagar;
	private BigDecimal tributos;
	private BigDecimal valorRetencoes;
	private BigDecimal valorPermuta;
	private BigDecimal descontos;
	private BigDecimal multaJuros;
	private BigDecimal valorPago;
	private BigDecimal valorBruto;
	private BigDecimal multa;
	private BigDecimal juros;
	private String etapa;
	private String descricaoItem;
	private Long codigoEtapa;
	private Long codigoNota;
	private Date dataEmissao;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getNumParcelas() {
		return numParcelas;
	}

	public void setNumParcelas(Integer numParcelas) {
		this.numParcelas = numParcelas;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public BigDecimal getTributos() {
		return tributos;
	}

	public void setTributos(BigDecimal tributos) {
		this.tributos = tributos;
	}

	public BigDecimal getDescontos() {
		return descontos;
	}

	public void setDescontos(BigDecimal descontos) {
		this.descontos = descontos;
	}

	public BigDecimal getMultaJuros() {
		return multaJuros;
	}

	public void setMultaJuros(BigDecimal multaJuros) {
		this.multaJuros = multaJuros;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public Long getCodigoEtapa() {
		return codigoEtapa;
	}

	public void setCodigoEtapa(Long codigoEtapa) {
		this.codigoEtapa = codigoEtapa;
	}

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}

	public BigDecimal getValorRetencoes() {
		return valorRetencoes;
	}

	public void setValorRetencoes(BigDecimal valorRetencoes) {
		this.valorRetencoes = valorRetencoes;
	}

	public BigDecimal getValorPermuta() {
		return valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getMulta() {
		return multa;
	}

	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}

	public BigDecimal getJuros() {
		return juros;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaFiscalExibicaoTO)) return false;
		if (!super.equals(o)) return false;
		NotaFiscalExibicaoTO that = (NotaFiscalExibicaoTO) o;
		return Objects.equals(getNumero(), that.getNumero()) &&
			Objects.equals(getNumParcelas(), that.getNumParcelas()) &&
			Objects.equals(getSituacao(), that.getSituacao()) &&
			Objects.equals(getValorPagar(), that.getValorPagar()) &&
			Objects.equals(getTributos(), that.getTributos()) &&
			Objects.equals(getValorRetencoes(), that.getValorRetencoes()) &&
			Objects.equals(getValorPermuta(), that.getValorPermuta()) &&
			Objects.equals(getDescontos(), that.getDescontos()) &&
			Objects.equals(getMultaJuros(), that.getMultaJuros()) &&
			Objects.equals(getValorPago(), that.getValorPago()) &&
			Objects.equals(getValorBruto(), that.getValorBruto()) &&
			Objects.equals(getMulta(), that.getMulta()) &&
			Objects.equals(getJuros(), that.getJuros()) &&
			Objects.equals(getEtapa(), that.getEtapa()) &&
			Objects.equals(getDescricaoItem(), that.getDescricaoItem()) &&
			Objects.equals(getCodigoEtapa(), that.getCodigoEtapa()) &&
			Objects.equals(getCodigoNota(), that.getCodigoNota()) &&
			Objects.equals(getDataEmissao(), that.getDataEmissao());
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), getNumero(), getNumParcelas(), getSituacao(), getValorPagar(), getTributos(), getValorRetencoes(), getValorPermuta(), getDescontos(), getMultaJuros(), getValorPago(), getValorBruto(), getMulta(), getJuros(), getEtapa(), getDescricaoItem(), getCodigoEtapa(), getCodigoNota(), getDataEmissao());
	}

	@Override
	public String toString() {
		return "NotaFiscalExibicaoTO{" +
			"numero='" + numero + '\'' +
			", numParcelas=" + numParcelas +
			", situacao='" + situacao + '\'' +
			", valorPagar=" + valorPagar +
			", tributos=" + tributos +
			", valorRetencoes=" + valorRetencoes +
			", valorPermuta=" + valorPermuta +
			", descontos=" + descontos +
			", multaJuros=" + multaJuros +
			", valorPago=" + valorPago +
			", valorBruto=" + valorBruto +
			", multa=" + multa +
			", juros=" + juros +
			", etapa='" + etapa + '\'' +
			", descricaoItem='" + descricaoItem + '\'' +
			", codigoEtapa=" + codigoEtapa +
			", codigoNota=" + codigoNota +
			'}';
	}
}

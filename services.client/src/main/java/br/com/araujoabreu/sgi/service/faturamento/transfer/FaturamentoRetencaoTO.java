package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.SituacaoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class FaturamentoRetencaoTO extends BaseTO implements Comparable {
	private Long codigoResponsavelBaixa;
	private Long codigoResponsavelCancelamento;
	private Date dataBaixa;
	private Date dataCancelamento;
	private BigDecimal percentual;
	private FaturamentoTO faturamento;
	private SituacaoTO situacao;
	private TipoTO tipo;

	public Long getCodigoResponsavelBaixa() {
		return codigoResponsavelBaixa;
	}

	public void setCodigoResponsavelBaixa(Long codigoResponsavelBaixa) {
		this.codigoResponsavelBaixa = codigoResponsavelBaixa;
	}

	public Long getCodigoResponsavelCancelamento() {
		return codigoResponsavelCancelamento;
	}

	public void setCodigoResponsavelCancelamento(Long codigoResponsavelCancelamento) {
		this.codigoResponsavelCancelamento = codigoResponsavelCancelamento;
	}

	public Date getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Timestamp dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
	}

	public SituacaoTO getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoTO situacao) {
		this.situacao = situacao;
	}

	public TipoTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoTO tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Object o) {
		return getCodigo().compareTo(((FaturamentoRetencaoTO) o).getCodigo());
	}
}

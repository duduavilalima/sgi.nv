package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.araujoabreu.sgi.administracao.domain.Situacao;
import br.com.araujoabreu.sgi.administracao.domain.Tipo;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Entity
@Table(name = "FATURAMENTO_RETENCAO")
@SequenceGenerator(name="SEQ_FATURAMENTO_RETENCAO", sequenceName="SEQ_FATURAMENTO_RETENCAO", allocationSize=1)
public class FaturamentoRetencao extends BaseEntity implements Serializable, Comparable {

	private static final long serialVersionUID = -5610668976433274089L;
	private Long codigoResponsavelBaixa;
	private Long codigoResponsavelCancelamento;
	private Timestamp dataBaixa;
	private Timestamp dataCancelamento;
	private BigDecimal percentual;
	private Faturamento faturamento;
	private Situacao situacao;
	private Tipo tipo;

	public FaturamentoRetencao() {
	}


	@Column(name = "codigo_responsavel_baixa")
	public Long getCodigoResponsavelBaixa() {
		return this.codigoResponsavelBaixa;
	}

	public void setCodigoResponsavelBaixa(Long codigoResponsavelBaixa) {
		this.codigoResponsavelBaixa = codigoResponsavelBaixa;
	}


	@Column(name = "codigo_responsavel_cancelamento")
	public Long getCodigoResponsavelCancelamento() {
		return this.codigoResponsavelCancelamento;
	}

	public void setCodigoResponsavelCancelamento(Long codigoResponsavelCancelamento) {
		this.codigoResponsavelCancelamento = codigoResponsavelCancelamento;
	}


	@Column(name = "data_baixa")
	public Timestamp getDataBaixa() {
		return this.dataBaixa;
	}

	public void setDataBaixa(Timestamp dataBaixa) {
		this.dataBaixa = dataBaixa;
	}


	@Column(name = "data_cancelamento")
	public Timestamp getDataCancelamento() {
		return this.dataCancelamento;
	}

	public void setDataCancelamento(Timestamp dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}


	public BigDecimal getPercentual() {
		return this.percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}


	//bi-directional many-to-one association to Faturamento
	@ManyToOne
	@JoinColumn(name = "codigo_faturamento")
	public Faturamento getFaturamento() {
		return this.faturamento;
	}

	public void setFaturamento(Faturamento faturamento) {
		this.faturamento = faturamento;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_situacao")
	public Situacao getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_tipo_retencao")
	public Tipo getTipo() {
		return this.tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Object o) {
		return getCodigo().compareTo(((FaturamentoRetencao) o).getCodigo());
	}
}

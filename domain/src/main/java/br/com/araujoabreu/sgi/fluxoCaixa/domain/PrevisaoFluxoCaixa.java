package br.com.araujoabreu.sgi.fluxoCaixa.domain;

import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PREVISAO_FLUXO_CAIXA")
@SequenceGenerator(name="SEQ_PREVISAO_FLUXO_CAIXA", sequenceName="SEQ_PREVISAO_FLUXO_CAIXA", allocationSize=1)
public class PrevisaoFluxoCaixa extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 8373157460945714252L;
	private Unidade unidade;
	private Diretoria diretoria;
	private DespesaFluxoCaixa despesaFluxoCaixa;
	private Date data;
	private BigDecimal valor;

	public PrevisaoFluxoCaixa() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE", nullable = false)
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DIRETORIA", nullable = false)
	@NotNull
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DESPESA_FLUXO_CAIXA", nullable = false)
	@NotNull
	public DespesaFluxoCaixa getDespesaFluxoCaixa() {
		return despesaFluxoCaixa;
	}

	public void setDespesaFluxoCaixa(DespesaFluxoCaixa despesaFluxoCaixa) {
		this.despesaFluxoCaixa = despesaFluxoCaixa;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false, length = 13)
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column(name = "VALOR", nullable = false, precision = 19, scale = 2)
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}

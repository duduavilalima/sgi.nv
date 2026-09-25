package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.domain.Contrato.PeriodicidadePagamento;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CONTRATO_PARCELA")
@SequenceGenerator(name = "SEQ_CONTRATO_PARCELA", sequenceName = "SEQ_CONTRATO_PARCELA", allocationSize = 1)
public class ContratoParcela extends AuditEntity implements Comparable<ContratoParcela> {

	private static final long serialVersionUID = -2748717535511369078L;
	private Contrato contrato;
	private Integer numero;
	private Date dataVencimento;
	private String referencia;
	private List<ContratoParcelaPagamento> listaParcelaPagamento = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO")
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Column(name = "NUMERO", nullable = false)
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Column(name = "VENCIMENTO", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contratoParcela")
	public List<ContratoParcelaPagamento> getListaParcelaPagamento() {
		return listaParcelaPagamento;
	}

	public void setListaParcelaPagamento(List<ContratoParcelaPagamento> listaParcelaPagamento) {
		this.listaParcelaPagamento = listaParcelaPagamento;
	}

	@Transient
	public String getNumeroVigencia() {

		StringBuilder label = new StringBuilder();

		/*Este if atende ao item 3.3.2.1 do issue SGI-1766*/
		if (PeriodicidadePagamento.CONTRA_APRESENTACAO.equals(this.contrato.getPeriodicidadePagamento())) {
			label.append(acrescentaZerosAEsquerda(2, this.numero)).append(this.numero);
		} else {
			String numeroParcela = getNumeroParcela();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");

			label.append(numeroParcela);
			label.append(" - ");
			label.append(dateFormat.format(this.dataVencimento));
			if (getDataVencimento().before(new Date())) {
				label.append(" (vencida)");
			}
		}

		return label.toString();
	}

	@Transient
	public String getNumeroParcela() {
		int totalParcela = 0;
		return formatarApresentacaoNumeroParcela(this.getNumero(), totalParcela);
	}


	@Transient
	private String formatarApresentacaoNumeroParcela(int numeroParcela, int totalParcela) {

		int tamanhoCaracter = totalParcela < 100 ? 2 : 3;

		String num = "";
		if (this.numero.toString().length() < tamanhoCaracter) {
			num = acrescentaZerosAEsquerda(tamanhoCaracter, this.numero);
		}

		String totalString = "";
		if (String.valueOf(totalParcela).length() < tamanhoCaracter) {
			totalString = acrescentaZerosAEsquerda(tamanhoCaracter, totalParcela);
		}

		return num + numero.toString() + "/" + totalString + totalParcela;
	}

	@Transient
	private String acrescentaZerosAEsquerda(Integer tamanhoCaracter, Integer numero) {
		String num = "";
		if (numero.toString().length() < tamanhoCaracter) {
			for (int x = 0; x < (tamanhoCaracter - numero.toString().length()); x++) {
				num += 0;
			}
		}
		return num;
	}

	@Override
	public int compareTo(ContratoParcela parcela) {
		return this.numero.compareTo(parcela.getNumero());
	}

	@Transient
	public Pagamento getPagamentoAssociado() {
		List<ContratoParcelaPagamento> listaCPP = this.getListaParcelaPagamento();

		if (listaCPP == null || listaCPP.isEmpty()) {
			return null;
		} else {
			return listaCPP.get(0).getPagamento();
		}
	}

	@Transient
	public BigDecimal getValor() {

		if (getPagamentoAssociado() != null)
			return getPagamentoAssociado().getValor();
		else
			return getContrato().getValorMensal() != null ? getContrato().getValorMensal() : BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getMultaJuros() {

		if (getPagamentoAssociado() != null)
			return getPagamentoAssociado().getValorJurosMulta();
		else
			return BigDecimal.ZERO;
	}

	@Transient
	public List<NotaTributoAliquota> getListaTributos() {
		if (getPagamentoAssociado() instanceof Pagamento) {
			Pagamento pgto = getPagamentoAssociado();
			//Por algum motivo não traz a instancia de ParcelaNota, foi utilizado este artificio. By: Bruno Barata
			if (pgto instanceof HibernateProxy) {
				pgto = (Pagamento) ((HibernateProxy) pgto).getHibernateLazyInitializer().getImplementation();
				if (pgto instanceof ParcelaNota) {
					ParcelaNota parcelaNota = (ParcelaNota) pgto;
					Nota nota = parcelaNota.getNota();
					return nota.getListaNotaTributoAliquota();
				}
			}
		}
		return new ArrayList<>();
	}

	@Transient
	public String getReferencia() {
		if (this.listaParcelaPagamento != null && !this.listaParcelaPagamento.isEmpty()) {
			if (this.listaParcelaPagamento.get(0).getReferencia() != null && !this.listaParcelaPagamento.get(0).getReferencia().trim().equals("") && this.referencia == null)
				this.referencia = this.listaParcelaPagamento.get(0).getReferencia();
		}
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}

package br.com.araujoabreu.sgi.fundoFixo.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDemonstrativo;
import br.com.araujoabreu.sgi.compras.domain.NotaPagamento;
import br.com.araujoabreu.sgi.fundoFixo.enums.TipoConta;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Representa uma composição de gastos realizados pelo colaborador em um determinado
 * fundo fixo
 *
 * @author Marcus Lacerda
 * @see FundoFixo
 */
@Entity
@Table(name = "DEMONSTRATIVO")
public class Demonstrativo extends Transacao {

	private static final long serialVersionUID = 4079863586954337017L;
	private String numero;
	private String descricao;
	private Status status;
	private List<ItemDemonstrativo> itens;
	private List<MotivoRetornoDemonstrativo> motivosRetorno;
	private Colaborador aprovador;
	private Colaborador homologador;
	private Colaborador emissor;
	private NotaPagamento notaPagamento;
	private Date dataAprovador;
	private Date dataHomologador;
	private Date dataEmissor;
	private TipoConta tipo;
	private Colaborador canceladorHomologacao;
	private Date dataCancelamentoHomologacao;

	@Override
	public void setCodigo(Long codigo) {
		super.setCodigo(codigo);
		this.numero = "RD/" + codigo;
	}

	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "STATUS", nullable = false)
	@NotNull
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "demonstrativo")
	@OrderBy(value = "codigo")
	public List<ItemDemonstrativo> getItens() {
		return itens;
	}

	public void setItens(List<ItemDemonstrativo> itens) {
		this.itens = itens;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "demonstrativo")
	@OrderBy(value = "codigo")
	public List<MotivoRetornoDemonstrativo> getMotivosRetorno() {
		return motivosRetorno;
	}

	public void setMotivosRetorno(List<MotivoRetornoDemonstrativo> motivosRetorno) {
		this.motivosRetorno = motivosRetorno;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public void addItem(ItemDemonstrativo item) {
		item.setDemonstrativo(this);
		itens.add(item);
	}

	@Override
	@Transient
	public BigDecimal getValor() {
		BigDecimal valor = new BigDecimal(0);
		if (getItens() != null && !getItens().isEmpty()) {
			for (ItemDemonstrativo item : getItens()) {
				valor = valor.add(item.getValor());
			}
		}
		return valor;
	}

	@Override
	public void setValor(BigDecimal valor) {
	}

	@Transient
	public BigDecimal getValorHomologado() {
		BigDecimal valor = new BigDecimal(0);
		if (getItens() != null && !getItens().isEmpty()) {
			for (ItemDemonstrativo item : getItens()) {
				valor = valor.add(item.getValorHomologado());
			}
		}
		return valor;
	}

	@Transient
	public boolean isAberta() {
		return Status.ABERTA.equals(this.status);
	}

	@Transient
	public boolean isReAberta() {
		return Status.REABERTA.equals(this.status);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_HOMOLOGADOR")
	public Colaborador getHomologador() {
		return homologador;
	}

	public void setHomologador(Colaborador homologador) {
		this.homologador = homologador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APROVADOR")
	public Colaborador getAprovador() {
		return aprovador;
	}

	public void setAprovador(Colaborador aprovador) {
		this.aprovador = aprovador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_EMISSOR")
	public Colaborador getEmissor() {
		return emissor;
	}

	public void setEmissor(Colaborador emissor) {
		this.emissor = emissor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_PAGAMENTO")
	public NotaPagamento getNotaPagamento() {
		return notaPagamento;
	}

	public void setNotaPagamento(NotaPagamento notaPagamento) {
		this.notaPagamento = notaPagamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_HOMOLOGADOR")
	public Date getDataHomologador() {
		return dataHomologador;
	}

	public void setDataHomologador(Date dataHomologador) {
		this.dataHomologador = dataHomologador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_APROVADOR")
	public Date getDataAprovador() {
		return dataAprovador;
	}

	public void setDataAprovador(Date dataAprovador) {
		this.dataAprovador = dataAprovador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EMISSOR")
	public Date getDataEmissor() {
		return dataEmissor;
	}

	public void setDataEmissor(Date dataEmissor) {
		this.dataEmissor = dataEmissor;
	}

	@Column(name = "TIPO", nullable = false)
	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANC_HOMOLOGACAO")
	public Colaborador getCanceladorHomologacao() {
		return canceladorHomologacao;
	}

	public void setCanceladorHomologacao(Colaborador canceladorHomologacao) {
		this.canceladorHomologacao = canceladorHomologacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CANC_HOMOLOGACAO")
	public Date getDataCancelamentoHomologacao() {
		return dataCancelamentoHomologacao;
	}

	public void setDataCancelamentoHomologacao(Date dataCancelamentoHomologacao) {
		this.dataCancelamentoHomologacao = dataCancelamentoHomologacao;
	}

	@Override
	public String toString() {
		return "Demonstrativo [numero=" + numero + ", status=" + status + ", dataAprovador=" + dataAprovador
			+ ", dataHomologador=" + dataHomologador + ", tipo=" + tipo + ", notaPagamento=" + notaPagamento + "]";
	}

	public enum Status {
		ABERTA("Aberto", "Demonstrativo em aberto"),
		EM_APROVACAO("Em aprovação (novo RD)", "Demonstrativo enviado. Aguardando aprovação"),

		EM_APROVACAO_RETORNO_HOMOLOGACAO("Em aprovação (retorno da homologação)", "Demonstrativo enviado. Aguardando aprovação"),
		EM_APROVACAO_APOS_AJUSTES("Em aprovação (após ajustes)", "Demonstrativo enviado. Aguardando aprovação"),

		EM_HOMOLOGACAO("Em homologação", "Demonstrativo aprovado. Aguardando homologação"),
		REABERTA("Reaberto", "Demonstrativo reaberto"),
		HOMOLOGADO_PARCIAL("Homologado parcial", "Demonstrativo homologado parcialmente"),
		HOMOLOGADO_TOTAL("Homologado total", "Demonstrativo homologado na sua totalidade"),
		EM_AJUSTE_FISCAL("Em Ajuste Fiscal", "Enviado para homologação com NF/Serviço ou Conjugada");

		private final String nome;
		private final String mensagem;

		private Status(String nome, String mensagem) {
			this.nome = nome;
			this.mensagem = mensagem;
		}

		public String getMensagem() {
			return mensagem;
		}

		public String getNome() {
			return nome;
		}

	}

}

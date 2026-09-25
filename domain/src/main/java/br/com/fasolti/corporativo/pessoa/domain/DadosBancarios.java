package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DADOS_BANCARIOS")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_DADOS_BANCARIOS", sequenceName = "SEQ_DADOS_BANCARIOS", allocationSize = 1)
public class DadosBancarios extends AuditEntity implements DomainObject, Detail, Comparable<DadosBancarios> {

	private static final long serialVersionUID = -4928963478506586423L;

	private Pessoa pessoa;
	private Banco banco;
	private String agencia;
	private String digitoAgencia;
	private String conta;
	private String digito;
	private Boolean principal = false;
	private Boolean homologado = false;
	private Boolean selecao;
	private Date dataHomologacao;
	private TipoHomologacaoDadosBancarios tipoHomologacao;
	private Colaborador homologador;
	private TipoConta tipoConta;
	private Situacao situacao;
	private Pessoa responsavelInativacao;
	private Date dataInativacao;
	private Pessoa responsavelAtivacao;
	private Date dataAtivacao;

	public DadosBancarios() {
		setSituacao(Situacao.ATIVO);
	}

	public DadosBancarios(Banco banco) {
		this.banco = banco;
		setSituacao(Situacao.ATIVO);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PESSOA", nullable = false)
	@NotNull
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BANCO", nullable = false)
	@NotNull
	public Banco getBanco() {
		return this.banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "AGENCIA", nullable = false, length = 5)
	@NotNull
	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	/**
	 * Obter a agencia com o DV, se houver. Se não, apenas a agência.
	 *
	 * @return A agencia com o DV, se houver. Se não, apenas a agência.
	 */
	@Transient
	public String getAgenciaCompleta() {
		if (digitoAgencia != null)
			digitoAgencia = digitoAgencia.replaceAll(" ", "");

		return agencia + ((digitoAgencia != null && !digitoAgencia.isEmpty()) ? "-" + digitoAgencia : "");
	}

	@Column(name = "DIGITO_AGENCIA", length = 1)
	public String getDigitoAgencia() {
		if (this.digitoAgencia != null) {
			return this.digitoAgencia.toUpperCase();
		}
		return this.digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		if (digitoAgencia != null) {
			this.digitoAgencia = digitoAgencia.toUpperCase();
		}
		this.digitoAgencia = digitoAgencia;
	}

	@Column(name = "CONTA", nullable = false, length = 12)
	@NotNull
	public String getConta() {
		return this.conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	/**
	 * Obter a conta com o DV, se houver. Se não, apenas a conta.
	 *
	 * @return A conta com o DV, se houver. Se não, apenas a conta.
	 */
	@Transient
	public String getContaCompleta() {
		if (digito != null)
			digito = digito.replaceAll(" ", "");

		return conta + ((digito != null && !digito.isEmpty()) ? "-" + digito : "");
	}

	/**
	 * Obter a agência e conta completa.
	 *
	 * @return A agência e conta completa.
	 */
	@Transient
	public String getAgenciaConta() {
		return getAgenciaCompleta() + " / " + getContaCompleta();
	}

	@Column(name = "DIGITO", length = 1)
	public String getDigito() {
		if (this.digito != null) {
			return this.digito.toUpperCase();
		}
		return this.digito;
	}

	public void setDigito(String digito) {
		if (digito != null) {
			this.digito = digito.toUpperCase();
		}
		this.digito = digito;
	}

	@Column(name = "HOMOLOGADO", nullable = false)
	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
	}

	@Column(name = "TIPO_HOMOLOGACAO")
	public TipoHomologacaoDadosBancarios getTipoHomologacao() {
		return tipoHomologacao;
	}

	public void setTipoHomologacao(TipoHomologacaoDadosBancarios tipoHomologacao) {
		this.tipoHomologacao = tipoHomologacao;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Colaborador.class)
	@JoinColumn(name = "CODIGO_HOMOLOGADOR")
	public Colaborador getHomologador() {
		return homologador;
	}

	public void setHomologador(Colaborador homologador) {
		this.homologador = homologador;
	}

	@Override
	@Column(name = "PRINCIPAL", nullable = false)
	@NotNull
	public Boolean getPrincipal() {
		return principal;
	}

	@Override
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@Transient
	public String getDadosBancariosFormatado() {
		return banco.getNome() + " / ag." + agencia + " / cc " + conta + "-" + digito;
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_HOMOLOGACAO", length = 7)
	public Date getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(Date dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	@Column(name = "TIPO_CONTA")
	@Enumerated(EnumType.ORDINAL)
	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	@Column(name = "SITUACAO")
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_INATIVACAO")
	public Pessoa getResponsavelInativacao() {
		return responsavelInativacao;
	}

	public void setResponsavelInativacao(Pessoa responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESP_INATIVACAO")
	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_ATIVACAO")
	public Pessoa getResponsavelAtivacao() {
		return responsavelAtivacao;
	}

	public void setResponsavelAtivacao(Pessoa responsavelAtivacao) {
		this.responsavelAtivacao = responsavelAtivacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESP_ATIVACAO")
	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	@Transient
	public boolean isPoupanca() {
		return this.getTipoConta() == null ? false : TipoConta.POUPANCA.equals(this.getTipoConta());
	}

	@Transient
	public boolean isSalario() {
		return this.getTipoConta() == null ? false : TipoConta.SALARIO.equals(this.getTipoConta());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agencia == null) ? 0 : agencia.hashCode());
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((digito == null) ? 0 : digito.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(DadosBancarios.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		DadosBancarios other = (DadosBancarios) obj;
		if (agencia == null) {
			if (other.getAgencia() != null) {
				return false;
			}
		} else if (!agencia.equals(other.getAgencia())) {
			return false;
		}
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (conta == null) {
			if (other.getConta() != null) {
				return false;
			}
		} else if (!conta.equals(other.getConta())) {
			return false;
		}
		if (digito == null) {
			if (other.getDigito() != null) {
				return false;
			}
		} else if (!digito.equals(other.getDigito())) {
			return false;
		}
		if (principal == null) {
			if (other.getPrincipal() != null) {
				return false;
			}
		} else if (!principal.equals(other.getPrincipal())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DadosBancarios [codigo=" + getCodigo() + ", agencia=" + agencia + ", digitoAgencia=" + digitoAgencia
			+ ", conta=" + conta + ", digito=" + digito + ", principal=" + principal + "]";
	}

	@Override
	public int compareTo(DadosBancarios o) {
		return o.getPrincipal().compareTo(this.getPrincipal());
	}

	@Transient
	public void formatarAgencia() {
		if (agencia != null && !agencia.isEmpty()) {
			String novaAgencia = agencia.replace("_", "").trim();
			if (novaAgencia != null && !novaAgencia.isEmpty()) {
				Long agenciaAux = Long.parseLong(novaAgencia);
				novaAgencia = String.format("%05d", agenciaAux);
			}
			agencia = novaAgencia;
		}
	}

	@Transient
	public void formatarConta() {
		if (conta != null && !conta.isEmpty()) {
			String novaConta = conta.replace("_", "").trim();
			if (novaConta != null && !novaConta.isEmpty()) {
				Long contaAux = Long.parseLong(novaConta);
				novaConta = String.format("%012d", contaAux);
			}
			conta = novaConta;
		}
	}

	public static enum TipoHomologacaoDadosBancarios {
		INTERFACE_BANCARIA("Interface bancária", false),
		HOMOLOG_BAIXA_MANUAL("Homologação pela baixa manual", true),
		HOMOLOG_MANUAL("Homologação manual", true);

		private final String nome;
		private final boolean manual;

		TipoHomologacaoDadosBancarios(String nome, boolean manual) {
			this.nome = nome;
			this.manual = manual;
		}

		public String getNome() {
			return nome;
		}

		public boolean isManual() {
			return manual;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Interface bancária";
				case 1:
					return "Homologação pela baixa manual";
				case 2:
					return "Homologação manual";
				default:
					return "";
			}
		}
	}

	public static enum TipoConta {
		CONTA_CORRENTE("Conta corrente", "Corrente"),
		POUPANCA("Conta poupança", "Poupança"),
		SALARIO("Conta salário", "Salário");

		private final String nome;
		private final String abreviacao;

		TipoConta(String nome, String abreviacao) {
			this.nome = nome;
			this.abreviacao = abreviacao;
		}

		public String getNome() {
			return nome;
		}

		public String getAbreviacao() {
			return abreviacao;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Conta corrente";
				case 1:
					return "Conta poupança";
				case 2:
					return "Conta salário";
				default:
					return "";
			}
		}
	}

}

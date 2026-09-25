package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.domain.Avaliacao;
import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.comercial.domain.ClassificacaoCliente;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil.TipoRelacionamento;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PESSOA")
@SequenceGenerator(name = "SEQ_PESSOA", sequenceName = "SEQ_PESSOA", allocationSize = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa extends AuditEntity implements Serializable, DomainObject {

	private static final long serialVersionUID = -2557528898668277232L;

	private Pessoa homologador;
	private Boolean homologado = false;
	private Date dataHomologacao;
	private Pessoa inativador;
	private String nome;
	private TipoPessoa tipoPessoa;
	private String documento;
	private String email;
	private List<FornecedorUsuario> usuarios = new ArrayList<>();
	private ClassificacaoCliente classificacaoCliente;
	private List<Contrato> listaContratos;
	private List<PessoaContaContabil> listaContaContabil;
	private Situacao situacao;
	private Notificacao notificacao;
	private Boolean inativacaoManual = false;
	private Date dataInativacao;
	private Endereco endereco;
	private Set<DadosBancarios> listaDadosBancarios = new HashSet<>(0);
	private Set<Telefone> telefones = new HashSet<>(0);
	private Boolean selecionado;

	/**
	 * {@link Avaliacao} usada para apresentação na tela de negociações.
	 */
	private Avaliacao avaliacaoParaApresentacao = new Avaliacao();
	private Pessoa clientePrincipal;
	private List<Pessoa> subClientes;
	private Boolean faturamentoCobranca;

	public Pessoa() {
	}

	public Pessoa(Endereco endereco) {
		this.endereco = endereco;
	}

	public Pessoa(final Long codigo) {
		this.setCodigo(codigo);
	}

	public Pessoa(Long codigo, String nome, TipoPessoa tipoPessoa, String documento) {
		this.setCodigo(codigo);
		this.nome = nome;
		this.tipoPessoa = tipoPessoa;
		this.documento = documento;
	}

	public Pessoa(Long codigo, String nome) {
		this.setCodigo(codigo);
		this.nome = nome;
	}

	public void addDadosBancarios(DadosBancarios dadosBancarios) {
		listaDadosBancarios.add(dadosBancarios);
		dadosBancarios.setPessoa(this);
	}

	public void addTelefone(Telefone telefone) {
		telefones.add(telefone);
		telefone.setPessoa(this);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ENDERECO")
	public Endereco getEndereco() {
		return this.endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Column(name = "NOME", nullable = false)
	@NotNull
	@Length(max = 255)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoPessoa getTipoPessoa() {
		return this.tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	@Column(name = "EMAIL", length = 100)
	@Length(max = 100)
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fornecedor")
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<FornecedorUsuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<FornecedorUsuario> usuarios) {
		this.usuarios = usuarios;
	}

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "pessoa")
	@LazyCollection(LazyCollectionOption.FALSE)
	public Set<DadosBancarios> getListaDadosBancarios() {
		return this.listaDadosBancarios;
	}

	public void setListaDadosBancarios(Set<DadosBancarios> listaDadosBancarios) {
		this.listaDadosBancarios = listaDadosBancarios;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pessoa")
	public Set<Telefone> getTelefones() {
		return this.telefones;
	}

	public void setTelefones(Set<Telefone> telefones) {
		this.telefones = telefones;
	}

	@OneToOne
	@JoinColumn(name = "CODIGO_INATIVADOR")
	public Pessoa getInativador() {
		return inativador;
	}

	public void setInativador(Pessoa inativador) {
		this.inativador = inativador;
	}

	@OneToOne
	@JoinColumn(name = "CODIGO_HOMOLOGADOR")
	public Pessoa getHomologador() {
		return homologador;
	}

	public void setHomologador(Pessoa homologador) {
		this.homologador = homologador;
	}

	@Column(name = "DOCUMENTO")
	@NotNull
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Column(name = "INATIVACAO_MANUAL")
	public Boolean getInativacaoManual() {
		return inativacaoManual;
	}

	public void setInativacaoManual(Boolean inativacaoManual) {
		this.inativacaoManual = inativacaoManual;
	}

	@Column(name = "HOMOLOGADO")
	public Boolean getHomologado() {
		return homologado;
	}

	public void setHomologado(Boolean homologado) {
		this.homologado = homologado;
	}

	@Column(name = "DATA_INATIVACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@Column(name = "DATA_HOMOLOGACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(Date dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CLASSIFICACAO_CLIENTE")
	public ClassificacaoCliente getClassificacaoCliente() {
		return classificacaoCliente;
	}

	public void setClassificacaoCliente(ClassificacaoCliente classificacaoCliente) {
		this.classificacaoCliente = classificacaoCliente;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cliente")
	public List<Contrato> getListaContratos() {
		return listaContratos;
	}

	public void setListaContratos(List<Contrato> listaContratos) {
		this.listaContratos = listaContratos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pessoa")
	public List<PessoaContaContabil> getListaContaContabil() {
		return listaContaContabil;
	}

	public void setListaContaContabil(List<PessoaContaContabil> listaContaContabil) {
		this.listaContaContabil = listaContaContabil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CLIENTE")
	public Pessoa getClientePrincipal() {
		return clientePrincipal;
	}

	public void setClientePrincipal(Pessoa clientePrincipal) {
		this.clientePrincipal = clientePrincipal;
	}

	//bi-directional many-to-one association to Pessoa
	@OneToMany(mappedBy = "clientePrincipal")
	public List<Pessoa> getSubClientes() {
		return subClientes;
	}

	public void setSubClientes(List<Pessoa> subClientes) {
		this.subClientes = subClientes;
	}

	@Transient
	public ContaContabil getContaContabilByEmpresa(Unidade empresa, TipoRelacionamento tipoRelacionamento) {
		if (listaContaContabil != null) {
			for (PessoaContaContabil contaContabil : listaContaContabil) {
				if (contaContabil.getUnidade() == null || contaContabil.getUnidade().equals(empresa) && contaContabil.getTipoEspecial().equals(tipoRelacionamento)) {
					return contaContabil.getContaContabil();
				}
			}
		}
		return null;
	}

	@Transient
	public DadosBancarios getDadosBancarios() {
		for (DadosBancarios dadosBancarios : listaDadosBancarios) {
			if (dadosBancarios.getPrincipal()) return dadosBancarios;
		}
		return null;
	}

	@Transient
	public DadosBancarios getDadosBancariosRecentes() {
		for (DadosBancarios dadosBancarios : listaDadosBancarios) {
			new Comparator<DadosBancarios>() {
				@Override
				public int compare(DadosBancarios one, DadosBancarios other) {
					return one.getDataCriacao().compareTo(other.getDataCriacao());
				}
			};
			return dadosBancarios;
		}
		return null;
	}

	@Transient
	public Telefone getTelefone() {
		for (Telefone telefone : telefones) {
			if (telefone.getPrincipal()) return telefone;
		}
		return null;
	}

	@Transient
	public Telefone getFax() {
		List<Telefone> faxes = this.getFaxes();
		if (faxes != null && !faxes.isEmpty()) {
			for (Telefone fax : faxes) {
				if (fax.getPrincipal() != null && fax.getPrincipal()) {
					return fax;
				}
			}
			return faxes.get(0);
		}
		return null;
	}

	@Transient
	public List<Telefone> getFaxes() {
		List<Telefone> telefonesFiltrados = new ArrayList<>();

		for (Telefone telefone : telefones) {
			if (telefone.getTipo().equals(Telefone.TipoTelefone.Fax)) telefonesFiltrados.add(telefone);
		}

		return telefonesFiltrados;
	}

	@Transient
	public String getIdentificador() {
		return "";
	}

	@Transient
	public Boolean isFisica() {
		return TipoPessoa.PESSOA_FISICA.equals(getTipoPessoa());
	}

	@Transient
	public Boolean isJuridica() {
		return TipoPessoa.PESSOA_JURIDICA.equals(getTipoPessoa());
	}

	@Transient
	public Boolean isUnidade() {
		return TipoPessoa.UNIDADE.equals(getTipoPessoa());
	}

	@Transient
	public String getNomeAbreviado() {
		return StringUtils.substring(getNome(), 0, 17) + (getNome().length() > 17 ? "..." : "");
	}

	@Transient
	public String getNomeFantasia() {
		if (this instanceof PessoaJuridica)
			return this.getNomeFantasia();

		return nome;
	}

	@Transient
	public String getNomeUpperCase() {
		return this.nome != null && !Objects.equals(this.nome, "") ? this.nome.toUpperCase() : "";
	}

	@NotNull
	@Column(name = "situacao")
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Column(name = "notificacao")
	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	@Column(name = "FATURAMENTO_COBRANCA")
	public Boolean getFaturamentoCobranca() {
		return faturamentoCobranca;
	}

	public void setFaturamentoCobranca(Boolean faturamentoCobranca) {
		this.faturamentoCobranca = faturamentoCobranca;
	}

	@Transient
	public Avaliacao getAvaliacaoParaApresentacao() {
		return avaliacaoParaApresentacao;
	}

	public void setAvaliacaoParaApresentacao(Avaliacao avaliacaoParaApresentacao) {
		this.avaliacaoParaApresentacao = avaliacaoParaApresentacao;
	}

	@Transient
	public Notificacao[] getNotificacoes() {
		return Notificacao.values();
	}

	@Transient
	public boolean isAtivo() {
		if (this.getSituacao() == null)
			return false;

		return this.getSituacao().equals(Situacao.ENABLED);
	}

	/**
	 * Obter uma nova instância de pessoa.
	 */
	public Pessoa newInstance() {
		Pessoa pessoa = new Pessoa();
		pessoa.setCodigo(getCodigo());
		pessoa.setDocumento(getDocumento());
		pessoa.setAtualizador(getAtualizador());
		pessoa.setCriador(getCriador());
		pessoa.setDataAtualizacao(getDataAtualizacao());
		pessoa.setDataCriacao(getDataCriacao());
		pessoa.setDataHomologacao(getDataHomologacao());
		pessoa.setEmail(getEmail());
		pessoa.setEndereco(getEndereco());
		pessoa.setHomologado(getHomologado());
		pessoa.setHomologador(getHomologador());
		pessoa.setListaDadosBancarios(getListaDadosBancarios());
		pessoa.setNome(getNome());
		pessoa.setNotificacao(getNotificacao());
		pessoa.setSituacao(getSituacao());
		pessoa.setTelefones(getTelefones());
		pessoa.setTipoPessoa(getTipoPessoa());
		return pessoa;
	}

	@Transient
	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Transient
	public String getHomologacao() {
		if (homologado != null && homologado && dataHomologacao != null) {
			Locale LOCAL_BR = new Locale("pt", "BR");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", LOCAL_BR);
			String data = formatter.format(dataHomologacao);
			return homologador.getNome() + " - " + data;
		}
		return "Não";
	}

	@Transient
	public String getInativacao() {
		if (inativacaoManual == null || inativador == null)
			return "";

		if (inativacaoManual)
			return "Sim";
		return "Não";
	}

	@Transient
	public String getEmailContato() {

		if (this instanceof PessoaJuridica)
			return this.getEmailContato();

		return null;
	}

	@Override
	public String toString() {
		return "Pessoa [codigo=" + getCodigo() + ", nome=" + getNome() + ", tipoPessoa=" + getTipoPessoa() + ", documento=" + getDocumento() + ", email=" + getEmail() +
			", classificacaoCliente=" + getClassificacaoCliente() + ", situacao=" + getSituacao() + ", clientePrincipal=" + getClientePrincipal() + "]";
	}

	@Transient
	public ContaContabil getContaContabilPorTipoEmpresa(TipoRelacionamento tipo, Unidade empresa) {
		if (listaContaContabil != null && !listaContaContabil.isEmpty() && tipo != null && empresa != null) {
			for (PessoaContaContabil pessoaContaContabil : listaContaContabil) {
				Unidade unidade = empresa.getMatriz() == null ? empresa : empresa.getMatriz();
				Unidade unidadeContabil = pessoaContaContabil.getContaContabil().getEmpresa().getMatriz() == null ? pessoaContaContabil.getContaContabil().getEmpresa() : pessoaContaContabil.getContaContabil().getEmpresa().getMatriz();

				if (tipo.equals(pessoaContaContabil.getTipoEspecial()) && unidade.equals(unidadeContabil)) {
					return pessoaContaContabil.getContaContabil();
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Pessoa pessoa = (Pessoa) o;
		return homologado.equals(pessoa.homologado) &&
			dataHomologacao.equals(pessoa.dataHomologacao) &&
			nome.equals(pessoa.nome) &&
			documento.equals(pessoa.documento) &&
			email.equals(pessoa.email) &&
			inativacaoManual.equals(pessoa.inativacaoManual) &&
			dataInativacao.equals(pessoa.dataInativacao) &&
			selecionado.equals(pessoa.selecionado) &&
			faturamentoCobranca.equals(pessoa.faturamentoCobranca);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), homologado, dataHomologacao, nome, documento, email, inativacaoManual, dataInativacao, selecionado, faturamentoCobranca);
	}

	public enum Situacao {
		DISABLED("Inativo"),
		ENABLED("Ativo");

		private final String nome;

		Situacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Inativo";
				case 1:
					return "Ativo";
				default:
					return "";
			}
		}
	}

	public enum TipoPessoa {
		PESSOA_JURIDICA, PESSOA_FISICA, UNIDADE, PESSOA_SIMPLES, FAVORECIDO;

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Pessoa Jurídica";
				case 1:
					return "Pessoa Física";
				case 2:
					return "Unidade";
				case 3:
					return "Pessoa Simples"; // SGI-703: Utilizado para Fornecedor de Cupom Fiscal
				case 4:
					return "Favorecido";
				default:
					return "";
			}
		}
	}

	public enum Notificacao {
		NENHUM, EMAIL, SMS, EMAIL_SMS
	}
}

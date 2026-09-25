package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.comercial.domain.ClassificacaoCliente;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil.TipoRelacionamento;
import br.com.caelum.stella.hibernate.validator.IE;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;

@Entity
@Table(name = "PESSOA_JURIDICA")
public class PessoaJuridica extends Pessoa {

	private static final long serialVersionUID = 1L;
	private Cnae cnae;
	private NaturezaJuridica naturezaJuridica;
	private String nomeFantasia;

	private String emailContato;
	private String inscricaoEstadual;
	private String ufInscricaoEstadual;
	private String inscricaoMunicipal;
	private BigDecimal desempenho;
	private TipoPessoaJuridica tipoPessoaJuridica;
	private CanalComunicacao canalPreferencial = CanalComunicacao.EMAIL;
	private Boolean notaFiscalEletronica = Boolean.FALSE;
	private Boolean fornecimentoConsumo = Boolean.FALSE;
	private Boolean fornecimentoTransporte = Boolean.FALSE;
	//	private List<NaturezaFornecimento> naturezasFornecimento = new ArrayList<NaturezaFornecimento>();
	private ContaContabil contaContabil;
	private Boolean mei;

	public PessoaJuridica() {
		super();
		setTipoPessoa(TipoPessoa.PESSOA_JURIDICA);
	}

	public PessoaJuridica(Cnae cnae, ClassificacaoCliente classificacaoCliente) {
		super();
		this.cnae = cnae;
		super.setClassificacaoCliente(classificacaoCliente);
	}

	public PessoaJuridica(Cnae cnae, ClassificacaoCliente classificacaoCliente, Endereco endereco) {
		super();
		this.cnae = cnae;
		super.setClassificacaoCliente(classificacaoCliente);
		super.setEndereco(endereco);
	}

	public PessoaJuridica(Long codigo, String nome, String cnpj, String inscricaoEstadual) {
		super();
		setTipoPessoa(TipoPessoa.PESSOA_JURIDICA);
		setCodigo(codigo);
		setNome(nome);
		setDocumento(cnpj);
		setInscricaoEstadual(inscricaoEstadual);
	}

	public PessoaJuridica(String documento) {
		super();
		setDocumento(documento);
	}

	@Column(name = "NOME_FANTASIA", length = 100)
	@NotNull
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	@Column(name = "INSCRICAO_ESTADUAL", length = 20)
	@Length(max = 20)
	public String getInscricaoEstadual() {
		return this.inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	@Column(name = "UF_INSCRICAO_ESTADUAL", length = 2)
	@Length(max = 2)
	public String getUfInscricaoEstadual() {
		return ufInscricaoEstadual;
	}

	public void setUfInscricaoEstadual(String ufInscricaoEstadual) {
		this.ufInscricaoEstadual = ufInscricaoEstadual;
	}

	@Column(name = "INSCRICAO_MUNICIPAL", length = 20)
	@Length(max = 20)
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CNAE")
	public Cnae getCnae() {
		return cnae;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_NATUREZA_JURIDICA")
	public NaturezaJuridica getNaturezaJuridica() {
		return naturezaJuridica;
	}

	public void setNaturezaJuridica(NaturezaJuridica naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
	}

	@Column(name = "EMAIL_CONTATO", length = 100)
	@Length(max = 100)
	@Email
	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}

	public String estadoField() {
		return "ufInscricaoEstadual";
	}

	public boolean formatted() {
		return true;
	}

	public String ieField() {
		return "inscricaoEstadual";
	}

	public String message() {
		return "Invalido";
	}

	public Class<? extends Annotation> annotationType() {
		return IE.class;
	}

	@Column(name = "CANAL_PREFERENCIAL", nullable = false)
	@NotNull
	public CanalComunicacao getCanalPreferencial() {
		return canalPreferencial;
	}

	public void setCanalPreferencial(CanalComunicacao canalPreferencial) {
		this.canalPreferencial = canalPreferencial;
	}

	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	public void setPrincipal(Boolean principal) {
	}

	@Column(name = "NOTA_FISCAL_ELETRONICA", nullable = false, length = 1)
	@NotNull
	public Boolean getNotaFiscalEletronica() {
		return notaFiscalEletronica;
	}

//	@ManyToMany(targetEntity=br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento.class, fetch = FetchType.LAZY)
//	@JoinTable(
//		name="FORNECEDOR_NAT_FORNECIMENTO",
//		joinColumns=@JoinColumn(name="CODIGO_FORNECEDOR"),
//		inverseJoinColumns=@JoinColumn(name="CODIGO_NATUREZA_FORNECIMENTO")
//	)
//	@OrderBy(value="nome")
//	public List<NaturezaFornecimento> getNaturezasFornecimento() {
//		return naturezasFornecimento;
//	}
//
//	public void setNaturezasFornecimento(List<NaturezaFornecimento> naturezasFornecimento) {
//		this.naturezasFornecimento = naturezasFornecimento;
//	}
//
//	public void setListaContato(List<Contato> listaContato) {
//		this.listaContato = listaContato;
//	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pessoaJuridica")
//	@Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//	public List<Contato> getListaContato() {
//		return listaContato;
//	}

	public void setNotaFiscalEletronica(Boolean notaFiscalEletronica) {
		this.notaFiscalEletronica = notaFiscalEletronica;
	}

	@Column(name = "FORNECIMENTO_CONSUMO", nullable = false, length = 1)
	@NotNull
	public Boolean getFornecimentoConsumo() {
		return fornecimentoConsumo;
	}

	public void setFornecimentoConsumo(Boolean fornecimentoConsumo) {
		this.fornecimentoConsumo = fornecimentoConsumo;
	}

	@Column(name = "FORNECIMENTO_TRANSPORTE", nullable = false, length = 1)
	@NotNull
	public Boolean getFornecimentoTransporte() {
		return fornecimentoTransporte;
	}

	public void setFornecimentoTransporte(Boolean fornecimentoTransporte) {
		this.fornecimentoTransporte = fornecimentoTransporte;
	}

	//TODO : este método deve ser removido no sprint 4.4, pois será substiuído pelo relacionamente entre as classes Pessoa / PessoaContaContabil / ContaContabil
	@Transient
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "MEI")
	public Boolean getMei() {
		return mei;
	}

	public void setMei(Boolean mei) {
		this.mei = mei;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoPessoaJuridica getTipoPessoaJuridica() {
		return tipoPessoaJuridica;
	}

	public void setTipoPessoaJuridica(TipoPessoaJuridica tipoPessoaJuridica) {
		this.tipoPessoaJuridica = tipoPessoaJuridica;
	}

	@Transient
	public BigDecimal getDesempenho() {
		return desempenho;
	}

	public void setDesempenho(BigDecimal desempenho) {
		this.desempenho = desempenho;
	}

	@Transient
	public String getInscricaoEstadualUnidadeFederal() {
		if (this != null) {
			if (this.getInscricaoEstadual() != null && !this.getInscricaoEstadual().isEmpty()) {
				return this.getInscricaoEstadual() + " / " + this.getUfInscricaoEstadual();
			}
		}
		return "";
	}

	@Transient
	public String getFornecimentos() {
		String fornecimento;
		if (this.fornecimentoConsumo == true) {
			fornecimento = "Consumo";
		} else if (this.fornecimentoTransporte == true) {
			fornecimento = "Transporte";
		} else {
			fornecimento = "";
		}
		return fornecimento;
	}

	@Override
	public String toString() {
		return "PessoaJuridica [nomeFantasia=" + nomeFantasia + "]";
	}

	@Transient
	public ContaContabil getContaContabilPessoaJuridicaByEmpresa(Unidade empresa) {
		return getContaContabilByEmpresa(empresa, TipoRelacionamento.FORNECEDOR);
	}

	public enum TipoPessoaJuridica {
		FORNECEDOR("Fornecedor"),
		CLIENTE("Cliente"),
		FORNECEDOR_CLIENTE("Fornecedor/Cliente"),
		UNIDADE("Unidade"),
		SUBCLIENTE("Subcliente");

		private final String nome;

		TipoPessoaJuridica(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum CanalComunicacao {
		TELEFONE("Telefone"),
		CORRESPONDENCIA("Correspondência"),
		EMAIL("E-mail");

		private final String nome;

		CanalComunicacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

//	public void addContato(Contato contato) {
//		listaContato.add(contato);
//		contato.setPessoaJuridica(this);
//	}
//
//	@Transient
//	public Contato getContato(){
//		if (listaContato != null && !listaContato.isEmpty()){
//			for (Contato contato : listaContato){
//				if (contato.getPrincipal() != null && contato.getPrincipal()){
//					return contato;
//				}
//			}
//			return listaContato.get(0);
//		}
//		return null;
//	}

	public enum Mei {
		SIM(true), NAO(false);

		private final boolean status;

		Mei(boolean status) {
			this.status = status;
		}

		public boolean getStatus() {
			return status;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Sim";
				case 1:
					return "Não";
				default:
					break;
			}
			return super.toString();
		}

	}
}

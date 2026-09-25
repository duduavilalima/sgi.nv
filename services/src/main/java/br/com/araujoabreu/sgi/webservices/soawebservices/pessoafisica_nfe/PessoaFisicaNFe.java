package br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;


@XmlRootElement(name = "PessoaFisicaNFeResult")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaFisicaNFe extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2512450170350604193L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PessoaFisicaNFe.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaNFe"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nome");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Nome"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nomeMae");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "NomeMae"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataNascimento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "DataNascimento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("escolaridade");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Escolaridade"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sexo");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Sexo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("enderecos");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Enderecos"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("telefones");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Telefones"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Telefone"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "Telefone"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("emails");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Emails"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Email"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "Email"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("atividadeProfissional");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "AtividadeProfissional"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("rendaPresumida");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "RendaPresumida"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cargo");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Cargo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("mensagem");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Mensagem"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Status"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("situacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "SituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private String documento;
	private String nome;
	private String nomeMae;
	private String dataNascimento;
	private String escolaridade;
	private String sexo;
	private Endereco[] enderecos;
	private Telefone[] telefones;
	private Email[] emails;
	private String atividadeProfissional;
	private String rendaPresumida;
	private String cargo;
	private String mensagem;
	private boolean status;
	private Transacao transacao;
	private String situacaoRFB;


	public PessoaFisicaNFe() {
	}


	public PessoaFisicaNFe(String documento, String nome, String nomeMae, String dataNascimento, String escolaridade,
						   String sexo, Endereco[] enderecos,
						   Telefone[] telefone,
						   Email[] email,
						   String atividadeProfissional, String rendaPresumida, String cargo, String mensagem, boolean status,
						   Transacao transacao, String situacaoRFB, String protocoloRFB, String digitoVerificador) {
		super();
		this.documento = documento;
		this.nome = nome;
		this.nomeMae = nomeMae;
		this.dataNascimento = dataNascimento;
		this.escolaridade = escolaridade;
		this.sexo = sexo;
		this.enderecos = enderecos;
		this.telefones = telefone;
		this.emails = email;
		this.atividadeProfissional = atividadeProfissional;
		this.rendaPresumida = rendaPresumida;
		this.cargo = cargo;
		this.mensagem = mensagem;
		this.status = status;
		this.transacao = transacao;
		this.situacaoRFB = situacaoRFB;
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(String mechType, Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(String mechType, Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

	@JsonProperty("Documento")
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@JsonProperty("Nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("NomeMae")
	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	@JsonProperty("DataNascimento")
	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@JsonProperty("Escolaridade")
	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	@JsonProperty("Sexo")
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@JsonProperty("Enderecos")
	public Endereco[] getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(Endereco[] enderecos) {
		this.enderecos = enderecos;
	}

	@JsonProperty("Telefones")
	public Telefone[] getTelefones() {
		return telefones;
	}

	public void setTelefone(Telefone[] telefones) {
		this.telefones = telefones;
	}

	@JsonProperty("Emails")
	public Email[] getEmails() {
		return emails;
	}

	public void setEmails(Email[] email) {
		this.emails = email;
	}

	@JsonProperty("AtividadeProfissional")
	public String getAtividadeProfissional() {
		return atividadeProfissional;
	}

	public void setAtividadeProfissional(String atividadeProfissional) {
		this.atividadeProfissional = atividadeProfissional;
	}

	@JsonProperty("RendaPresumida")
	public String getRendaPresumida() {
		return rendaPresumida;
	}

	public void setRendaPresumida(String rendaPresumida) {
		this.rendaPresumida = rendaPresumida;
	}

	@JsonProperty("Cargo")
	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@JsonProperty("Mensagem")
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@JsonProperty("Status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@JsonProperty("Transacao")
	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	@JsonProperty("SituacaoRFB")
	public String getSituacaoRFB() {
		return situacaoRFB;
	}

	public void setSituacaoRFB(String situacaoRFB) {
		this.situacaoRFB = situacaoRFB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((atividadeProfissional == null) ? 0 : atividadeProfissional.hashCode());
		result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + Arrays.hashCode(emails);
		result = prime * result + Arrays.hashCode(enderecos);
		result = prime * result + ((escolaridade == null) ? 0 : escolaridade.hashCode());
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nomeMae == null) ? 0 : nomeMae.hashCode());
		result = prime * result + ((rendaPresumida == null) ? 0 : rendaPresumida.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((situacaoRFB == null) ? 0 : situacaoRFB.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(telefones);
		result = prime * result + ((transacao == null) ? 0 : transacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaFisicaNFe other = (PessoaFisicaNFe) obj;
		if (atividadeProfissional == null) {
			if (other.atividadeProfissional != null)
				return false;
		} else if (!atividadeProfissional.equals(other.atividadeProfissional))
			return false;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (!Arrays.equals(emails, other.emails))
			return false;
		if (!Arrays.equals(enderecos, other.enderecos))
			return false;
		if (escolaridade == null) {
			if (other.escolaridade != null)
				return false;
		} else if (!escolaridade.equals(other.escolaridade))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nomeMae == null) {
			if (other.nomeMae != null)
				return false;
		} else if (!nomeMae.equals(other.nomeMae))
			return false;
		if (rendaPresumida == null) {
			if (other.rendaPresumida != null)
				return false;
		} else if (!rendaPresumida.equals(other.rendaPresumida))
			return false;
		if (sexo == null) {
			if (other.sexo != null)
				return false;
		} else if (!sexo.equals(other.sexo))
			return false;
		if (situacaoRFB == null) {
			if (other.situacaoRFB != null)
				return false;
		} else if (!situacaoRFB.equals(other.situacaoRFB))
			return false;
		if (status != other.status)
			return false;
		if (!Arrays.equals(telefones, other.telefones))
			return false;
		if (transacao == null) {
			if (other.transacao != null)
				return false;
		} else if (!transacao.equals(other.transacao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PessoaFisicaNFe [documento=" + documento + ", nome=" + nome + ", nomeMae=" + nomeMae
			+ ", dataNascimento=" + dataNascimento + ", escolaridade=" + escolaridade + ", sexo=" + sexo
			+ ", enderecos=" + Arrays.toString(enderecos) + ", telefone=" + Arrays.toString(telefones) + ", email="
			+ Arrays.toString(emails) + ", atividadeProfissional=" + atividadeProfissional + ", rendaPresumida="
			+ rendaPresumida + ", cargo=" + cargo + ", mensagem=" + mensagem + ", status=" + status + ", transacao="
			+ transacao + ", situacaoRFB=" + situacaoRFB + "]";
	}

}

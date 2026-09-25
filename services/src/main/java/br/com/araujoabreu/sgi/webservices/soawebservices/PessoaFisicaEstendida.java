package br.com.araujoabreu.sgi.webservices.soawebservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaFisicaEstendida extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1891722829591803518L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PessoaFisicaEstendida.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaEstendida"));
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
//        typeDesc.addFieldDesc(elemField);
//        elemField = new org.apache.axis.description.ElementDesc();
//        elemField.setFieldName("emails");
//        elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Emails"));
//        elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Email"));
//        elemField.setMinOccurs(0);
//        elemField.setNillable(false);
//        elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "Email"));
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

	//    private Email[] emails;
	private EnderecoNfe[] enderecos;
	private Telefone[] telefones;
	private String atividadeProfissional;
	private String rendaPresumida;
	private String cargo;
	private String mensagem;
	private boolean status;
	private String situacaoRFB;

	public PessoaFisicaEstendida() {
	}

	public PessoaFisicaEstendida(String documento,
								 String nome,
								 String nomeMae,
								 String dataNascimento,
								 String escolaridade,
								 String sexo,
								 EnderecoNfe[] enderecos,
								 Telefone[] telefones,
								 Email[] emails,
								 String atividadeProfissional,
								 String rendaPresumida,
								 String cargo,
								 String mensagem,
								 boolean status,
								 String situacaoRFB) {
		this.documento = documento;
		this.nome = nome;
		this.nomeMae = nomeMae;
		this.dataNascimento = dataNascimento;
		this.escolaridade = escolaridade;
		this.sexo = sexo;
		this.enderecos = enderecos;
		this.telefones = telefones;
//        this.emails = emails;
		this.atividadeProfissional = atividadeProfissional;
		this.rendaPresumida = rendaPresumida;
		this.cargo = cargo;
		this.mensagem = mensagem;
		this.status = status;
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

	/**
	 * Gets the documento value for this PessoaFisicaEstendida.
	 *
	 * @return documento
	 */
	@JsonProperty("Documento")
	public String getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento value for this PessoaFisicaEstendida.
	 *
	 * @param documento
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Gets the nome value for this PessoaFisicaEstendida.
	 *
	 * @return nome
	 */
	@JsonProperty("Nome")
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the nome value for this PessoaFisicaEstendida.
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the nomeMae value for this PessoaFisicaEstendida.
	 *
	 * @return nomeMae
	 */
	@JsonProperty("NomeMae")
	public String getNomeMae() {
		return nomeMae;
	}

	/**
	 * Sets the nomeMae value for this PessoaFisicaEstendida.
	 *
	 * @param nomeMae
	 */
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	/**
	 * Gets the dataNascimento value for this PessoaFisicaEstendida.
	 *
	 * @return dataNascimento
	 */
	@JsonProperty("DataNascimento")
	public String getDataNascimento() {
		return dataNascimento;
	}

	/**
	 * Sets the dataNascimento value for this PessoaFisicaEstendida.
	 *
	 * @param dataNascimento
	 */
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/**
	 * Gets the escolaridade value for this PessoaFisicaEstendida.
	 *
	 * @return escolaridade
	 */
	@JsonProperty("Escolaridade")
	public String getEscolaridade() {
		return escolaridade;
	}

	/**
	 * Sets the escolaridade value for this PessoaFisicaEstendida.
	 *
	 * @param escolaridade
	 */
	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	/**
	 * Gets the sexo value for this PessoaFisicaEstendida.
	 *
	 * @return sexo
	 */
	@JsonProperty("Sexo")
	public String getSexo() {
		return sexo;
	}

	//	@JsonProperty("Emails")
//    public Email[] getEmails() {
//        return emails;
//    }
//
//    /**
//     * Sets the emails value for this PessoaFisicaEstendida.
//     * @param emails
//     */
//    public void setEmails(Email[] emails) {
//        this.emails = emails;
//    }

	/**
	 * Sets the sexo value for this PessoaFisicaEstendida.
	 *
	 * @param sexo
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * Gets the enderecos value for this PessoaFisicaEstendida.
	 *
	 * @return enderecos
	 */
	@JsonProperty("Enderecos")
	public EnderecoNfe[] getEnderecos() {
		return enderecos;
	}

	/**
	 * Sets the enderecos value for this PessoaFisicaEstendida.
	 *
	 * @param enderecos
	 */
	public void setEnderecos(EnderecoNfe[] enderecos) {
		this.enderecos = enderecos;
	}

	/**
	 * Gets the telefones value for this PessoaFisicaEstendida.
	 *
	 * @return telefones
	 */
	@JsonProperty("Telefones")
	public Telefone[] getTelefones() {
		return telefones;
	}

	/**
	 * Sets the telefones value for this PessoaFisicaEstendida.
	 *
	 * @param telefones
	 */
	public void setTelefones(Telefone[] telefones) {
		this.telefones = telefones;
	}

	/**
	 * Gets the atividadeProfissional value for this PessoaFisicaEstendida.
	 *
	 * @return atividadeProfissional
	 */
	@JsonProperty("AtividadeProfissional")
	public String getAtividadeProfissional() {
		return atividadeProfissional;
	}

	/**
	 * Sets the atividadeProfissional value for this PessoaFisicaEstendida.
	 *
	 * @param atividadeProfissional
	 */
	public void setAtividadeProfissional(String atividadeProfissional) {
		this.atividadeProfissional = atividadeProfissional;
	}

	/**
	 * Gets the rendaPresumida value for this PessoaFisicaEstendida.
	 *
	 * @return rendaPresumida
	 */
	@JsonProperty("RendaPresumida")
	public String getRendaPresumida() {
		return rendaPresumida;
	}

	/**
	 * Sets the rendaPresumida value for this PessoaFisicaEstendida.
	 *
	 * @param rendaPresumida
	 */
	public void setRendaPresumida(String rendaPresumida) {
		this.rendaPresumida = rendaPresumida;
	}

	/**
	 * Gets the cargo value for this PessoaFisicaEstendida.
	 *
	 * @return cargo
	 */
	@JsonProperty("Cargo")
	public String getCargo() {
		return cargo;
	}

	/**
	 * Sets the cargo value for this PessoaFisicaEstendida.
	 *
	 * @param cargo
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	/**
	 * Gets the mensagem value for this PessoaFisicaEstendida.
	 *
	 * @return mensagem
	 */
	@JsonProperty("Mensagem")
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this PessoaFisicaEstendida.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this PessoaFisicaEstendida.
	 *
	 * @return status
	 */
	@JsonProperty("Status")
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this PessoaFisicaEstendida.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
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
//		result = prime * result + Arrays.hashCode(emails);
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
		PessoaFisicaEstendida other = (PessoaFisicaEstendida) obj;
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
//		if (!Arrays.equals(emails, other.emails))
//			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "PessoaFisicaEstendida [documento=" + documento + ", nome=" + nome + ", nomeMae=" + nomeMae
			+ ", dataNascimento=" + dataNascimento + ", escolaridade=" + escolaridade + ", sexo=" + sexo
			+ ", enderecos=" + Arrays.toString(enderecos) + ", telefones=" + Arrays.toString(telefones)
			+ "atividadeProfissional=" + atividadeProfissional
			+ ", rendaPresumida=" + rendaPresumida + ", cargo=" + cargo + ", mensagem=" + mensagem + ", status="
			+ status + ", situacaoRFB=" + situacaoRFB + "]";
	}

}

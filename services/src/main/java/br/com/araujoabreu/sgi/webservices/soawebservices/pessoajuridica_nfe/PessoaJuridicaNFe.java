package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import br.com.araujoabreu.sgi.webservices.soawebservices.EnderecoNfe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "PessoaJuridicaNFeResult")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaJuridicaNFe implements java.io.Serializable {

	private static final long serialVersionUID = -6038915611425570489L;
	@Transient
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PessoaJuridicaNFe.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "PessoaJuridicaNFe"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("razaoSocial");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "RazaoSocial"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nomeFantasia");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "NomeFantasia"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataFundacao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "DataFundacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoAtividadeEconomica");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CodigoAtividadeEconomica"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoAtividadeEconomicaDescricao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CodigoAtividadeEconomicaDescricao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoNaturezaJuridica");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CodigoNaturezaJuridica"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoNaturezaJuridicaDescricao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CodigoNaturezaJuridicaDescricao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("situacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "SituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataSituacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "DataSituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataConsultaRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "DataConsultaRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("motivoSituacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "MotivoSituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("motivoEspecialSituacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "MotivoEspecialSituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataMotivoEspecialSituacaoRFB");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "DataMotivoEspecialSituacaoRFB"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("CNAES");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAES"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE"));
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("enderecos");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Enderecos"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "EnderecoNfe"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "EnderecoNfe"));
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("mensagem");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Mensagem"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Status"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		//Novos campos incluídos na atualização do serviço em 08/08/2015
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("capital");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Capital"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("email");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Email"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("telefone");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Telefone"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("qsa");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "QSA"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "QSA"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("transacao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Transacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Transacao"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);


	}

	@JsonProperty("Documento")
	private String documento;
	@JsonProperty("RazaoSocial")
	private String razaoSocial;
	@JsonProperty("NomeFantasia")
	private String nomeFantasia;
	@JsonProperty("DataFundacao")
	private String dataFundacao;
	@JsonProperty("CodigoAtividadeEconomica")
	private String codigoAtividadeEconomica;
	@JsonProperty("CodigoAtividadeEconomicaDescricao")
	private String codigoAtividadeEconomicaDescricao;
	@JsonProperty("CodigoNaturezaJuridica")
	private String codigoNaturezaJuridica;
	@JsonProperty("CodigoNaturezaJuridicaDescricao")
	private String codigoNaturezaJuridicaDescricao;
	@JsonProperty("SituacaoRFB")
	private String situacaoRFB;
	@JsonProperty("DataSituacaoRFB")
	private String dataSituacaoRFB;
	@JsonProperty("DataConsultaRFB")
	private String dataConsultaRFB;
	@JsonProperty("MotivoSituacaoRFB")
	private String motivoSituacaoRFB;
	@JsonProperty("MotivoEspecialSituacaoRFB")
	private String motivoEspecialSituacaoRFB;
	@JsonProperty("DataMotivoEspecialSituacaoRFB")
	private String dataMotivoEspecialSituacaoRFB;
	@JsonProperty("CNAES")
	private CNAE[] CNAES;
	@JsonProperty("Enderecos")
	private EnderecoNfe[] enderecos;
	@JsonProperty("Mensagem")
	private String mensagem;
	@JsonProperty("Status")
	private boolean status;
	@JsonProperty("Capital")
	private String capital;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Telefone")
	private String telefone;
	private QSA qsa;
	@JsonProperty("Transacao")
	private Transacao transacao;


	public PessoaJuridicaNFe() {
	}

	public PessoaJuridicaNFe(String documento, String razaoSocial, String nomeFantasia, String dataFundacao, String codigoAtividadeEconomica, String codigoAtividadeEconomicaDescricao, String codigoNaturezaJuridica, String codigoNaturezaJuridicaDescricao, String situacaoRFB, String dataSituacaoRFB, String dataConsultaRFB, String motivoSituacaoRFB, String motivoEspecialSituacaoRFB, String dataMotivoEspecialSituacaoRFB, CNAE[] cNAES, EnderecoNfe[] enderecos, String mensagem, boolean status, String capital, String email, String telefone, QSA qsa, Transacao transacao) {
		super();
		this.documento = documento;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.dataFundacao = dataFundacao;
		this.codigoAtividadeEconomica = codigoAtividadeEconomica;
		this.codigoAtividadeEconomicaDescricao = codigoAtividadeEconomicaDescricao;
		this.codigoNaturezaJuridica = codigoNaturezaJuridica;
		this.codigoNaturezaJuridicaDescricao = codigoNaturezaJuridicaDescricao;
		this.situacaoRFB = situacaoRFB;
		this.dataSituacaoRFB = dataSituacaoRFB;
		this.dataConsultaRFB = dataConsultaRFB;
		this.motivoSituacaoRFB = motivoSituacaoRFB;
		this.motivoEspecialSituacaoRFB = motivoEspecialSituacaoRFB;
		this.dataMotivoEspecialSituacaoRFB = dataMotivoEspecialSituacaoRFB;
		CNAES = cNAES;
		this.enderecos = enderecos;
		this.mensagem = mensagem;
		this.status = status;
		this.capital = capital;
		this.email = email;
		this.telefone = telefone;
		this.qsa = qsa;
		this.transacao = transacao;
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

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getDataFundacao() {
		return dataFundacao;
	}

	public void setDataFundacao(String dataFundacao) {
		this.dataFundacao = dataFundacao;
	}

	public String getCodigoAtividadeEconomica() {
		return codigoAtividadeEconomica;
	}

	public void setCodigoAtividadeEconomica(String codigoAtividadeEconomica) {
		this.codigoAtividadeEconomica = codigoAtividadeEconomica;
	}

	public String getCodigoAtividadeEconomicaDescricao() {
		return codigoAtividadeEconomicaDescricao;
	}

	public void setCodigoAtividadeEconomicaDescricao(String codigoAtividadeEconomicaDescricao) {
		this.codigoAtividadeEconomicaDescricao = codigoAtividadeEconomicaDescricao;
	}

	public String getCodigoNaturezaJuridica() {
		return codigoNaturezaJuridica;
	}

	public void setCodigoNaturezaJuridica(String codigoNaturezaJuridica) {
		this.codigoNaturezaJuridica = codigoNaturezaJuridica;
	}

	public String getCodigoNaturezaJuridicaDescricao() {
		return codigoNaturezaJuridicaDescricao;
	}

	public void setCodigoNaturezaJuridicaDescricao(String codigoNaturezaJuridicaDescricao) {
		this.codigoNaturezaJuridicaDescricao = codigoNaturezaJuridicaDescricao;
	}

	public String getSituacaoRFB() {
		return situacaoRFB;
	}

	public void setSituacaoRFB(String situacaoRFB) {
		this.situacaoRFB = situacaoRFB;
	}

	public String getDataSituacaoRFB() {
		return dataSituacaoRFB;
	}

	public void setDataSituacaoRFB(String dataSituacaoRFB) {
		this.dataSituacaoRFB = dataSituacaoRFB;
	}

	public String getDataConsultaRFB() {
		return dataConsultaRFB;
	}

	public void setDataConsultaRFB(String dataConsultaRFB) {
		this.dataConsultaRFB = dataConsultaRFB;
	}

	public String getMotivoSituacaoRFB() {
		return motivoSituacaoRFB;
	}

	public void setMotivoSituacaoRFB(String motivoSituacaoRFB) {
		this.motivoSituacaoRFB = motivoSituacaoRFB;
	}

	public String getMotivoEspecialSituacaoRFB() {
		return motivoEspecialSituacaoRFB;
	}

	public void setMotivoEspecialSituacaoRFB(String motivoEspecialSituacaoRFB) {
		this.motivoEspecialSituacaoRFB = motivoEspecialSituacaoRFB;
	}

	public String getDataMotivoEspecialSituacaoRFB() {
		return dataMotivoEspecialSituacaoRFB;
	}

	public void setDataMotivoEspecialSituacaoRFB(String dataMotivoEspecialSituacaoRFB) {
		this.dataMotivoEspecialSituacaoRFB = dataMotivoEspecialSituacaoRFB;
	}

	public CNAE[] getCNAES() {
		return CNAES;
	}

	public void setCNAES(CNAE[] cNAES) {
		CNAES = cNAES;
	}

	public EnderecoNfe[] getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(EnderecoNfe[] enderecos) {
		this.enderecos = enderecos;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public QSA getQsa() {
		return qsa;
	}

	public void setQsa(QSA qsa) {
		this.qsa = qsa;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(CNAES);
		result = prime * result + ((codigoAtividadeEconomica == null) ? 0 : codigoAtividadeEconomica.hashCode());
		result = prime * result + ((codigoAtividadeEconomicaDescricao == null) ? 0 : codigoAtividadeEconomicaDescricao.hashCode());
		result = prime * result + ((codigoNaturezaJuridica == null) ? 0 : codigoNaturezaJuridica.hashCode());
		result = prime * result + ((codigoNaturezaJuridicaDescricao == null) ? 0 : codigoNaturezaJuridicaDescricao.hashCode());
		result = prime * result + ((dataConsultaRFB == null) ? 0 : dataConsultaRFB.hashCode());
		result = prime * result + ((dataFundacao == null) ? 0 : dataFundacao.hashCode());
		result = prime * result + ((dataMotivoEspecialSituacaoRFB == null) ? 0 : dataMotivoEspecialSituacaoRFB.hashCode());
		result = prime * result + ((dataSituacaoRFB == null) ? 0 : dataSituacaoRFB.hashCode());
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + Arrays.hashCode(enderecos);
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((motivoEspecialSituacaoRFB == null) ? 0 : motivoEspecialSituacaoRFB.hashCode());
		result = prime * result + ((motivoSituacaoRFB == null) ? 0 : motivoSituacaoRFB.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime * result + ((situacaoRFB == null) ? 0 : situacaoRFB.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaJuridicaNFe other = (PessoaJuridicaNFe) obj;
		if (!Arrays.equals(CNAES, other.CNAES))
			return false;
		if (codigoAtividadeEconomica == null) {
			if (other.codigoAtividadeEconomica != null)
				return false;
		} else if (!codigoAtividadeEconomica.equals(other.codigoAtividadeEconomica))
			return false;
		if (codigoAtividadeEconomicaDescricao == null) {
			if (other.codigoAtividadeEconomicaDescricao != null)
				return false;
		} else if (!codigoAtividadeEconomicaDescricao.equals(other.codigoAtividadeEconomicaDescricao))
			return false;
		if (codigoNaturezaJuridica == null) {
			if (other.codigoNaturezaJuridica != null)
				return false;
		} else if (!codigoNaturezaJuridica.equals(other.codigoNaturezaJuridica))
			return false;
		if (codigoNaturezaJuridicaDescricao == null) {
			if (other.codigoNaturezaJuridicaDescricao != null)
				return false;
		} else if (!codigoNaturezaJuridicaDescricao.equals(other.codigoNaturezaJuridicaDescricao))
			return false;
		if (dataConsultaRFB == null) {
			if (other.dataConsultaRFB != null)
				return false;
		} else if (!dataConsultaRFB.equals(other.dataConsultaRFB))
			return false;
		if (dataFundacao == null) {
			if (other.dataFundacao != null)
				return false;
		} else if (!dataFundacao.equals(other.dataFundacao))
			return false;
		if (dataMotivoEspecialSituacaoRFB == null) {
			if (other.dataMotivoEspecialSituacaoRFB != null)
				return false;
		} else if (!dataMotivoEspecialSituacaoRFB.equals(other.dataMotivoEspecialSituacaoRFB))
			return false;
		if (dataSituacaoRFB == null) {
			if (other.dataSituacaoRFB != null)
				return false;
		} else if (!dataSituacaoRFB.equals(other.dataSituacaoRFB))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (!Arrays.equals(enderecos, other.enderecos))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		if (motivoEspecialSituacaoRFB == null) {
			if (other.motivoEspecialSituacaoRFB != null)
				return false;
		} else if (!motivoEspecialSituacaoRFB.equals(other.motivoEspecialSituacaoRFB))
			return false;
		if (motivoSituacaoRFB == null) {
			if (other.motivoSituacaoRFB != null)
				return false;
		} else if (!motivoSituacaoRFB.equals(other.motivoSituacaoRFB))
			return false;
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null)
				return false;
		} else if (!nomeFantasia.equals(other.nomeFantasia))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (situacaoRFB == null) {
			if (other.situacaoRFB != null)
				return false;
		} else if (!situacaoRFB.equals(other.situacaoRFB))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PessoaJuridicaNFe [documento=" + documento + ", razaoSocial=" + razaoSocial + ", nomeFantasia=" + nomeFantasia + ", dataFundacao="
			+ dataFundacao + ", codigoAtividadeEconomica=" + codigoAtividadeEconomica + ", codigoAtividadeEconomicaDescricao="
			+ codigoAtividadeEconomicaDescricao + ", codigoNaturezaJuridica=" + codigoNaturezaJuridica + ", codigoNaturezaJuridicaDescricao="
			+ codigoNaturezaJuridicaDescricao + ", situacaoRFB=" + situacaoRFB + ", dataSituacaoRFB=" + dataSituacaoRFB + ", dataConsultaRFB="
			+ dataConsultaRFB + ", motivoSituacaoRFB=" + motivoSituacaoRFB + ", motivoEspecialSituacaoRFB=" + motivoEspecialSituacaoRFB
			+ ", dataMotivoEspecialSituacaoRFB=" + dataMotivoEspecialSituacaoRFB + ", CNAES=" + Arrays.toString(CNAES) + ", enderecos="
			+ Arrays.toString(enderecos) + ", mensagem=" + mensagem + ", status=" + status + "]";
	}

}

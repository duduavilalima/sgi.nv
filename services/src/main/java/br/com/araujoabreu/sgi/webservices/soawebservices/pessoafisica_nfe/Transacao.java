package br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transacao extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Transacao.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "Transacao"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("status");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "Status"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoStatus");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "CodigoStatusDescricao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoStatusDescricao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "CodigoStatus"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private boolean status;
	private String codigoStatus;
	private String codigoStatusDescricao;

	public Transacao() {
	}

	public Transacao(boolean status, String codigoStatus, String codigoStatusDescricao) {
		this.status = status;
		this.codigoStatus = codigoStatus;
		this.codigoStatusDescricao = codigoStatusDescricao;
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

	@JsonProperty("Status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@JsonProperty("CodigoStatus")
	public String getCodigoStatus() {
		return codigoStatus;
	}

	public void setCodigoStatus(String codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	@JsonProperty("CodigoStatusDescricao")
	public String getCodigoStatusDescricao() {
		return codigoStatusDescricao;
	}

	public void setCodigoStatusDescricao(String codigoStatusDescricao) {
		this.codigoStatusDescricao = codigoStatusDescricao;
	}

	@Override
	public String toString() {
		return "Transacao [status=" + status + ", codigoStatus=" + codigoStatus + ", codigoStatusDescricao=" + codigoStatusDescricao + "]";
	}


}

package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CNAE")
@XmlAccessorType(XmlAccessType.FIELD)
public class CNAE implements java.io.Serializable {

	private static final long serialVersionUID = -4562042795748058176L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CNAE.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("CNAE");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAE"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("CNAEDescricao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "CNAEDescricao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private String CNAE;
	private String CNAEDescricao;

	public CNAE() {
	}

	public CNAE(String CNAE, String CNAEDescricao) {
		this.CNAE = CNAE;
		this.CNAEDescricao = CNAEDescricao;
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

	@JsonProperty("CNAE")
	public String getCNAE() {
		return CNAE;
	}

	public void setCNAE(String CNAE) {
		this.CNAE = CNAE;
	}

	@JsonProperty("CNAEDescricao")
	public String getCNAEDescricao() {
		return CNAEDescricao;
	}

	public void setCNAEDescricao(String CNAEDescricao) {
		this.CNAEDescricao = CNAEDescricao;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((CNAE == null) ? 0 : CNAE.hashCode());
		result = prime * result + ((CNAEDescricao == null) ? 0 : CNAEDescricao.hashCode());
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
		CNAE other = (CNAE) obj;
		if (CNAE == null) {
			if (other.CNAE != null)
				return false;
		} else if (!CNAE.equals(other.CNAE))
			return false;
		if (CNAEDescricao == null) {
			if (other.CNAEDescricao != null)
				return false;
		} else if (!CNAEDescricao.equals(other.CNAEDescricao))
			return false;
		return true;
	}

}

package br.com.araujoabreu.sgi.webservices.soawebservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Telefone extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8770682272091301953L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Telefone.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Telefone"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tipoTelefone");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "TipoTelefone"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "TiposTelefones"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("numero");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Numero"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("ramal");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Ramal"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataAtualizacao");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "DataAtualizacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private int tipoTelefone;
	private String numero;
	private String ramal;
	private java.util.Calendar dataAtualizacao;

	public Telefone() {
	}

	public Telefone(int tipoTelefone,
					String numero,
					String ramal,
					java.util.Calendar dataAtualizacao) {
		this.tipoTelefone = tipoTelefone;
		this.numero = numero;
		this.ramal = ramal;
		this.dataAtualizacao = dataAtualizacao;
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
	 * Gets the tipoTelefone value for this Telefone.
	 *
	 * @return tipoTelefone
	 */
	@JsonProperty("TipoTelefone")
	public int getTipoTelefone() {
		return tipoTelefone;
	}

	/**
	 * Sets the tipoTelefone value for this Telefone.
	 *
	 * @param tipoTelefone
	 */
	public void setTipoTelefone(int tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	/**
	 * Gets the numero value for this Telefone.
	 *
	 * @return numero
	 */
	@JsonProperty("Numero")
	public String getNumero() {
		return numero;
	}

	/**
	 * Sets the numero value for this Telefone.
	 *
	 * @param numero
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * Gets the ramal value for this Telefone.
	 *
	 * @return ramal
	 */
	@JsonProperty("Ramal")
	public String getRamal() {
		return ramal;
	}

	/**
	 * Sets the ramal value for this Telefone.
	 *
	 * @param ramal
	 */
	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	/**
	 * Gets the dataAtualizacao value for this Telefone.
	 *
	 * @return dataAtualizacao
	 */
	public java.util.Calendar getDataAtualizacao() {
		return dataAtualizacao;
	}

	/**
	 * Sets the dataAtualizacao value for this Telefone.
	 *
	 * @param dataAtualizacao
	 */
	public void setDataAtualizacao(java.util.Calendar dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataAtualizacao == null) ? 0 : dataAtualizacao.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((ramal == null) ? 0 : ramal.hashCode());
		result = prime * result + tipoTelefone;
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
		Telefone other = (Telefone) obj;
		if (dataAtualizacao == null) {
			if (other.dataAtualizacao != null)
				return false;
		} else if (!dataAtualizacao.equals(other.dataAtualizacao))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (ramal == null) {
			if (other.ramal != null)
				return false;
		} else if (!ramal.equals(other.ramal))
			return false;
		if (tipoTelefone != other.tipoTelefone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Telefone [tipoTelefone=" + tipoTelefone + ", numero=" + numero + ", ramal=" + ramal + ", dataAtualizacao=" + dataAtualizacao + "]";
	}

}

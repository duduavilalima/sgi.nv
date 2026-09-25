package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "GeoLocalizacao")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoLocalizacao extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = -7164347982658763738L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(GeoLocalizacao.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "GeoLocalizacao"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("latitude");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Latitude"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("longitude");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Longitude"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("plusCodes");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "PlusCodes"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private java.math.BigDecimal latitude;
	private java.math.BigDecimal longitude;
	private String plusCodes;


	public GeoLocalizacao() {
	}

	public GeoLocalizacao(java.math.BigDecimal latitude, java.math.BigDecimal longitude, String plusCodes) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.plusCodes = plusCodes;
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

	@JsonProperty("Latitude")
	public java.math.BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(java.math.BigDecimal latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("Longitude")
	public java.math.BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(java.math.BigDecimal longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("PlusCodes")
	public String getPlusCodes() {
		return plusCodes;
	}

	public void setPlusCodes(String plusCodes) {
		this.plusCodes = plusCodes;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
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
		GeoLocalizacao other = (GeoLocalizacao) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeoLocalizacao [latitude=" + latitude + ", longitude=" + longitude + "]";
	}

}

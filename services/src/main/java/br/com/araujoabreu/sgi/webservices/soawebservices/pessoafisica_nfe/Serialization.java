package br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe;

public class Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1299293087933468156L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Serialization.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaFisicaNFe", "Serialization"));
	}

	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public Serialization() {
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

	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Serialization)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true;
		__equalsCalc = null;
		return _equals;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		__hashCodeCalc = false;
		return _hashCode;
	}

}

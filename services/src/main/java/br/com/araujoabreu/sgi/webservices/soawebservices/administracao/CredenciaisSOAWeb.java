package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class CredenciaisSOAWeb implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CredenciaisSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Credenciais"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("email");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Email"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("senha");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Senha"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private String email;
	private String senha;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public CredenciaisSOAWeb() {
	}

	public CredenciaisSOAWeb(String email, String senha) {
		this.email = email;
		this.senha = senha;
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
	 * Gets the email value for this Credenciais.
	 *
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email value for this Credenciais.
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the senha value for this Credenciais.
	 *
	 * @return senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * Sets the senha value for this Credenciais.
	 *
	 * @param senha
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	@SuppressWarnings("unused")
	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof CredenciaisSOAWeb)) {
			return false;
		}
		CredenciaisSOAWeb other = (CredenciaisSOAWeb) obj;
		if (obj == null) {
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
		_equals = true && ((this.email == null && other.getEmail() == null) || (this.email != null && this.email.equals(other.getEmail())))
			&& ((this.senha == null && other.getSenha() == null) || (this.senha != null && this.senha.equals(other.getSenha())));
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
		if (getEmail() != null) {
			_hashCode += getEmail().hashCode();
		}
		if (getSenha() != null) {
			_hashCode += getSenha().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}

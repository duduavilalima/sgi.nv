package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class HistoricoSOAWeb implements java.io.Serializable {
	private static final long serialVersionUID = -2252674839187602530L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(HistoricoSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Historico"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("uniqueID");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "UniqueID"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("valor");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Valor"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("datahora");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Datahora"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("IPAddress");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "IPAddress"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private String uniqueID;
	private String documento;
	private String valor;
	private java.util.Calendar datahora;
	private String IPAddress;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public HistoricoSOAWeb() {
	}

	public HistoricoSOAWeb(String uniqueID,
						   String documento,
						   String valor,
						   java.util.Calendar datahora,
						   String IPAddress) {
		this.uniqueID = uniqueID;
		this.documento = documento;
		this.valor = valor;
		this.datahora = datahora;
		this.IPAddress = IPAddress;
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
	 * Gets the uniqueID value for this Historico.
	 *
	 * @return uniqueID
	 */
	public String getUniqueID() {
		return uniqueID;
	}

	/**
	 * Sets the uniqueID value for this Historico.
	 *
	 * @param uniqueID
	 */
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	/**
	 * Gets the documento value for this Historico.
	 *
	 * @return documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento value for this Historico.
	 *
	 * @param documento
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Gets the valor value for this Historico.
	 *
	 * @return valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Sets the valor value for this Historico.
	 *
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Gets the datahora value for this Historico.
	 *
	 * @return datahora
	 */
	public java.util.Calendar getDatahora() {
		return datahora;
	}

	/**
	 * Sets the datahora value for this Historico.
	 *
	 * @param datahora
	 */
	public void setDatahora(java.util.Calendar datahora) {
		this.datahora = datahora;
	}

	/**
	 * Gets the IPAddress value for this Historico.
	 *
	 * @return IPAddress
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Sets the IPAddress value for this Historico.
	 *
	 * @param IPAddress
	 */
	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof HistoricoSOAWeb)) {
			return false;
		}

		HistoricoSOAWeb other = (HistoricoSOAWeb) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.uniqueID == null && other.getUniqueID() == null) || (this.uniqueID != null && this.uniqueID.equals(other.getUniqueID())))
			&& ((this.documento == null && other.getDocumento() == null) || (this.documento != null && this.documento.equals(other.getDocumento())))
			&& ((this.valor == null && other.getValor() == null) || (this.valor != null && this.valor.equals(other.getValor())))
			&& ((this.datahora == null && other.getDatahora() == null) || (this.datahora != null && this.datahora.equals(other.getDatahora())))
			&& ((this.IPAddress == null && other.getIPAddress() == null) || (this.IPAddress != null && this.IPAddress.equals(other.getIPAddress())));
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
		if (getUniqueID() != null) {
			_hashCode += getUniqueID().hashCode();
		}
		if (getDocumento() != null) {
			_hashCode += getDocumento().hashCode();
		}
		if (getValor() != null) {
			_hashCode += getValor().hashCode();
		}
		if (getDatahora() != null) {
			_hashCode += getDatahora().hashCode();
		}
		if (getIPAddress() != null) {
			_hashCode += getIPAddress().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}

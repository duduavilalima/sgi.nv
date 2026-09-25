package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class ExtratoSOAWeb extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = -6706990215427451176L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ExtratoSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Extrato"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("produtos");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Produtos"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Produto"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "Produto"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("saldo");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Saldo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
	}

	private ProdutoSOAWeb[] produtos;
	private java.math.BigDecimal saldo;
	private String mensagem;
	private boolean status;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public ExtratoSOAWeb() {
	}

	public ExtratoSOAWeb(ProdutoSOAWeb[] produtos, java.math.BigDecimal saldo, String mensagem, boolean status) {
		this.produtos = produtos;
		this.saldo = saldo;
		this.mensagem = mensagem;
		this.status = status;
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
	 * Gets the produtos value for this Extrato.
	 *
	 * @return produtos
	 */
	public ProdutoSOAWeb[] getProdutos() {
		return produtos;
	}

	/**
	 * Sets the produtos value for this Extrato.
	 *
	 * @param produtos
	 */
	public void setProdutos(ProdutoSOAWeb[] produtos) {
		this.produtos = produtos;
	}

	/**
	 * Gets the saldo value for this Extrato.
	 *
	 * @return saldo
	 */
	public java.math.BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * Sets the saldo value for this Extrato.
	 *
	 * @param saldo
	 */
	public void setSaldo(java.math.BigDecimal saldo) {
		this.saldo = saldo;
	}

	/**
	 * Gets the mensagem value for this Extrato.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this Extrato.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this Extrato.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this Extrato.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ExtratoSOAWeb)) {
			return false;
		}

		ExtratoSOAWeb other = (ExtratoSOAWeb) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj)
			&& ((this.produtos == null && other.getProdutos() == null) || (this.produtos != null && java.util.Arrays.equals(this.produtos,
			other.getProdutos())))
			&& ((this.saldo == null && other.getSaldo() == null) || (this.saldo != null && this.saldo.equals(other.getSaldo())))
			&& ((this.mensagem == null && other.getMensagem() == null) || (this.mensagem != null && this.mensagem.equals(other.getMensagem())))
			&& this.status == other.isStatus();
		__equalsCalc = null;
		return _equals;
	}

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = super.hashCode();
		if (getProdutos() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getProdutos()); i++) {
				Object obj = java.lang.reflect.Array.get(getProdutos(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getSaldo() != null) {
			_hashCode += getSaldo().hashCode();
		}
		if (getMensagem() != null) {
			_hashCode += getMensagem().hashCode();
		}
		_hashCode += (isStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

}

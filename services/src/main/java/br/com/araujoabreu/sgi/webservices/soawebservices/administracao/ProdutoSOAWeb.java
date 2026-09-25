package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class ProdutoSOAWeb extends Serialization implements java.io.Serializable {
	private static final long serialVersionUID = -5274496928703417556L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ProdutoSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Produto"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("produto");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Produto"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("mes");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Mes"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("ano");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Ano"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("quantidade");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Quantidade"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("consumo");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Consumo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private String produto;
	private String mes;
	private String ano;
	private int quantidade;
	private java.math.BigDecimal consumo;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public ProdutoSOAWeb() {
	}

	public ProdutoSOAWeb(String produto, String mes, String ano, int quantidade, java.math.BigDecimal consumo) {
		this.produto = produto;
		this.mes = mes;
		this.ano = ano;
		this.quantidade = quantidade;
		this.consumo = consumo;
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
	 * Gets the produto value for this Produto.
	 *
	 * @return produto
	 */
	public String getProduto() {
		return produto;
	}

	/**
	 * Sets the produto value for this Produto.
	 *
	 * @param produto
	 */
	public void setProduto(String produto) {
		this.produto = produto;
	}

	/**
	 * Gets the mes value for this Produto.
	 *
	 * @return mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * Sets the mes value for this Produto.
	 *
	 * @param mes
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * Gets the ano value for this Produto.
	 *
	 * @return ano
	 */
	public String getAno() {
		return ano;
	}

	/**
	 * Sets the ano value for this Produto.
	 *
	 * @param ano
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * Gets the quantidade value for this Produto.
	 *
	 * @return quantidade
	 */
	public int getQuantidade() {
		return quantidade;
	}

	/**
	 * Sets the quantidade value for this Produto.
	 *
	 * @param quantidade
	 */
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * Gets the consumo value for this Produto.
	 *
	 * @return consumo
	 */
	public java.math.BigDecimal getConsumo() {
		return consumo;
	}

	/**
	 * Sets the consumo value for this Produto.
	 *
	 * @param consumo
	 */
	public void setConsumo(java.math.BigDecimal consumo) {
		this.consumo = consumo;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ProdutoSOAWeb)) {
			return false;
		}
		ProdutoSOAWeb other = (ProdutoSOAWeb) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj)
			&& ((this.produto == null && other.getProduto() == null) || (this.produto != null && this.produto.equals(other.getProduto())))
			&& ((this.mes == null && other.getMes() == null) || (this.mes != null && this.mes.equals(other.getMes())))
			&& ((this.ano == null && other.getAno() == null) || (this.ano != null && this.ano.equals(other.getAno())))
			&& this.quantidade == other.getQuantidade()
			&& ((this.consumo == null && other.getConsumo() == null) || (this.consumo != null && this.consumo.equals(other.getConsumo())));
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
		if (getProduto() != null) {
			_hashCode += getProduto().hashCode();
		}
		if (getMes() != null) {
			_hashCode += getMes().hashCode();
		}
		if (getAno() != null) {
			_hashCode += getAno().hashCode();
		}
		_hashCode += getQuantidade();
		if (getConsumo() != null) {
			_hashCode += getConsumo().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}

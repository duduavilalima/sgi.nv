package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class HistoricoDetalhadoSOAWeb implements java.io.Serializable {

	private static final long serialVersionUID = -5344438686186965324L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(HistoricoDetalhadoSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "HistoricoDetalhado"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("produto");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Produto"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("conteudoXML");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "ConteudoXML"));
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

	private String documento;
	private String produto;
	private String conteudoXML;
	private String valor;
	private java.util.Calendar datahora;
	private String IPAddress;
	private String mensagem;
	private boolean status;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public HistoricoDetalhadoSOAWeb() {
	}

	public HistoricoDetalhadoSOAWeb(String documento,
									String produto,
									String conteudoXML,
									String valor,
									java.util.Calendar datahora,
									String IPAddress,
									String mensagem,
									boolean status) {
		this.documento = documento;
		this.produto = produto;
		this.conteudoXML = conteudoXML;
		this.valor = valor;
		this.datahora = datahora;
		this.IPAddress = IPAddress;
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
	 * Gets the documento value for this HistoricoDetalhado.
	 *
	 * @return documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento value for this HistoricoDetalhado.
	 *
	 * @param documento
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Gets the produto value for this HistoricoDetalhado.
	 *
	 * @return produto
	 */
	public String getProduto() {
		return produto;
	}

	/**
	 * Sets the produto value for this HistoricoDetalhado.
	 *
	 * @param produto
	 */
	public void setProduto(String produto) {
		this.produto = produto;
	}

	/**
	 * Gets the conteudoXML value for this HistoricoDetalhado.
	 *
	 * @return conteudoXML
	 */
	public String getConteudoXML() {
		return conteudoXML;
	}

	/**
	 * Sets the conteudoXML value for this HistoricoDetalhado.
	 *
	 * @param conteudoXML
	 */
	public void setConteudoXML(String conteudoXML) {
		this.conteudoXML = conteudoXML;
	}

	/**
	 * Gets the valor value for this HistoricoDetalhado.
	 *
	 * @return valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Sets the valor value for this HistoricoDetalhado.
	 *
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Gets the datahora value for this HistoricoDetalhado.
	 *
	 * @return datahora
	 */
	public java.util.Calendar getDatahora() {
		return datahora;
	}

	/**
	 * Sets the datahora value for this HistoricoDetalhado.
	 *
	 * @param datahora
	 */
	public void setDatahora(java.util.Calendar datahora) {
		this.datahora = datahora;
	}

	/**
	 * Gets the IPAddress value for this HistoricoDetalhado.
	 *
	 * @return IPAddress
	 */
	public String getIPAddress() {
		return IPAddress;
	}

	/**
	 * Sets the IPAddress value for this HistoricoDetalhado.
	 *
	 * @param IPAddress
	 */
	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}

	/**
	 * Gets the mensagem value for this HistoricoDetalhado.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this HistoricoDetalhado.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this HistoricoDetalhado.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this HistoricoDetalhado.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof HistoricoDetalhadoSOAWeb)) {
			return false;
		}

		HistoricoDetalhadoSOAWeb other = (HistoricoDetalhadoSOAWeb) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
			&& ((this.documento == null && other.getDocumento() == null) || (this.documento != null && this.documento.equals(other.getDocumento())))
			&& ((this.produto == null && other.getProduto() == null) || (this.produto != null && this.produto.equals(other.getProduto())))
			&& ((this.conteudoXML == null && other.getConteudoXML() == null) || (this.conteudoXML != null && this.conteudoXML
			.equals(other.getConteudoXML())))
			&& ((this.valor == null && other.getValor() == null) || (this.valor != null && this.valor.equals(other.getValor())))
			&& ((this.datahora == null && other.getDatahora() == null) || (this.datahora != null && this.datahora.equals(other.getDatahora())))
			&& ((this.IPAddress == null && other.getIPAddress() == null) || (this.IPAddress != null && this.IPAddress.equals(other.getIPAddress())))
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
		int _hashCode = 1;
		if (getDocumento() != null) {
			_hashCode += getDocumento().hashCode();
		}
		if (getProduto() != null) {
			_hashCode += getProduto().hashCode();
		}
		if (getConteudoXML() != null) {
			_hashCode += getConteudoXML().hashCode();
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
		if (getMensagem() != null) {
			_hashCode += getMensagem().hashCode();
		}
		_hashCode += (isStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

}

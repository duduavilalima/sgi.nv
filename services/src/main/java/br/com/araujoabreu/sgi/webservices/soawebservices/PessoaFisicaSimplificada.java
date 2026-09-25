package br.com.araujoabreu.sgi.webservices.soawebservices;

public class PessoaFisicaSimplificada extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5903293393395545266L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PessoaFisicaSimplificada.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "PessoaFisicaSimplificada"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nome");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Nome"));
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
	private String nome;
	private String mensagem;
	private boolean status;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public PessoaFisicaSimplificada() {
	}

	public PessoaFisicaSimplificada(String documento, String nome, String mensagem, boolean status) {
		this.documento = documento;
		this.nome = nome;
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
	 * Gets the documento value for this PessoaFisicaSimplificada.
	 *
	 * @return documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento value for this PessoaFisicaSimplificada.
	 *
	 * @param documento
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Gets the nome value for this PessoaFisicaSimplificada.
	 *
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the nome value for this PessoaFisicaSimplificada.
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the mensagem value for this PessoaFisicaSimplificada.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this PessoaFisicaSimplificada.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this PessoaFisicaSimplificada.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this PessoaFisicaSimplificada.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof PessoaFisicaSimplificada)) {
			return false;
		}
		PessoaFisicaSimplificada other = (PessoaFisicaSimplificada) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj)
			&& ((this.documento == null && other.getDocumento() == null) || (this.documento != null && this.documento.equals(other.getDocumento())))
			&& ((this.nome == null && other.getNome() == null) || (this.nome != null && this.nome.equals(other.getNome())))
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
		if (getDocumento() != null) {
			_hashCode += getDocumento().hashCode();
		}
		if (getNome() != null) {
			_hashCode += getNome().hashCode();
		}
		if (getMensagem() != null) {
			_hashCode += getMensagem().hashCode();
		}
		_hashCode += (isStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	@Override
	public String toString() {
		return "PessoaFisicaSimplificada [documento=" + documento + ", nome=" + nome + ", mensagem=" + mensagem + ", status=" + status + "]";
	}

}

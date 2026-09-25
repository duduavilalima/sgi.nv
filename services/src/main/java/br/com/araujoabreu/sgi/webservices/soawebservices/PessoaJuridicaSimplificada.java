package br.com.araujoabreu.sgi.webservices.soawebservices;

public class PessoaJuridicaSimplificada extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2552739764036361351L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(PessoaJuridicaSimplificada.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "PessoaJuridicaSimplificada"));
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

	public PessoaJuridicaSimplificada() {
	}

	public PessoaJuridicaSimplificada(String documento, String nome, String mensagem, boolean status) {
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
	 * Gets the documento value for this PessoaJuridicaSimplificada.
	 *
	 * @return documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * Sets the documento value for this PessoaJuridicaSimplificada.
	 *
	 * @param documento
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * Gets the nome value for this PessoaJuridicaSimplificada.
	 *
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the nome value for this PessoaJuridicaSimplificada.
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the mensagem value for this PessoaJuridicaSimplificada.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this PessoaJuridicaSimplificada.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this PessoaJuridicaSimplificada.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this PessoaJuridicaSimplificada.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + (status ? 1231 : 1237);
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
		PessoaJuridicaSimplificada other = (PessoaJuridicaSimplificada) obj;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PessoaJuridicaSimplificada [documento=" + documento + ", nome=" + nome + ", mensagem=" + mensagem + ", status=" + status + "]";
	}

}

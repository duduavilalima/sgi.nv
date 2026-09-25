package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class ExtratoAnalitico extends Serialization implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3659485212695572742L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ExtratoAnalitico.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "ExtratoAnalitico"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("historicos");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Historicos"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Historico"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("SOAWebServices", "Historico"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("totalConsultas");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "TotalConsultas"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("valorTotal");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "ValorTotal"));
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

	private HistoricoSOAWeb[] historicos;
	private int totalConsultas;
	private String valorTotal;
	private String mensagem;
	private boolean status;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public ExtratoAnalitico() {
	}

	public ExtratoAnalitico(HistoricoSOAWeb[] historicos, int totalConsultas, String valorTotal, String mensagem, boolean status) {
		this.historicos = historicos;
		this.totalConsultas = totalConsultas;
		this.valorTotal = valorTotal;
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
	 * Gets the historicos value for this ExtratoAnalitico.
	 *
	 * @return historicos
	 */
	public HistoricoSOAWeb[] getHistoricos() {
		return historicos;
	}

	/**
	 * Sets the historicos value for this ExtratoAnalitico.
	 *
	 * @param historicos
	 */
	public void setHistoricos(HistoricoSOAWeb[] historicos) {
		this.historicos = historicos;
	}

	/**
	 * Gets the totalConsultas value for this ExtratoAnalitico.
	 *
	 * @return totalConsultas
	 */
	public int getTotalConsultas() {
		return totalConsultas;
	}

	/**
	 * Sets the totalConsultas value for this ExtratoAnalitico.
	 *
	 * @param totalConsultas
	 */
	public void setTotalConsultas(int totalConsultas) {
		this.totalConsultas = totalConsultas;
	}

	/**
	 * Gets the valorTotal value for this ExtratoAnalitico.
	 *
	 * @return valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * Sets the valorTotal value for this ExtratoAnalitico.
	 *
	 * @param valorTotal
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * Gets the mensagem value for this ExtratoAnalitico.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this ExtratoAnalitico.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this ExtratoAnalitico.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this ExtratoAnalitico.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ExtratoAnalitico)) {
			return false;
		}
		ExtratoAnalitico other = (ExtratoAnalitico) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj)
			&& ((this.historicos == null && other.getHistoricos() == null) || (this.historicos != null && java.util.Arrays.equals(this.historicos,
			other.getHistoricos()))) && this.totalConsultas == other.getTotalConsultas()
			&& ((this.valorTotal == null && other.getValorTotal() == null) || (this.valorTotal != null && this.valorTotal.equals(other.getValorTotal())))
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
		if (getHistoricos() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getHistoricos()); i++) {
				Object obj = java.lang.reflect.Array.get(getHistoricos(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		_hashCode += getTotalConsultas();
		if (getValorTotal() != null) {
			_hashCode += getValorTotal().hashCode();
		}
		if (getMensagem() != null) {
			_hashCode += getMensagem().hashCode();
		}
		_hashCode += (isStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

}

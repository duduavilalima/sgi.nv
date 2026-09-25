package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.TransacaoSaldo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "SaldoResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaldoSOAWeb extends Serialization implements java.io.Serializable {
	private static final long serialVersionUID = 797272650631797013L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(SaldoSOAWeb.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Saldo"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("transacao");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Transacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "Transacao"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	private BigDecimal saldo;
	private String mensagem;
	private boolean status;
	private TransacaoSaldo transacao;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;

	public SaldoSOAWeb() {
	}

	public SaldoSOAWeb(BigDecimal saldo, String mensagem, boolean status, TransacaoSaldo transacao) {
		super();
		this.saldo = saldo;
		this.mensagem = mensagem;
		this.status = status;
		this.transacao = transacao;
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
	 * Gets the saldo value for this Saldo.
	 *
	 * @return saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * Sets the saldo value for this Saldo.
	 *
	 * @param saldo
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	/**
	 * Gets the mensagem value for this Saldo.
	 *
	 * @return mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Sets the mensagem value for this Saldo.
	 *
	 * @param mensagem
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Gets the status value for this Saldo.
	 *
	 * @return status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Sets the status value for this Saldo.
	 *
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	public TransacaoSaldo getTransacao() {
		return transacao;
	}

	public void setTransacao(TransacaoSaldo transacao) {
		this.transacao = transacao;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof SaldoSOAWeb)) {
			return false;
		}
		SaldoSOAWeb other = (SaldoSOAWeb) obj;
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = super.equals(obj) && ((this.saldo == null && other.getSaldo() == null) || (this.saldo != null && this.saldo.equals(other.getSaldo())))
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

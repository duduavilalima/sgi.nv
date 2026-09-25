package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "QSA")
@XmlAccessorType(XmlAccessType.FIELD)
public class QSA extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = -3315160722181557173L;
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(QSA.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "QSA"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("mensagens");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Mensagens"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "string"));
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("socios");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Socios"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Socio"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Socio"));
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("administradores");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Administradores"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Administrador"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setItemQName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Administrador"));
		typeDesc.addFieldDesc(elemField);

	}

	private Socio[] socios;
	private String[] mensagens;
	private Administrador[] administradores;

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

	public Socio[] getSocios() {
		return socios;
	}

	public void setSocios(Socio[] socios) {
		this.socios = socios;
	}

	public String[] getMensagens() {
		return mensagens;
	}

	public void setMensagens(String[] mensagens) {
		this.mensagens = mensagens;
	}

	public Administrador[] getAdministradores() {
		return administradores;
	}

	public void setAdministradores(Administrador[] administradores) {
		this.administradores = administradores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(socios);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QSA other = (QSA) obj;
		if (!Arrays.equals(socios, other.socios))
			return false;
		return true;
	}


}

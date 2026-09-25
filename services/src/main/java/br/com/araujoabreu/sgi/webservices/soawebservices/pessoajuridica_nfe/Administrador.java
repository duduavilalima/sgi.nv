package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Administradores")
@XmlAccessorType(XmlAccessType.FIELD)
public class Administrador extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = 8716423928778621218L;
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Administrador.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Administrador"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("pessoa");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Pessoa"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "TipoAdministrador"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("documento");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Documento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("nome");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Nome"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cargo");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Cargo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

	}

	private TipoAdministrador pessoa;
	private String documento;
	private String nome;
	private String cargo;


	public Administrador() {
	}

	public Administrador(TipoAdministrador pessoa, String documento, String nome, String cargo) {
		super();
		this.pessoa = pessoa;
		this.documento = documento;
		this.nome = nome;
		this.cargo = cargo;
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

	public TipoAdministrador getPessoa() {
		return pessoa;
	}

	public void setPessoa(TipoAdministrador pessoa) {
		this.pessoa = pessoa;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
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
		Administrador other = (Administrador) obj;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
		return true;
	}


}

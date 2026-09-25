package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Socios")
@XmlAccessorType(XmlAccessType.FIELD)
public class Socio extends Serialization implements java.io.Serializable {

	private static final long serialVersionUID = -3537246221684808451L;
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Socio.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Socio"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("pessoa");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Pessoa"));
		elemField.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "TipoSocio"));
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
		elemField.setFieldName("participacao");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Participacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cargo");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "Cargo"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);

		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataEntrada");
		elemField.setXmlName(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "DataEntrada"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);


	}

	private TipoSocio pessoa;
	private String documento;
	private String nome;
	private String participacao;
	private String cargo;
	private String dataEntrada;

	public Socio() {
	}

	public Socio(TipoSocio pessoa, String documento, String nome, String participacao, String cargo, String dataEntrada) {
		this.pessoa = pessoa;
		this.documento = documento;
		this.nome = nome;
		this.participacao = participacao;
		this.cargo = cargo;
		this.dataEntrada = dataEntrada;
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

	public String getSocio() {
		return nome;
	}

	public void setSocio(String socio) {
		this.nome = socio;
	}

	public TipoSocio getPessoa() {
		return pessoa;
	}

	public void setPessoa(TipoSocio pessoa) {
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

	public String getParticipacao() {
		return participacao;
	}

	public void setParticipacao(String participacao) {
		this.participacao = participacao;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Socio other = (Socio) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}

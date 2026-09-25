package br.com.araujoabreu.sgi.webservices.soawebservices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnderecoNfe implements java.io.Serializable {


	private static final long serialVersionUID = 4368185033189081154L;
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(EnderecoNfe.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "EnderecoNfe"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("tipo");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Tipo"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "TiposEnderecos"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("logradouro");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Logradouro"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("numero");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Numero"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("complemento");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Complemento"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("bairro");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Bairro"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("cidade");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Cidade"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("estado");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "Estado"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("CEP");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "CEP"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("geoLocalizacao");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "GeoLocalizacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "GeoLocalizacao"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataAtualizacao");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "DataAtualizacao"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("codigoIBGE");
		elemField.setXmlName(new javax.xml.namespace.QName("SOAWebServices", "CodigoIBGE"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	@JsonProperty("Tipo")
	private TiposEnderecos tipo;
	@JsonProperty("Logradouro")
	private String logradouro;
	@JsonProperty("Numero")
	private String numero;
	@JsonProperty("Complemento")
	private String complemento;
	@JsonProperty("Bairro")
	private String bairro;
	@JsonProperty("Cidade")
	private String cidade;
	@JsonProperty("Estado")
	private String estado;
	@JsonProperty("CEP")
	private String CEP;
	@JsonProperty("GeoLocalizacao")
	private GeoLocalizacao geoLocalizacao;
	@JsonProperty("DataAtualizacao")
	private java.util.Calendar dataAtualizacao;
	@JsonProperty("CodigoIBGE")
	private int codigoIBGE;

	public EnderecoNfe() {
	}

	public EnderecoNfe(TiposEnderecos tipo,
					   String logradouro,
					   String numero,
					   String complemento,
					   String bairro,
					   String cidade,
					   String estado,
					   String CEP,
					   GeoLocalizacao geoLocalizacao,
					   java.util.Calendar dataAtualizacao,
					   int codigoIBGE) {
		this.tipo = tipo;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.CEP = CEP;
		this.geoLocalizacao = geoLocalizacao;
		this.dataAtualizacao = dataAtualizacao;
		this.codigoIBGE = codigoIBGE;
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

	public TiposEnderecos getTipo() {
		return tipo;
	}

	public void setTipo(TiposEnderecos tipo) {
		this.tipo = tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	public GeoLocalizacao getGeoLocalizacao() {
		return geoLocalizacao;
	}

	public void setGeoLocalizacao(GeoLocalizacao geoLocalizacao) {
		this.geoLocalizacao = geoLocalizacao;
	}

	public java.util.Calendar getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(java.util.Calendar dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public int getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(int codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((CEP == null) ? 0 : CEP.hashCode());
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + codigoIBGE;
		result = prime * result + ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((dataAtualizacao == null) ? 0 : dataAtualizacao.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((geoLocalizacao == null) ? 0 : geoLocalizacao.hashCode());
		result = prime * result + ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		EnderecoNfe other = (EnderecoNfe) obj;
		if (CEP == null) {
			if (other.CEP != null)
				return false;
		} else if (!CEP.equals(other.CEP))
			return false;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (codigoIBGE != other.codigoIBGE)
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (dataAtualizacao == null) {
			if (other.dataAtualizacao != null)
				return false;
		} else if (!dataAtualizacao.equals(other.dataAtualizacao))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (geoLocalizacao == null) {
			if (other.geoLocalizacao != null)
				return false;
		} else if (!geoLocalizacao.equals(other.geoLocalizacao))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EnderecoNfe [tipo=" + tipo + ", logradouro=" + logradouro + ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro
			+ ", cidade=" + cidade + ", estado=" + estado + ", CEP=" + CEP + ", geoLocalizacao=" + geoLocalizacao + ", dataAtualizacao=" + dataAtualizacao
			+ ", codigoIBGE=" + codigoIBGE + "]";
	}

}

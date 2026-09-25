package br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe;

public class TipoSocio implements java.io.Serializable {

	public static final String _NaoInformado = "NaoInformado";
	public static final String _Fisica = "Fisica";
	public static final String _Juridica = "Juridica";
	public static final TipoSocio NaoInformado = new TipoSocio(_NaoInformado);
	public static final TipoSocio Residencial = new TipoSocio(_Fisica);
	public static final TipoSocio Comercial = new TipoSocio(_Juridica);
	private static final long serialVersionUID = -6565583130993197015L;
	@SuppressWarnings("rawtypes")
	private static final java.util.HashMap _table_ = new java.util.HashMap();
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TipoSocio.class);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PessoaJuridicaNFe", "TipoSocio"));
	}

	private String tipo;


	public TipoSocio() {
		super();
	}

	// Constructor
	@SuppressWarnings("unchecked")
	protected TipoSocio(String value) {
		tipo = value;
		_table_.put(tipo, this);
	}

	public static TipoSocio fromValue(String value) throws IllegalArgumentException {
		TipoSocio enumeration = (TipoSocio) _table_.get(value);
		if (enumeration == null) {
			throw new IllegalArgumentException();
		}
		return enumeration;
	}

	public static TipoSocio fromString(String value) throws IllegalArgumentException {
		return fromValue(value);
	}

	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(String mechType, Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.EnumSerializer(_javaType, _xmlType);
	}

	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(String mechType, Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.EnumDeserializer(_javaType, _xmlType);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	public String getValue() {
		return tipo;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return tipo;
	}

	public Object readResolve() {
		return fromValue(tipo);
	}

}

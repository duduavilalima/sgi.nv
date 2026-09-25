package br.com.araujoabreu.sgi.webservices.soawebservices;

public class TiposTelefones implements java.io.Serializable {
	public static final String _NaoInformado = "NaoInformado";
	public static final String _Residencial = "Residencial";
	public static final String _Comercial = "Comercial";
	public static final String _Celular = "Celular";
	public static final String _Fax = "Fax";
	public static final TiposTelefones NaoInformado = new TiposTelefones(_NaoInformado);
	public static final TiposTelefones Residencial = new TiposTelefones(_Residencial);
	public static final TiposTelefones Comercial = new TiposTelefones(_Comercial);
	public static final TiposTelefones Celular = new TiposTelefones(_Celular);
	public static final TiposTelefones Fax = new TiposTelefones(_Fax);
	/**
	 *
	 */
	private static final long serialVersionUID = -3205790065526430306L;
	@SuppressWarnings("rawtypes")
	private static final java.util.HashMap _table_ = new java.util.HashMap();
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TiposTelefones.class);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "TiposTelefones"));
	}

	private String tipo;

	public TiposTelefones() {
		super();
	}

	// Constructor
	@SuppressWarnings("unchecked")
	protected TiposTelefones(String value) {
		tipo = value;
		_table_.put(tipo, this);
	}

	public static TiposTelefones fromValue(String value) throws IllegalArgumentException {
		TiposTelefones enumeration = (TiposTelefones) _table_.get(value);
		if (enumeration == null) {
			throw new IllegalArgumentException();
		}
		return enumeration;
	}

	public static TiposTelefones fromString(String value) throws IllegalArgumentException {
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

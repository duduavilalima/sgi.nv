package br.com.araujoabreu.sgi.webservices.soawebservices;

public class TiposEnderecos implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5499925015111705420L;
	@SuppressWarnings("rawtypes")
	private static final java.util.HashMap _table_ = new java.util.HashMap();
	// Type metadata
	private static final org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TiposEnderecos.class);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("SOAWebServices", "TiposEnderecos"));
	}

	private String tipo;

	public TiposEnderecos() {
		super();
	}

	// Constructor
	@SuppressWarnings("unchecked")
	protected TiposEnderecos(String value) {
		tipo = value;
		_table_.put(tipo, this);
	}

	protected TiposEnderecos(Integer value) {
		tipo = value.toString();
		_table_.put(tipo, this);
	}

	public static TiposEnderecos fromValue(String value) throws IllegalArgumentException {
		TiposEnderecos enumeration = (TiposEnderecos) _table_.get(value);
		if (enumeration == null) {
			throw new IllegalArgumentException();
		}
		return enumeration;
	}

	public static TiposEnderecos fromString(String value) throws IllegalArgumentException {
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

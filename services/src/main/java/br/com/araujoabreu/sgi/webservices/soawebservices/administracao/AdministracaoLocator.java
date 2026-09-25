package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class AdministracaoLocator extends org.apache.axis.client.Service implements AdministracaoSOAWeb {

	private static final long serialVersionUID = -6218807102392799122L;
	// Use to get a proxy class for AdministracaoSoap
	private String AdministracaoSoap_address = "http://www.com.br/webservices/producao/sws/administracao.asmx";
	// The WSDD service name defaults to the port name.
	private String AdministracaoSoapWSDDServiceName = "AdministracaoSoap";
	@SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

	public AdministracaoLocator() {
	}

	public AdministracaoLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public AdministracaoLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	@Override
	public String getAdministracaoSoapAddress() {
		return AdministracaoSoap_address;
	}

	public String getAdministracaoSoapWSDDServiceName() {
		return AdministracaoSoapWSDDServiceName;
	}

	public void setAdministracaoSoapWSDDServiceName(String name) {
		AdministracaoSoapWSDDServiceName = name;
	}

	@Override
	public AdministracaoSoap getAdministracaoSoap() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(AdministracaoSoap_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getAdministracaoSoap(endpoint);
	}

	@Override
	public AdministracaoSoap getAdministracaoSoap(java.net.URL portAddress) {
		try {
			AdministracaoSoapStub _stub = new AdministracaoSoapStub(portAddress, this);
			_stub.setPortName(getAdministracaoSoapWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setAdministracaoSoapEndpointAddress(String address) {
		AdministracaoSoap_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (AdministracaoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				AdministracaoSoapStub _stub = new AdministracaoSoapStub(new java.net.URL(AdministracaoSoap_address), this);
				_stub.setPortName(getAdministracaoSoapWSDDServiceName());
				return _stub;
			}
		} catch (Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
			+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		String inputPortName = portName.getLocalPart();
		if ("AdministracaoSoap".equals(inputPortName)) {
			return getAdministracaoSoap();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	@Override
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("SOAWebServices", "Administracao");
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("SOAWebServices", "AdministracaoSoap"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

		if ("AdministracaoSoap".equals(portName)) {
			setAdministracaoSoapEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}

package br.com.araujoabreu.sgi.webservices.soawebservices;

import java.rmi.Remote;

public class CDCLocator extends org.apache.axis.client.Service implements CDC {

	/**
	 *
	 */
	private static final long serialVersionUID = -6381815366626178196L;
	// Use to get a proxy class for CDCSoap
	private String CDCSoap_address = "http://www.soawebservices.com.br/webservices/producao/cdc/cdc.asmx";
	// The WSDD service name defaults to the port name.
	private String CDCSoapWSDDServiceName = "CDCSoap";
	@SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

	/**
	 * <h3>SOA WebServices - CDC</h3>
	 */

	public CDCLocator() {
	}

	public CDCLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public CDCLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	@Override
	public String getCDCSoapAddress() {
		return CDCSoap_address;
	}

	public String getCDCSoapWSDDServiceName() {
		return CDCSoapWSDDServiceName;
	}

	public void setCDCSoapWSDDServiceName(String name) {
		CDCSoapWSDDServiceName = name;
	}

	@Override
	public CDCSoap getCDCSoap() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(CDCSoap_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getCDCSoap(endpoint);
	}

	@Override
	public CDCSoap getCDCSoap(java.net.URL portAddress) {
		try {
			CDCSoapStub _stub = new CDCSoapStub(
				portAddress, this);
			_stub.setPortName(getCDCSoapWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setCDCSoapEndpointAddress(String address) {
		CDCSoap_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (CDCSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				CDCSoapStub _stub = new CDCSoapStub(
					new java.net.URL(CDCSoap_address), this);
				_stub.setPortName(getCDCSoapWSDDServiceName());
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
	@SuppressWarnings("rawtypes")
	@Override
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		String inputPortName = portName.getLocalPart();
		if ("CDCSoap".equals(inputPortName)) {
			return getCDCSoap();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	@Override
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("SOAWebServices", "CDC");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("SOAWebServices", "CDCSoap"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

		if ("CDCSoap".equals(portName)) {
			setCDCSoapEndpointAddress(address);
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

package br.com.araujoabreu.sgi.webservices.soawebservices;

public interface CDC extends javax.xml.rpc.Service {

	/**
	 * <h3>SOA WebServices - CDC</h3>
	 */
	String getCDCSoapAddress();

	CDCSoap getCDCSoap() throws javax.xml.rpc.ServiceException;

	CDCSoap getCDCSoap(java.net.URL portAddress);
}

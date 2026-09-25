package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public interface AdministracaoSOAWeb extends javax.xml.rpc.Service {

	String getAdministracaoSoapAddress();

	AdministracaoSoap getAdministracaoSoap() throws javax.xml.rpc.ServiceException;

	AdministracaoSoap getAdministracaoSoap(java.net.URL portAddress);
}

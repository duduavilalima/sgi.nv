package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public class AdministracaoSoapProxy implements AdministracaoSoap {
	private String _endpoint = null;
	private AdministracaoSoap administracaoSoap = null;

	public AdministracaoSoapProxy() {
		_initAdministracaoSoapProxy();
	}

	public AdministracaoSoapProxy(String endpoint) {
		_endpoint = endpoint;
		_initAdministracaoSoapProxy();
	}

	private void _initAdministracaoSoapProxy() {
		try {
			administracaoSoap = (new AdministracaoLocator()).getAdministracaoSoap();
			if (administracaoSoap != null) {
				if (_endpoint != null) {
					((javax.xml.rpc.Stub) administracaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				} else {
					_endpoint = (String) ((javax.xml.rpc.Stub) administracaoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
				}
			}

		} catch (javax.xml.rpc.ServiceException ignored) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (administracaoSoap != null) {
			((javax.xml.rpc.Stub) administracaoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
		}

	}

	public AdministracaoSoap getAdministracaoSoap() {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap;
	}

	@Override
	public SaldoSOAWeb saldo(CredenciaisSOAWeb credenciais) throws java.rmi.RemoteException {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap.saldo(credenciais);
	}

	@Override
	public ExtratoSOAWeb extrato(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap.extrato(credenciais, mes, ano);
	}

	@Override
	public ExtratoAnalitico extratoAnalitico(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap.extratoAnalitico(credenciais, mes, ano);
	}

	@Override
	public ExtratoAnalitico extratoAnaliticoPorDocumento(CredenciaisSOAWeb credenciais, String documento) throws java.rmi.RemoteException {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap.extratoAnaliticoPorDocumento(credenciais, documento);
	}

	@Override
	public HistoricoDetalhadoSOAWeb historicoDetalhado(CredenciaisSOAWeb credenciais, String uniqueID) throws java.rmi.RemoteException {
		if (administracaoSoap == null) {
			_initAdministracaoSoapProxy();
		}
		return administracaoSoap.historicoDetalhado(credenciais, uniqueID);
	}

}

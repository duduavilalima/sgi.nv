package br.com.araujoabreu.sgi.webservices.soawebservices;

import br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.PessoaFisicaNFe;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.PessoaJuridicaNFe;

public class CDCSoapProxy implements CDCSoap {
	private String _endpoint = null;
	private CDCSoap cDCSoap = null;

	public CDCSoapProxy() {
		_initCDCSoapProxy();
	}

	public CDCSoapProxy(String endpoint) {
		_endpoint = endpoint;
		_initCDCSoapProxy();
	}

	private void _initCDCSoapProxy() {
		try {
			cDCSoap = (new CDCLocator()).getCDCSoap();
			if (cDCSoap != null) {
				if (_endpoint != null) {
					((javax.xml.rpc.Stub) cDCSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				} else {
					_endpoint = (String) ((javax.xml.rpc.Stub) cDCSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
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
		if (cDCSoap != null) {
			((javax.xml.rpc.Stub) cDCSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
		}

	}

	public CDCSoap getCDCSoap() {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap;
	}

	@Override
	public PessoaFisicaEstendida pessoaFisicaEstendida(
		Credenciais credenciais, String documento) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaFisicaEstendida(credenciais, documento);
	}

	@Override
	public PessoaFisicaSimplificada pessoaFisicaSimplificada(
		Credenciais credenciais, String documento) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaFisicaSimplificada(credenciais, documento);
	}

	@Override
	public PessoaJuridicaSimplificada pessoaJuridicaSimplificada(
		Credenciais credenciais, String documento) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaJuridicaSimplificada(credenciais, documento);
	}

	@Override
	public PessoaJuridicaNFe pessoaJuridicaNFe(
		Credenciais credenciais, String documento) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaJuridicaNFe(credenciais, documento);
	}

	@Override
	public PessoaJuridicaNFe pessoaJuridicaNFe(
		Credenciais credenciais) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaJuridicaNFe(credenciais, credenciais.getDocumento());
	}

	@Override
	public PessoaFisicaNFe pessoaFisicaNFe(
		Credenciais credenciais, String documento) throws java.rmi.RemoteException {
		if (cDCSoap == null) {
			_initCDCSoapProxy();
		}
		return cDCSoap.pessoaFisicaNFe(credenciais, documento);
	}

}

package br.com.araujoabreu.sgi.webservices.soawebservices.administracao;

public interface AdministracaoSoap extends java.rmi.Remote {

	/**
	 * Consulta Saldo de sua Conta<br /><br />
	 */
	SaldoSOAWeb saldo(CredenciaisSOAWeb credenciais) throws java.rmi.RemoteException;

	/**
	 * Consulta Extrato de sua Conta<br /><br />
	 */
	ExtratoSOAWeb extrato(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException;

	/**
	 * Consulta Historico Analitico de sua Conta<br /><br />
	 */
	ExtratoAnalitico extratoAnalitico(CredenciaisSOAWeb credenciais, String mes, String ano) throws java.rmi.RemoteException;

	/**
	 * Consulta Historico de um Documento<br /><br />
	 */
	ExtratoAnalitico extratoAnaliticoPorDocumento(CredenciaisSOAWeb credenciais, String documento) throws java.rmi.RemoteException;

	/**
	 * Consulta um Historico<br /><br />
	 */
	HistoricoDetalhadoSOAWeb historicoDetalhado(CredenciaisSOAWeb credenciais, String uniqueID) throws java.rmi.RemoteException;
}

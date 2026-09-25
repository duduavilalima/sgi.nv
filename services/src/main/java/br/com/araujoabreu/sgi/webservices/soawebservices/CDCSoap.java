package br.com.araujoabreu.sgi.webservices.soawebservices;

import br.com.araujoabreu.sgi.webservices.soawebservices.pessoafisica_nfe.PessoaFisicaNFe;
import br.com.araujoabreu.sgi.webservices.soawebservices.pessoajuridica_nfe.PessoaJuridicaNFe;

import java.rmi.RemoteException;

public interface CDCSoap extends java.rmi.Remote {

	/**
	 * Consulta CDC Pessoa Fisica Estendida<br /><br />
	 */
	PessoaFisicaEstendida pessoaFisicaEstendida(Credenciais credenciais, String documento) throws RemoteException;

	/**
	 * Consulta CDC Pessoa Fisica Simplificada<br /><br />
	 */
	PessoaFisicaSimplificada pessoaFisicaSimplificada(Credenciais credenciais, String documento) throws RemoteException;

	/**
	 * Consulta CDC Pessoa Juridica Estendida<br /><br />
	 */
	PessoaJuridicaSimplificada pessoaJuridicaSimplificada(Credenciais credenciais, String documento) throws RemoteException;

	/**
	 * Confirmacao de Dados Cadastrais Pessoa Juridica - NFe<br /><br
	 * />
	 */
	PessoaJuridicaNFe pessoaJuridicaNFe(Credenciais credenciais, String documento) throws RemoteException;

	/**
	 * Confirmacao de Dados Cadastrais Pessoa Fisica - NFe<br /><br
	 * />
	 */
	PessoaFisicaNFe pessoaFisicaNFe(Credenciais credenciais, String documento) throws RemoteException;

	PessoaJuridicaNFe pessoaJuridicaNFe(Credenciais credenciais) throws RemoteException;
}

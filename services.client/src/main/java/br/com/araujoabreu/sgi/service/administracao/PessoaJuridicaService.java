package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;
import com.querydsl.jpa.impl.JPAQuery;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.util.List;

public interface PessoaJuridicaService extends CrudService<PessoaJuridicaTO> {
	PessoaJuridicaPesquisaTO getPessoaPopUp(Long id, String tipo);

	PessoaJuridicaTO findPessoaByCnpj(String documento) throws Exception;

	List<PessoaJuridicaPesquisaTO> getPessoaJuridicaCliente(String nome);

	String getDocumentoFormatado(PessoaJuridicaTO object);

	void consultarCNPJ(PessoaTO object);

	PessoaJuridicaTO consultarCnpj(PessoaJuridicaTO pessoaJuridica, PessoaTO pessoaTO, boolean converterFornecedor) throws RemoteException, JAXBException, ValidationException;

	PessoaJuridicaTO obterPessoaJuridica(Long codigo);

	ValueTO verificarCnpjClientePopup(PessoaJuridicaTO map);

	JPAQuery jpaQueryPessoaJuridica();

	String obterPessoaFisicaApenasNomeFantasia(Long codigo);

	List<PessoaJuridicaTO> buscarListaPorNomeFantasia(String nome);

	PessoaJuridicaPesquisaTO getPessoaPopUp(Long id, String tipo, Long codigoCL);

	String obterInscricaoEstadual(Long codigo);
}

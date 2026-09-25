package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaFisicaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;
import com.querydsl.jpa.impl.JPAQuery;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.util.List;

public interface PessoaFisicaService extends CrudService<PessoaFisicaTO> {
	PessoaFisicaTO consultarCpf(PessoaFisicaTO pessoaFisicaTO, PessoaTO pessoaTO, boolean converterFornecedor) throws RemoteException, JAXBException, ValidationException;

	String removerAcento(String str);

	List<PessoaFisicaTO> buscarPessoasFisicas(PessoaFisicaTO pessoaFisicaTO);

	List<PessoaFisicaTO> buscarListaPessoaFisica(PessoaFisicaTO pessoaFisicaTO);

	String nomeUsuarioPorLogin(String login);

	String getDocumentoFormatadoCPF(PessoaFisicaTO object);

	void consultarCPF(PessoaTO object);

	List<PessoaFisicaTO> findByUsuarioLogin(String login);

	String nomeDoCriador(String login);

	JPAQuery jpaQueryPessoaFisica();
}

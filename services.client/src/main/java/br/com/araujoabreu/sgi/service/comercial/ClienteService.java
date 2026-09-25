package br.com.araujoabreu.sgi.service.comercial;

import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClienteSimpleTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.ClienteTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.contato.transfer.RelacionamentoContatoTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoSimpleTO;
import br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer.PessoaContaContabilTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.lang.ValidationException;

import java.util.List;

public interface ClienteService extends CrudService<ClienteTO> {
	List<ContratoSimpleTO> buscarContratos(String codigoCliente);

	List<ClienteSimpleTO> buscarSubclientes(String codigoCliente);

	List<PessoaContaContabilTO> buscarDadosContabeis(String codigoCliente);

	List<TelefoneTO> buscarTelefones(String codigoCliente);

	List<ContatoTO> buscarContatos(String codigoCliente);

	void removerCliente(Long id) throws Exception;

	List<ClassificacaoClienteTO> buscarClassificacaoCliente();

	List<RelacionamentoContatoTO> buscarRelacionamento();

	List<TipoLogradouroTO> buscarTipoLogradouro();

	ValueTO consultarDocumentoPopup(ClienteTO clienteTO);

	Object consultarDocumento(ClienteTO clienteTO) throws ValidationException;

	ClienteTO converterFornecedor(ClienteTO clienteTO);
}

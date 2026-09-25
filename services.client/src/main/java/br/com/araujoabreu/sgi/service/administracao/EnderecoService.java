package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoPesquisaTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.TipoLogradouroTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface EnderecoService extends CrudService<EnderecoTO> {
	EnderecoPesquisaTO findLastNotaFiscalByUnidadeCodigo(EnderecoPesquisaTO exampleObj);

	List<TipoLogradouroTO> buscarTipoLogradouro();

}

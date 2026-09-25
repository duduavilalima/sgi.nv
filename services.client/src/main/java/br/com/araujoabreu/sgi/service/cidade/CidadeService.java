package br.com.araujoabreu.sgi.service.cidade;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CidadeService extends CrudService<CidadeTO> {

	List<CidadeTO> buscarCidadesByNome(String nomeCidade);

	List<CidadeTO> buscarCidades(String nome);

	List<CidadeTO> getCidade(AutocompleteTO autocompleteTO);

	CidadeTO obterCidadeSimpleTelaNotaFiscal(Long codigoCidade);

	List<String> buscarUFs();

	List<CidadeTO> getCidadesISS(String uf);

}

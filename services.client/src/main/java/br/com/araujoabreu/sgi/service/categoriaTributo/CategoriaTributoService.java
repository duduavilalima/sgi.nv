package br.com.araujoabreu.sgi.service.categoriaTributo;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CategoriaTributoService extends CrudService<CategoriaTributoTO> {
	CategoriaTributoTO buscarCategoriaTributoById(String strCategoriaTributoCodigo);

	CategoriaTributoTO getCategoriaTributoByCod(String id) throws Exception;

	List<CategoriaTributoTO> getListaCategoriaTributoByCodTributo(String id);

	List<CategoriaTributoTO> getListaCategoriaTributoByTributoUF(Long codTributo, String uf);

}

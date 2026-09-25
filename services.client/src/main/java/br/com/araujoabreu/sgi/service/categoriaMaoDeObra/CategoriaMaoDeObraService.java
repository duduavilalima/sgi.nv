package br.com.araujoabreu.sgi.service.categoriaMaoDeObra;

import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CategoriaMaoDeObraService extends CrudService<CategoriaMaoDeObraTO> {

	List<CategoriaMaoDeObraTO> buscarTodasCategorias();
}

package br.com.araujoabreu.sgi.service.categoriamaodeobraadicional;


import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CategoriaMaoDeObraAdicionalService extends CrudService<CategoriaMaoDeObraAdicionalTO> {

	List<CategoriaMaoDeObraAdicionalTO> buscarTodasCategorias();
}

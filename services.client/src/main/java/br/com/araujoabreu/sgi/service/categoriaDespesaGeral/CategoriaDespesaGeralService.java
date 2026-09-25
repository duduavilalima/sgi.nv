package br.com.araujoabreu.sgi.service.categoriaDespesaGeral;

import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CategoriaDespesaGeralService extends CrudService<CategoriaDespesaGeralTO> {

	List<CategoriaDespesaGeralTO> buscarTodasCategorias();
}

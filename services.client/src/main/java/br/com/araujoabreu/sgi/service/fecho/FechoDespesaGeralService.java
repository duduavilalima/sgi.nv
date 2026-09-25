package br.com.araujoabreu.sgi.service.fecho;

import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface FechoDespesaGeralService extends CrudService<FechoDespesaGeralTO> {
	List<FechoDespesaGeralTO> despesaByCodFecho(String id) throws Exception;

	BigDecimal getSubTotal(List<FechoDespesaGeralTO> fechoDespesaGeralTOS, String tipo);

	List<FechoDespesaGeralTO> getListaDespesaByCodFecho(Long codigo);

}

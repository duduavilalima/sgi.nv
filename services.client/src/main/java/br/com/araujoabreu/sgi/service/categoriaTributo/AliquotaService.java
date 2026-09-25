package br.com.araujoabreu.sgi.service.categoriaTributo;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface AliquotaService extends CrudService<AliquotaTO> {
	List<AliquotaTO> getListaAliquotaByCodCategoriaTributo(Long codigo);

	BigDecimal getPercentualAliquotaByCodCategoriaTributo(Long codigo);
}

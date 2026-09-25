package br.com.araujoabreu.sgi.service.fecho;

import br.com.araujoabreu.sgi.service.fecho.transfer.FechoMaoDeObraTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface FechoMaoDeObraService extends CrudService<FechoMaoDeObraTO> {
	List<FechoMaoDeObraTO> maoDeObraByCodFecho(String id) throws Exception;

	BigDecimal getTotal(List<FechoMaoDeObraTO> fechoMaoDeObraTOES, BigDecimal percentualEncargosSociais);

	List<FechoMaoDeObraTO> getListaMaoDeObraByCodFecho(Long codigo);

	BigDecimal totalAdicional(Long codigoFechoMaoDeObra);
}

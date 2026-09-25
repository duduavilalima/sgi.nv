package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaMotivoDescDevTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface NotaSaidaParcelaMotivoDescDevService extends CrudService<NotaSaidaParcelaMotivoDescDevTO> {
	List<NotaSaidaParcelaMotivoDescDevTO> findNotaSaidaParcelaMotivoDescDev(Long codigo);

	void salvarNotaSaidaParcelaMotivoDescDev(NotaSaidaParcelaTO transferObj);

	void deletarPorParcela(Long codigoNotaSaidaParcela);
}

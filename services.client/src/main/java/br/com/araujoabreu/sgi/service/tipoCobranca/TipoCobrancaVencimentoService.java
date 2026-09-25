package br.com.araujoabreu.sgi.service.tipoCobranca;

import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaVencimentoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface TipoCobrancaVencimentoService extends CrudService<TipoCobrancaVencimentoTO> {

	List<TipoCobrancaVencimentoTO> findListaByCodCobranca(Long id);
}

package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ChequeContaCorrenteService extends CrudService<ChequeContaCorrenteTO> {
	List<ChequeContaCorrenteTO> obterListaChequeContaCorrenteDeContaCorrente(Long codigoContaCorrente);

	List<ChequeContaCorrenteTO> obterListaChequeContaCorrenteDeContaCorrente(Long codigoContaCorrente, Long chequeAnterior, Long chequeFinal);
}

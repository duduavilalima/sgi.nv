package br.com.araujoabreu.sgi.service.tesouraria;

import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteItemRelacaoTO;

import java.util.List;

public interface ChequeContaCorrenteItemRelacaoService {

	List<ChequeContaCorrenteItemRelacaoTO> obterChequeContaCorrenteItemRelacaoPorCodigoChequeContaCorrente(Long codigo);

	List<ChequeContaCorrenteItemRelacaoTO> obterChequeContaCorrenteItemRelacaoPorCodigoItemRelacao(Long codigoItemRelacao);
}

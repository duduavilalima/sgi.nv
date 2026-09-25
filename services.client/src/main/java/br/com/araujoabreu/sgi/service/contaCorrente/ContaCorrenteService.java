package br.com.araujoabreu.sgi.service.contaCorrente;

import br.com.araujoabreu.sgi.service.contaCorrente.transfer.ContaCorrenteTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequesEmitidosExibirTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.ConciliacaoBancariaTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface ContaCorrenteService extends CrudService<ContaCorrenteTO> {


	UnidadeTO obterUnidadeDeConatCorrente(Long codigoContaCorrente);

	/**
	 * Obter o cheque anterior de uma conta corrente.
	 * Caso a conta corrente não exita, será retornado 0 (zero).
	 *
	 * @param codigoContaCorrente O código da conta corrente.
	 * @return O número do cheque anterior de uma conta corrente.
	 */
	Long obterNumeroChequeAnterior(Long codigoContaCorrente);

	List<ChequesEmitidosExibirTO> buscarChequesContaCorrente(String codigo);

	List<ChequesEmitidosExibirTO> buscarChequesContaCorrenteNumero(Long codigoContaCorrente);

	ConciliacaoBancariaTO buscarSaldos(ConciliacaoBancariaTO to);

	List<ContaCorrenteTO> buscarChequesContaCorrenteMatriz(String codigopessoa);

	Long obterChequeAnteriorPeloCodigo(long codigoContaCorrente);

	ContaCorrenteTO obterContaCorrenteApenasCodigoEBanco(Long codigo);

	JPAQuery<?> obterBancoDeContaCorrente(Long codigoContaCorrente);

	List<ContaCorrenteTO> obterContasCorrentes(ContaCorrenteTO contaCorrenteTO);

	ContaCorrenteTO findByCodigo(Long codigo);

	ContaCorrenteTO obterContaCorrenteSimpleTelaNotaFiscal(Long codigoContaCorrente);

    List<ChequesEmitidosExibirTO> buscarChequesContaCorrenteNumero(Long codigo, Long chequeAnterior, Long chequeFinal);
}

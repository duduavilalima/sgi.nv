package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.contasApagar.domain.RateioNotaPagamento;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RateioNotaPagamentoRepository extends BaseQueryDslRepository<RateioNotaPagamento, Long> {

	List<RateioNotaPagamento> findByCentroFinanceiroCodigo(Long id);

	List<RateioNotaPagamento> findByNotaPagamentoCodigoAndCodigoCentroFinanceiro(Long codigoNotaFiscal, Long codigoCentroFinanceiro);
}

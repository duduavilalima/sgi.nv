package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.compras.domain.RateioNotaFiscal;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RateioNotaFiscalRepository extends BaseQueryDslRepository<RateioNotaFiscal, Long> {

	List<RateioNotaFiscal> findByCentroFinanceiroCodigo(Long id);
	List<RateioNotaFiscal> findByItemNotaFiscalCodigoAndCodigoCentroFinanceiro(Long codigoItemNotaFiscal, Long codigoCentroFinanceiro);

}

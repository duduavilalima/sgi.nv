package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.compras.domain.RateioItemRecebimento;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RateioItemRecebimentoRepository extends BaseQueryDslRepository<RateioItemRecebimento, Long> {

	List<RateioItemRecebimento> findByCentroFinanceiroCodigo(Long id);

	List<RateioItemRecebimento> findByItemRecebimentoCodigoAndCentroFinanceiroCodigo(Long codigo, Long codigoCf);
}

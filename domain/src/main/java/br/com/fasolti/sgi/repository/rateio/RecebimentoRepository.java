package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.compras.domain.Recebimento;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RecebimentoRepository extends BaseQueryDslRepository<Recebimento, Long> {
	List<Recebimento> findByNotaFiscalCodigo(Long codigo);
}

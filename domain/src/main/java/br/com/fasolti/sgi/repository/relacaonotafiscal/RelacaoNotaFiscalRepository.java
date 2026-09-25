package br.com.fasolti.sgi.repository.relacaonotafiscal;

import br.com.araujoabreu.sgi.compras.domain.RelacaoNotaFiscal;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RelacaoNotaFiscalRepository extends BaseQueryDslRepository<RelacaoNotaFiscal, Long> {

	List<RelacaoNotaFiscal> findByCentroFinanceiroCodigo(Long id);
}

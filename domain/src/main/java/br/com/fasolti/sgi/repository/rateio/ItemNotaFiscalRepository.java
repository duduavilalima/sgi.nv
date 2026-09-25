package br.com.fasolti.sgi.repository.rateio;

import br.com.araujoabreu.sgi.compras.domain.ItemNotaFiscal;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface ItemNotaFiscalRepository extends BaseQueryDslRepository<ItemNotaFiscal, Long> {

	List<ItemNotaFiscal> findByNotaFiscalCodigo(Long codigoNotaFiscal);

}

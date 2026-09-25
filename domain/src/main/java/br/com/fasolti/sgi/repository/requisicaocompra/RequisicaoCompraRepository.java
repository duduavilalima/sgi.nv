package br.com.fasolti.sgi.repository.requisicaocompra;

import br.com.araujoabreu.sgi.compras.domain.RequisicaoCompra;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface RequisicaoCompraRepository extends BaseQueryDslRepository<RequisicaoCompra, Long> {

	List<RequisicaoCompra> findByCentroFinanceiroCodigo(Long id);
}

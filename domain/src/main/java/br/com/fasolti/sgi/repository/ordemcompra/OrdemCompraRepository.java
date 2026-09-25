package br.com.fasolti.sgi.repository.ordemcompra;

import br.com.araujoabreu.sgi.compras.domain.OrdemCompra;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface OrdemCompraRepository extends BaseQueryDslRepository<OrdemCompra, Long> {

	List<OrdemCompra> findByCentroFinanceiroCodigo(Long id);
}

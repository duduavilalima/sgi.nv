package br.com.fasolti.sgi.repository.contasApagar;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends QueryDslCrudRepository<Pagamento> {

	Pagamento findByCodigo(Long codigo);

	List<Pagamento> findByItemRelacoesCodigo(Long codigo);

	List<Pagamento> findByListaNotaTributoAliquotaCodigo(Long codigo);

	List<Pagamento> findByCentroFinanceiroCodigo(Long id);

	long countByCentroFinanceiroCodigo(Long id);

	long countByBancoBoletoCodigo(Long codigo);
}

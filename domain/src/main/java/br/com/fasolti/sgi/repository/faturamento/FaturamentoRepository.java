package br.com.fasolti.sgi.repository.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaturamentoRepository extends QueryDslCrudRepository<Faturamento> {

	List<Faturamento> findByCentroFinanceiroCodigo(Long id);

	long countByCentroFinanceiroCodigo(Long id);

}

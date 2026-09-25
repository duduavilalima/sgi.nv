package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroAliquota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroFinanceiroAliquotaRepository extends QueryDslCrudRepository<CentroFinanceiroAliquota> {

	List<CentroFinanceiroAliquota> findByCentroFinanceiroCodigo(Long id);
}

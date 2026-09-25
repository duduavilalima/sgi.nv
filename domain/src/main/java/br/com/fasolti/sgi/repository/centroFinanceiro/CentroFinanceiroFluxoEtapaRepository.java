package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroFluxoEtapa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroFinanceiroFluxoEtapaRepository extends QueryDslCrudRepository<CentroFinanceiroFluxoEtapa> {

	List<CentroFinanceiroFluxoEtapa> findByCentroFinanceiroCodigo(Long id);
}

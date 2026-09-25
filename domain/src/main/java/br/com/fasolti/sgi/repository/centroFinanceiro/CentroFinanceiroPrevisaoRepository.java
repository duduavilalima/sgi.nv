package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroPrevisao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroFinanceiroPrevisaoRepository extends QueryDslCrudRepository<CentroFinanceiroPrevisao> {

}

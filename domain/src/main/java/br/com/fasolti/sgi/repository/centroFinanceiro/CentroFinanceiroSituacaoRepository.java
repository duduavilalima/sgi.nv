package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroSituacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroFinanceiroSituacaoRepository extends QueryDslCrudRepository<CentroFinanceiroSituacao> {

	List<CentroFinanceiroSituacao> findByCentroFinanceiroCodigo(Long id);
}

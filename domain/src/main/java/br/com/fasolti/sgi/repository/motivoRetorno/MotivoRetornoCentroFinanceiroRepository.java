package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoCentroFinanceiro;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivoRetornoCentroFinanceiroRepository extends QueryDslCrudRepository<MotivoRetornoCentroFinanceiro> {

	List<MotivoRetornoCentroFinanceiro> findByCentroFinanceiroCodigo(Long id);
}

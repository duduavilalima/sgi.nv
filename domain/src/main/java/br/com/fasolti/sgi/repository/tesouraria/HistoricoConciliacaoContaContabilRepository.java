package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.HistoricoConciliacaoContaContabil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoConciliacaoContaContabilRepository extends QueryDslCrudRepository<HistoricoConciliacaoContaContabil> {

	List<HistoricoConciliacaoContaContabil> findByHistoricoConciliacaoCodigo(Long codigo);

	HistoricoConciliacaoContaContabil findByContaContabil(Long codigoContaContabil);
}

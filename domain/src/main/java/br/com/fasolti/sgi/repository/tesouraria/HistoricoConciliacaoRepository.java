package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.HistoricoConciliacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoConciliacaoRepository extends QueryDslCrudRepository<HistoricoConciliacao> {
	HistoricoConciliacao findByDescricao(String descricao);
}

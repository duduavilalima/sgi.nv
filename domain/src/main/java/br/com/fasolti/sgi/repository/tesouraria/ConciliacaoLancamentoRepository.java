package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ConciliacaoLancamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConciliacaoLancamentoRepository extends QueryDslCrudRepository<ConciliacaoLancamento> {

	List<ConciliacaoLancamento> findByConciliacaoBancariaCodigo(Long codigo);

}

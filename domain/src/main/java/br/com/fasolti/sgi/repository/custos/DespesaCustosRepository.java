package br.com.fasolti.sgi.repository.custos;

import br.com.araujoabreu.sgi.custos.DespesaCustos;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaCustosRepository extends QueryDslCrudRepository<DespesaCustos> {
}

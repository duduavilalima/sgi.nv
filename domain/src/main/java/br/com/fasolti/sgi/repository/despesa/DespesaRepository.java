package br.com.fasolti.sgi.repository.despesa;

import br.com.araujoabreu.sgi.compras.domain.Despesa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DespesaRepository extends QueryDslCrudRepository<Despesa> {

}

package br.com.fasolti.sgi.repository.custos;

import br.com.araujoabreu.sgi.custos.PrevisaoCustos;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrevisaoCustosRepository extends QueryDslCrudRepository<PrevisaoCustos> {
}

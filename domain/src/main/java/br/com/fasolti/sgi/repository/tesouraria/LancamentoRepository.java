package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.Lancamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends QueryDslCrudRepository<Lancamento> {

}

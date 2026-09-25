package br.com.fasolti.sgi.repository.contaContabil;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaContabilRepository extends QueryDslCrudRepository<ContaContabil> {
}

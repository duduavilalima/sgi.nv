package br.com.fasolti.sgi.repository.contato;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import org.springframework.stereotype.Repository;

@Repository
public interface RelacionamentoContatoRepository extends QueryDslCrudRepository<RelacionamentoContato> {
}

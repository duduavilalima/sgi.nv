package br.com.fasolti.sgi.repository.administracao;

import br.com.araujoabreu.sgi.administracao.domain.TipoServico;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicoRepository extends QueryDslCrudRepository<TipoServico> {
}

package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.administracao.domain.FormaContratacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaContratacaoRepository extends QueryDslCrudRepository<FormaContratacao> {
}

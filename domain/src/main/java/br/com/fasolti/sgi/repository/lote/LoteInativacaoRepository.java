package br.com.fasolti.sgi.repository.lote;

import br.com.araujoabreu.sgi.administracao.domain.LoteInativacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteInativacaoRepository extends QueryDslCrudRepository<LoteInativacao> {

}

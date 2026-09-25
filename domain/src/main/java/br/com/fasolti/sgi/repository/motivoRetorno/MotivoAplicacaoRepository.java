package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoAplicacaoRepository extends QueryDslCrudRepository<MotivoAplicacao> {

	MotivoAplicacao findByChave(String chave);

}

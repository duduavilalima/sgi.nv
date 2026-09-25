package br.com.fasolti.sgi.repository.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Situacao;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SituacaoRepository extends BaseQueryDslRepository<Situacao, Long> {

	Situacao findByNomeAndSubsituacaoAndChave(@Param("nome") String nome, @Param("subsituacao") String subsituacao, @Param("chave") String chave);
}

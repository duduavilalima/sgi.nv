package br.com.fasolti.sgi.repository.administracao;

import br.com.araujoabreu.sgi.administracao.domain.Tipo;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRepository extends BaseQueryDslRepository<Tipo, Long> {

	Tipo findByNomeAndTabelaAndSubtipo(@Param("nome") String nome, @Param("tabela") String tabela, @Param("subtipo") String subtipo);

}

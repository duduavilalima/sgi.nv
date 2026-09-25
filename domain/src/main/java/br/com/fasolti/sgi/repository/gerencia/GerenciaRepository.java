package br.com.fasolti.sgi.repository.gerencia;

import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GerenciaRepository extends QueryDslCrudRepository<Gerencia> {

	List<Gerencia> findByDiretoria(Diretoria diretoria);

}

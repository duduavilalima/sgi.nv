package br.com.fasolti.sgi.repository.administracao;

import br.com.araujoabreu.sgi.administracao.domain.RegionalGerencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionalGerenciaRepository extends QueryDslCrudRepository<RegionalGerencia> {

}

package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoTipoRepository extends QueryDslCrudRepository<ServicoTipo> {

}

package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.administracao.domain.TipoObra;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoObraRepository extends BaseQueryDslRepository<TipoObra, Long> {
}

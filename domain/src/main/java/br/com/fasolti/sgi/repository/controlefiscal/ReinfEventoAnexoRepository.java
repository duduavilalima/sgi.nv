package br.com.fasolti.sgi.repository.controlefiscal;

import br.com.araujoabreu.sgi.controleFiscal.domain.ReinfEventoAnexo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReinfEventoAnexoRepository extends QueryDslCrudRepository<ReinfEventoAnexo> {
}

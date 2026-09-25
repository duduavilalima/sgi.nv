package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoNotaSaidaParcela;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoRetornoDocumentoNotaSaidaParcelaRepository extends QueryDslCrudRepository<MotivoRetornoDocumentoNotaSaidaParcela> {
}

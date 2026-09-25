package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoChequeContaCorrente;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoRetornoDocumentoContaCorrenteRepository extends BaseQueryDslRepository<MotivoRetornoDocumentoChequeContaCorrente, Long> {
}

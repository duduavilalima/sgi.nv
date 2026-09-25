package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoFaturamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoRetornoDocumentoFaturamentoRepository extends QueryDslCrudRepository<MotivoRetornoDocumentoFaturamento> {
}

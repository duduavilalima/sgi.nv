package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoContrato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivoRetornoDocumentoContratoRepository extends QueryDslCrudRepository<MotivoRetornoDocumentoContrato> {
	List<MotivoRetornoDocumentoContrato> findByContratoCodigo(Long codigo);
}

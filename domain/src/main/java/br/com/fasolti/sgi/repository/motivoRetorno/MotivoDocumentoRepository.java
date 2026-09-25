package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoDocumento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivoDocumentoRepository extends QueryDslCrudRepository<MotivoDocumento> {

	List<MotivoDocumento> findByMotivoRetornoDocumentoCodigo(Long codigo);

}

package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoCidadePrestacaoServicos;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoCidadePrestacaoServicosRepository extends QueryDslCrudRepository<ContratoCidadePrestacaoServicos> {
	ContratoCidadePrestacaoServicos findByContratoCodigoAndCidadeCodigo(Long codigoContrato, Long codigoCidade);
}

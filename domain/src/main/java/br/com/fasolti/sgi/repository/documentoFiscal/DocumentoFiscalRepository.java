package br.com.fasolti.sgi.repository.documentoFiscal;

import br.com.araujoabreu.sgi.compras.domain.DocumentoFiscal;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoFiscalRepository extends BaseQueryDslRepository<DocumentoFiscal, Long> {

	DocumentoFiscal findByCodigo(Long codigo);

	DocumentoFiscal findByCodigoReceita(String codigo);

}

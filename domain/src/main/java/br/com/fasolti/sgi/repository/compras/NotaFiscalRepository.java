package br.com.fasolti.sgi.repository.compras;

import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NotaFiscalRepository extends QueryDslCrudRepository<NotaFiscal> {

	NotaFiscal findByDataEmissaoAndCredorDocumentoAndNumeroLike(Date dataEmissao, String documento, String numero);

}

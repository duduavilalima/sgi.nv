package br.com.fasolti.sgi.repository;

import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrenteDocumentosImpressao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChequeContaCorrenteDocumentosImpressaoRepository extends QueryDslCrudRepository<ChequeContaCorrenteDocumentosImpressao> {
}

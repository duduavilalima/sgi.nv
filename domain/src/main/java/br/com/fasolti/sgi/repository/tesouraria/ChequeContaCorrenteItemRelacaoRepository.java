package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrenteItemRelacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChequeContaCorrenteItemRelacaoRepository extends QueryDslCrudRepository<ChequeContaCorrenteItemRelacao> {

	ChequeContaCorrenteItemRelacao findByCodigoChequeContaCorrenteAndCodigoItemRelacao(Long codigoChequeContaCorrente, Long codigoItemRelacao);

	List<ChequeContaCorrenteItemRelacao> findByCodigoItemRelacao(Long codigoItemRelacao);

	List<ChequeContaCorrenteItemRelacao> findByCodigoChequeContaCorrente(Long codigoChequeContaCorrente);

}

package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrenteItemRelacao;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaCorrenteChequeItemRelacaoRepository extends BaseQueryDslRepository<ChequeContaCorrenteItemRelacao, Long> {

	List<ChequeContaCorrenteItemRelacao> findByCodigoChequeContaCorrente(Long codigoChequeContaCorrente);

}

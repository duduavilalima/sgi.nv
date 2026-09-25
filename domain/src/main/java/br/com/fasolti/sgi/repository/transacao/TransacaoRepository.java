package br.com.fasolti.sgi.repository.transacao;

import br.com.araujoabreu.sgi.fundoFixo.domain.Transacao;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface TransacaoRepository extends BaseQueryDslRepository<Transacao, Long> {

	List<Transacao> findByCentroFinanceiroCodigo(Long id);
}

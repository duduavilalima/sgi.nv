package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ConciliacaoLancamentoAssociacao;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConciliacaoLancamentoAssociacaoRepository extends BaseQueryDslRepository<ConciliacaoLancamentoAssociacao, Long> {

	List<ConciliacaoLancamentoAssociacao> findByConciliacaoLancamentoCodigo(Long codigoConciliacaoLancamento);

	List<ConciliacaoLancamentoAssociacao> findByItemRelacaoCodigo(Long codigoConciliacaoLancamento);

	List<ConciliacaoLancamentoAssociacao> findByNotaSaidaParcelaCodigo(Long codigoConciliacaoLancamento);
}

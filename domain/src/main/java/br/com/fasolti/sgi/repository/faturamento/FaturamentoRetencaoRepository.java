package br.com.fasolti.sgi.repository.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoRetencao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaturamentoRetencaoRepository extends BaseQueryDslRepository<FaturamentoRetencao, Long>, QueryDslCrudRepository<FaturamentoRetencao> {

	List<FaturamentoRetencao> findByFaturamento(Faturamento faturamento);

	void deleteByFaturamentoCodigo(Long codigo);
}

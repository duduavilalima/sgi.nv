package br.com.fasolti.sgi.repository.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoContato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaturamentoContatoRepository extends QueryDslCrudRepository<FaturamentoContato> {
	List<FaturamentoContato> findByFaturamento(Faturamento faturamento);
}

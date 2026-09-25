package br.com.fasolti.sgi.repository.faturamento;

import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoAliquota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaturamentoAliquotaRepository extends QueryDslCrudRepository<FaturamentoAliquota> {

	List<FaturamentoAliquota> findByFaturamentoCodigoOrderByCodigoAsc(Long codigoFaturamento);

}

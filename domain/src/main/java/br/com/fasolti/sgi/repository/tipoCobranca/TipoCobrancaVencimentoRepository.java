package br.com.fasolti.sgi.repository.tipoCobranca;

import br.com.araujoabreu.sgi.faturamento.domain.TipoCobrancaVencimento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCobrancaVencimentoRepository extends QueryDslCrudRepository<TipoCobrancaVencimento> {
}

package br.com.fasolti.sgi.repository.notaPagamento;

import br.com.araujoabreu.sgi.compras.domain.NotaPagamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaPagamentoRepository extends QueryDslCrudRepository<NotaPagamento> {
	NotaPagamento findByNumero(String numero);
}

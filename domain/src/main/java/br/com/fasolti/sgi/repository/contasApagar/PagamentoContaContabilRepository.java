package br.com.fasolti.sgi.repository.contasApagar;

import br.com.araujoabreu.sgi.contasApagar.domain.PagamentoContaContabil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoContaContabilRepository extends QueryDslCrudRepository<PagamentoContaContabil> {

	@Query("select pcc from PagamentoContaContabil pcc where pcc.pagamento.codigo = :codigoPagamento")
	List<PagamentoContaContabil> findPagamentoContaContabilByPagamento(@Param("codigoPagamento") Long codigoPagamento);


}

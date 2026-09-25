package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacaoPagamento;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ItemRelacaoPagamentoRepository extends QueryDslCrudRepository<ItemRelacaoPagamento> {

	ItemRelacaoPagamento findByCodigo(@Param("codigo") Long codigo);

	List<ItemRelacaoPagamento> findByItemRelacaoCodigo(Long codigo);

	@Query("SELECT itemRelacaoPagamento FROM ItemRelacaoPagamento itemRelacaoPagamento JOIN itemRelacaoPagamento.pagamento pagamento WHERE itemRelacaoPagamento.codigo = :codigo")
	List<Pagamento> findPagamentosByItemRelacaoPagamentoCodigo(@Param(value = "codigo") Long codigo);

	@Query("SELECT pagamento FROM ItemRelacaoPagamento itemRelacaoPagamento JOIN itemRelacaoPagamento.pagamento pagamento JOIN itemRelacaoPagamento.itemRelacao itemRelacao WHERE itemRelacao.codigo = :codigo")
	List<Pagamento> findPagamentosByItemRelacaoCodigo(@Param(value = "codigo") Long codigo);

	@Modifying
	@Transactional()
	@Query("DELETE FROM ItemRelacaoPagamento p WHERE p.codigo = ?1")
	void deleteItem(Long codigo);
}

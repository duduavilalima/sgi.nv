package br.com.fasolti.sgi.repository.tesouraria;

import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ItemRelacaoRepository extends QueryDslCrudRepository<ItemRelacao> {

	ItemRelacao findByCodigo(@Param("codigo") Long codigo);

	List<ItemRelacao> findByRelacaoBancariaCodigo(Long codigo);

	@Query(value =
		"select ir.*, " +
			" CASE (select p.tipo from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)) " +
			"     WHEN 2 THEN (select t.nome from tributo t join categoria_tributo ct on t.codigo = ct.codigo_tributo join aliquota alq on ct.codigo = alq.codigo_categoria_tributo where alq.codigo =  " +
			" (select nta.codigo_aliquota from nota_tributo_aliquota nta where nta.codigo_pagamento = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1))) " +
			"     ELSE (select p.nome from pessoa p where codigo = (select p.codigo_credor from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)))  " +
			"     END as nome_credor, " +
			" (select p.documento from pessoa p where codigo = (select p.codigo_credor from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1))) as documento, " +
			" CASE (select p.tipo_doc_pagamento from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1))  " +
			"     WHEN 0 THEN 'Boleto' " +
			"     WHEN 1 THEN 'Depósito/Tranf.' " +
			"     WHEN 2 THEN 'DDA' " +
			"     WHEN 3 THEN 'Guia' " +
			"     WHEN 4 THEN 'DOC' " +
			"     WHEN 5 THEN 'TED' " +
			"     WHEN 6 THEN 'Concessionária' " +
			"     WHEN 7 THEN 'Outros' " +
			"     ELSE 'Não encontrado' " +
			"     END as tipo_documento, " +
			" (select p.data_pagamento from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)) as data_pag, " +
			" (select p.codigo_dados_bancarios from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)) as dados_bancarios, " +
			" CASE (select p.tipo from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)) " +
			"     WHEN 2 THEN '-' " +
			"     ELSE (select b.nome from banco b where b.codigo = (select db.codigo_banco from dados_bancarios db where codigo = (select p.codigo_dados_bancarios from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)))) " +
			"     END as banco, " +
			" CASE (ir.pagamentos) " +
			"     WHEN 1 THEN (select p.numero from pagamento p where codigo = (select irp.codigo_pagamento from item_relacao_pagamento irp where codigo_item_relacao in (select codigo from item_relacao where codigo = ir.codigo) limit 1)) " +
			"     ELSE '-' " +
			"     END as numero " +
			" from item_relacao ir where codigo_relacao_bancaria = ?1 ;"
		, nativeQuery = true)
	List<Object> obterItensDaRelacaobancaria(Long id);

	@Modifying
	@Transactional()
	@Query("update ItemRelacao i set i.cheque = ?1 where i.codigo = ?2")
	void updateCheque(Long numeroCheque, Long codigoItem);

	Long countByBancoCodigo(Long codigo);
}

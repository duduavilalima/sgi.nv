package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaRetencao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaSaidaParcelaRetencaoRepository extends QueryDslCrudRepository<NotaSaidaParcelaRetencao> {

	@Query("select retencao from NotaSaidaParcelaRetencao retencao where retencao.parcela.codigo = ?1 order by retencao.codigo asc")
	List<NotaSaidaParcelaRetencao> findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcela(Long codigoParcela);

	@Query(
		"select retencao from NotaSaidaParcelaRetencao retencao where retencao.parcela.codigo = ?1" +
			" and retencao.tipo = ?2 order by retencao.codigo asc")
	NotaSaidaParcelaRetencao findNotaSaidaParcelaRetencaoByCodigoNotaSaidaParcelaAndTipo(Long codigoParcela, NotaSaidaParcelaRetencao.TipoRetencao tipo);

	List<NotaSaidaParcelaRetencao> findByParcelaCodigo(Long codigo);
}

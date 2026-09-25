package br.com.fasolti.sgi.repository.etapafluxo;

import br.com.araujoabreu.sgi.administracao.domain.EtapaFluxo;
import br.com.araujoabreu.sgi.administracao.domain.Fluxo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtapaFluxoRepository extends QueryDslCrudRepository<EtapaFluxo> {

	List<EtapaFluxo> findByFluxo(Fluxo fluxo);

	@Query("SELECT etapaFluxo FROM EtapaFluxo etapaFluxo WHERE etapaFluxo.fluxo.codigo = :codigoFluxo and etapaFluxo.statusAtual = :statusAtual")
	EtapaFluxo findByCodFluxoAndStatusAtual(@Param(value = "codigoFluxo") Long codigoFluxo, @Param(value = "statusAtual") Integer statusAtual);
}

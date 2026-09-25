package br.com.fasolti.sgi.repository.fluxoCaixa;

import br.com.araujoabreu.sgi.fluxoCaixa.domain.PrevisaoFluxoCaixa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrevisaoFluxoCaixaRepository extends QueryDslCrudRepository<PrevisaoFluxoCaixa> {
	List<PrevisaoFluxoCaixa> findByDespesaFluxoCaixaCodigo(Long codigo);

	@Query("Select pfc FROM PrevisaoFluxoCaixa pfc WHERE pfc.unidade.codigo = :codigoUnidade and pfc.diretoria.codigo = :codigoDiretoria and pfc.despesaFluxoCaixa.codigo = :codigoDespesaFC and pfc.data = :dataPrevisao")
	PrevisaoFluxoCaixa findPrevisaoFluxoCaixa(@Param("codigoUnidade") Long codigoUnidade, @Param("codigoDiretoria") Long codigoDiretoria, @Param("codigoDespesaFC") Long codigoDespesaFC, @Param("dataPrevisao") Date dataPrevisao);

}

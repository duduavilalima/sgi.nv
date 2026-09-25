package br.com.fasolti.sgi.repository.loteInterface;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LoteInterfaceRepository extends BaseQueryDslRepository<LoteInterface, Long> {

	@Query("SELECT loteInterface " +
		" FROM LoteInterface loteInterface " +
		" WHERE loteInterface.tipoLoteInterface = :tipoLoteInterface " +
		" AND loteInterface.situacao = :situacao")
	List<LoteInterface> findLoteContabilPorSituacaoETipo(@Param(value = "tipoLoteInterface") LoteInterface.TipoLoteInterface tipoLoteInterface, @Param(value = "situacao") LoteInterface.SituacaoLoteInterface situacao);

	@Modifying
	@Transactional()
	@Query("UPDATE LoteInterface SET situacao = ?2 WHERE codigo = ?1")
	void updateStatus(Long codigoLoteInterface, LoteInterface.SituacaoLoteInterface situacao);

}

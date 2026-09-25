package br.com.fasolti.sgi.repository.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterfaceNota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteInterfaceNotaRepository extends QueryDslCrudRepository<LoteInterfaceNota> {

	@Query("select loteNota FROM LoteInterfaceNota loteNota INNER JOIN FETCH loteNota.loteInterface WHERE loteNota.nota.codigo = ?1")
	List<LoteInterfaceNota> findLoteNotaByCodigoNota(Long codigo);

	@Query("select nota FROM LoteInterfaceNota nota INNER JOIN FETCH nota.loteInterface WHERE nota.loteInterface.codigo = ?1")
	List<LoteInterfaceNota> findNotaByCodigoLote(Long codigo);

}

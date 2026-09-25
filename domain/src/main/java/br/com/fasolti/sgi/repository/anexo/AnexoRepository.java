package br.com.fasolti.sgi.repository.anexo;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnexoRepository extends BaseQueryDslRepository<Anexo, Long> {

	@Query("Select anexo FROM Anexo anexo INNER JOIN anexo.loteInterfaces loteInterfaces WHERE loteInterfaces.codigo = :codigo")
	List<Anexo> findByCodigoLoteInterfaceJoinFetchLotesInterfaces(@Param("codigo") Long codigo);

}

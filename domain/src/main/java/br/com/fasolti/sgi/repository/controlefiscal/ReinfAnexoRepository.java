package br.com.fasolti.sgi.repository.controlefiscal;

import br.com.araujoabreu.sgi.controleFiscal.domain.ReinfAnexo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReinfAnexoRepository extends QueryDslCrudRepository<ReinfAnexo> {
	List<ReinfAnexo>	findByReinfCodigo(Long codigoReinf);
}

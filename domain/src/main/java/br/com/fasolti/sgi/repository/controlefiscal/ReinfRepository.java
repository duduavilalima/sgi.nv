package br.com.fasolti.sgi.repository.controlefiscal;

import br.com.araujoabreu.sgi.controleFiscal.domain.Reinf;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReinfRepository extends QueryDslCrudRepository<Reinf> {

	Reinf findOneByReferenciaAndUnidadeCodigo(String referencia, Long codigoUnidade);
	List<Reinf> findByReinfStatus(Reinf.ReinfStatus reinfStatus);

}

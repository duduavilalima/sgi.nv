package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquotaEmissao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaTributoAliquotaEmissaoRepository extends QueryDslCrudRepository<NotaTributoAliquotaEmissao> {

	List<NotaTributoAliquotaEmissao> findByNotaCodigo(Long codigo);
}

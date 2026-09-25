package br.com.fasolti.sgi.repository.comercial;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Cnae;
import org.springframework.stereotype.Repository;

@Repository
public interface RamoNegocioRepository extends QueryDslCrudRepository<Cnae> {
}

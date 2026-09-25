package br.com.fasolti.sgi.repository.categoriaTributo;

import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaTributoRepository extends QueryDslCrudRepository<CategoriaTributo> {
}

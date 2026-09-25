package br.com.fasolti.sgi.repository.categoriaMaoDeObra;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaMaoDeObra;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaMaoDeObraRepository extends QueryDslCrudRepository<CategoriaMaoDeObra> {
}

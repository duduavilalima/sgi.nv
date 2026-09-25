package br.com.fasolti.sgi.repository.categoriaDespesaGeral;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaDespesaGeral;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaDespesaGeralRepository extends QueryDslCrudRepository<CategoriaDespesaGeral> {
}

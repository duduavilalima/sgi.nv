package br.com.fasolti.sgi.repository.tributo;

import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TributoRepository extends QueryDslCrudRepository<Tributo> {

	List<Tributo> findTributoByNome(String nome);

}

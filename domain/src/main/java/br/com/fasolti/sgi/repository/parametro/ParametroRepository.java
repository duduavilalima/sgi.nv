package br.com.fasolti.sgi.repository.parametro;

import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends BaseQueryDslRepository<Parametro, String> {

	Parametro findByChave(String chave);
}

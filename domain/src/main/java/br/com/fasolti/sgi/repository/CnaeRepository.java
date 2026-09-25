package br.com.fasolti.sgi.repository;

import br.com.fasolti.corporativo.pessoa.domain.Cnae;
import org.springframework.stereotype.Repository;

@Repository
public interface CnaeRepository extends BaseQueryDslRepository<Cnae, Long> {
	Cnae findBySigla(String sigla);
}

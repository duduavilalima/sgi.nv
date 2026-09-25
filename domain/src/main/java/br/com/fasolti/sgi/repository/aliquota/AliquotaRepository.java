package br.com.fasolti.sgi.repository.aliquota;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AliquotaRepository extends QueryDslCrudRepository<Aliquota> {

}

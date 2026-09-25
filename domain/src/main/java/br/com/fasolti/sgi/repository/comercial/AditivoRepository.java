package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.comercial.domain.Aditivo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AditivoRepository extends QueryDslCrudRepository<Aditivo> {
	List<Aditivo> findByContratoCodigo(Long codigo);
}

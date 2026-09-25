package br.com.fasolti.sgi.repository.diretoria;

import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiretoriaRepository extends QueryDslCrudRepository<Diretoria> {

	List<Diretoria> findByPermiteContrato(Boolean permiteContrato);

	Diretoria findByCodigo(Long codigo);
}

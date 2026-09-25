package br.com.fasolti.sgi.repository.contato;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Contato;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends QueryDslCrudRepository<Contato> {
	List<Contato> findByPessoaCodigo(Long codigo);
}

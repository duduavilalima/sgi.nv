package br.com.fasolti.sgi.repository.banco;


import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BancoRepository extends QueryDslCrudRepository<Banco> {

	List<Banco> findAllByOrderByNome();

}

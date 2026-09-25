package br.com.fasolti.sgi.repository.administracao;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaFisicaRepository extends QueryDslCrudRepository<PessoaFisica> {

	//@Query("select pf.nome from PessoaFisica pf where pf.usuario.login = :login")
	List<PessoaFisica> findByUsuarioLogin(String login);

	PessoaFisica findByCodigo(Long codigo);

	PessoaFisica findByDocumento(String documento);
}

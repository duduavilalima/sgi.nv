package br.com.fasolti.sgi.repository.administracao;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends QueryDslCrudRepository<Pessoa> {

	List<Pessoa> findByNomeIgnoreCaseStartingWithOrderByNomeAsc(String nome);

	Pessoa findByCodigo(Long codigo);

	Pessoa findByDocumento(String documento);

	@Query("SELECT pessoaFisica FROM PessoaFisica pessoaFisica JOIN pessoaFisica.usuario usuario WHERE usuario.login = :login")
	Pessoa findByUsuario(@Param(value = "login") String login);

	List<Pessoa> findByClientePrincipalCodigo(Long codigoCliente);
}

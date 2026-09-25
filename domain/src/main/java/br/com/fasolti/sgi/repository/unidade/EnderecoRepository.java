package br.com.fasolti.sgi.repository.unidade;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends QueryDslCrudRepository<Endereco> {

	@Query("SELECT endereco FROM Pessoa pessoa JOIN pessoa.endereco endereco WHERE pessoa.codigo = :codigoPessoa")
	Endereco findByPessoa(@Param("codigoPessoa") Long codigoPessoa);

}

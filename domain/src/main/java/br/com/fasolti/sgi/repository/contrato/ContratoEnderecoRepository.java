package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoEndereco;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoEnderecoRepository extends QueryDslCrudRepository<ContratoEndereco> {

	List<ContratoEndereco> findByContratoCodigo(Long codigo);
}

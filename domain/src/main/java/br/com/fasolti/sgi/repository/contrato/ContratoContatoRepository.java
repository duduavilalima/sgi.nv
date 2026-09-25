package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoContato;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoContatoRepository extends QueryDslCrudRepository<ContratoContato> {

	List<ContratoContato> findByContratoCodigo(Long codigo);
}

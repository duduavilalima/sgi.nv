package br.com.fasolti.sgi.repository.administracao;

import br.com.fasolti.corporativo.pessoa.domain.TipoLogradouro;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoLogradouroRepository extends BaseQueryDslRepository<TipoLogradouro, Long> {

	List<TipoLogradouro> findAll();

	List<TipoLogradouro> findByNomeContaining(String nome);

	TipoLogradouro findByNome(String nome);

	List<TipoLogradouro> findByNomeStartsWithIgnoreCase(String nome);
}

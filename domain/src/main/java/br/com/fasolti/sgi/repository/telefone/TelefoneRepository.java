package br.com.fasolti.sgi.repository.telefone;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Telefone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TelefoneRepository extends QueryDslCrudRepository<Telefone> {
	List<Telefone> findByPessoaCodigo(Long codigoCliente);

	List<Telefone> findByContatoCodigo(Long codigoContato);

	@Modifying
	@Transactional
	@Query("delete from Telefone t where t.contato.codigo = ?1")
	void removeByContatoCodigo(Long codigo);
}

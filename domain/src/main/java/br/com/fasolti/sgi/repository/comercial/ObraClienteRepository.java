package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.administracao.domain.ObraCliente;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObraClienteRepository extends QueryDslCrudRepository<ObraCliente> {

	List<ObraCliente> findByObraCodigo(Long codigo);

	@Modifying
	@Transactional
	@Query("delete from ObraCliente o where o.obra.codigo = ?1")
	void removeByObraCodigo(Long codigo);
}

package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.comercial.domain.ContratoCategoriaContrato;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoCategoriaContratoRepository extends BaseQueryDslRepository<ContratoCategoriaContrato, Long> {
	List<ContratoCategoriaContrato> findByCodigoCategoriaContrato(Long id);
}

package br.com.fasolti.sgi.repository.comercial;

import br.com.araujoabreu.sgi.comercial.domain.ClassificacaoCliente;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificacaoClienteRepository extends BaseQueryDslRepository<ClassificacaoCliente, Long> {
	List<ClassificacaoCliente> findAll();
}

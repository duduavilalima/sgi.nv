package br.com.fasolti.sgi.repository.mensagem;

import br.com.araujoabreu.sgi.administracao.domain.MensagemGerencia;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemGerenciaRepository extends BaseQueryDslRepository<MensagemGerencia, Long> {

	List<MensagemGerencia> findByCentroFinanceiroCodigo(Long id);
}

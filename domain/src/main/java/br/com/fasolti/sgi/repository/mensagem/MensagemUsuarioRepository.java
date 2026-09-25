package br.com.fasolti.sgi.repository.mensagem;

import br.com.araujoabreu.sgi.administracao.domain.MensagemUsuario;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;

import java.util.List;

public interface MensagemUsuarioRepository extends BaseQueryDslRepository<MensagemUsuario, Long> {

	List<MensagemUsuario> findByCentroFinanceiroCodigo(Long id);
}

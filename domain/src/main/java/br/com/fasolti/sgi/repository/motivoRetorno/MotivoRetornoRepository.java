package br.com.fasolti.sgi.repository.motivoRetorno;

import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetorno;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivoRetornoRepository extends QueryDslCrudRepository<MotivoRetorno> {

	List<MotivoRetorno> findByMotivoAplicacaoCodigo(Long codigo);

	List<MotivoRetorno> findByMotivoAplicacao(MotivoAplicacao motivoAplicacao);

}

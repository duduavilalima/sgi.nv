package br.com.fasolti.sgi.repository.contrato;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.ContratoSituacao;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoSituacaoRepository extends BaseQueryDslRepository<ContratoSituacao, Long> {

	List<ContratoSituacao> findByContratoCodigoAndSituacao(Long codigo, Contrato.SituacaoContrato situacaoContrato);
}

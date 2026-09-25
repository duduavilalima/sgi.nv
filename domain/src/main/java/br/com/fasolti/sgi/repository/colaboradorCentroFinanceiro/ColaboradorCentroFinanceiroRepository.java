package br.com.fasolti.sgi.repository.colaboradorCentroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.ColaboradorCentroFinanceiro;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorCentroFinanceiroRepository extends BaseQueryDslRepository<ColaboradorCentroFinanceiro, Long> {

	List<ColaboradorCentroFinanceiro> findByColaborador(Colaborador colaborador);

	List<ColaboradorCentroFinanceiro> findByColaboradorCodigo(long id);
}

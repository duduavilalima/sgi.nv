package br.com.fasolti.sgi.repository.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroEndereco;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroFinanceiroEnderecoRepository extends QueryDslCrudRepository<CentroFinanceiroEndereco> {

	List<CentroFinanceiroEndereco> findByCentroFinanceiro(CentroFinanceiro centroFinanceiro);

	List<CentroFinanceiroEndereco> findByCentroFinanceiroCodigo(Long codigo);
}

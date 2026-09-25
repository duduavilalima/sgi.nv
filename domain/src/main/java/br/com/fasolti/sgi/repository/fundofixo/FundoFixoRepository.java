package br.com.fasolti.sgi.repository.fundofixo;

import br.com.araujoabreu.sgi.fundoFixo.domain.FundoFixo;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundoFixoRepository extends QueryDslCrudRepository<FundoFixo> {

	List<FundoFixo> findByCentroFinanceiroCodigo(Long id);

	long countByCentroFinanceiroCodigo(Long id);
}

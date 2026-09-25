package br.com.fasolti.sgi.repository.tesouraria;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosBancariosRepository extends QueryDslCrudRepository<DadosBancarios> {

	@Query("select db from Pagamento p join p.dadosBancarios db where p.codigo = :idPagamento")
	DadosBancarios findByPagamentoCodigo(@Param("idPagamento") Long IdPagamento);

	List<DadosBancarios> findByPessoaCodigo(Long codigoPessoa);

	Long countByBancoCodigo(Long id);
}

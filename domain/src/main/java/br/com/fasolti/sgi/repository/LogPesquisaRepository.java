package br.com.fasolti.sgi.repository;

import br.com.fasolti.corporativo.pessoa.domain.LogPesquisa;
import org.springframework.stereotype.Repository;

@Repository
public interface LogPesquisaRepository extends BaseQueryDslRepository<LogPesquisa, Long> {
	LogPesquisa findByDocumento(String documento);
}

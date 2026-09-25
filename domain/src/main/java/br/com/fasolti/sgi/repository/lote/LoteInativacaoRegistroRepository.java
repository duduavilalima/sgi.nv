package br.com.fasolti.sgi.repository.lote;

import br.com.araujoabreu.sgi.administracao.domain.LoteInativacaoRegistro;
import br.com.fasolti.sgi.repository.BaseQueryDslRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteInativacaoRegistroRepository extends BaseQueryDslRepository<LoteInativacaoRegistro, Long> {

	List<LoteInativacaoRegistro> findByCentroFinanceiroCodigo(Long id);
}

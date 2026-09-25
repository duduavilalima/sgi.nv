package br.com.fasolti.sgi.repository.pessoaContaContabil;

import br.com.araujoabreu.sgi.contabilidade.domain.PessoaContaContabil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaContaContabilRepository extends QueryDslCrudRepository<PessoaContaContabil> {
	List<PessoaContaContabil> findByPessoaCodigo(Long codigoCliente);

//	List<PessoaContaContabil> findByPessoaCodigoAndUnidadeCodigo(Long codigoCliente, Long codigoUnidade);

	List<PessoaContaContabil> findByPessoaCodigoAndTipoEspecialIn(Long codigoCliente, List<PessoaContaContabil.TipoRelacionamento> listaTipoPessoaConta);

	Long countByPessoaCodigoAndContaContabilEmpresaCodigo(Long codigoCliente, Long codigoUnidade);
}

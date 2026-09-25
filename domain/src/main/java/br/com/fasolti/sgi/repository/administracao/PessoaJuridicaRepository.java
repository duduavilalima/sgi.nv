package br.com.fasolti.sgi.repository.administracao;

import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaJuridicaRepository extends QueryDslCrudRepository<PessoaJuridica> {

	List<PessoaJuridica> findByNomeFantasiaContainsIgnoreCaseOrNomeContainsIgnoreCaseOrDocumentoContainsIgnoreCaseAndSituacao(String nome, String nome1, String nome2, Pessoa.Situacao situacao);

	List<PessoaJuridica> findByNomeFantasiaContainsIgnoreCaseOrNomeContainsIgnoreCaseOrDocumentoContainsIgnoreCaseAndSituacaoAndTipoPessoaJuridica(String nome, String nome1, String nome2, Pessoa.Situacao situacao, PessoaJuridica.TipoPessoaJuridica tipo);

	PessoaJuridica findByDocumento(String codigo);

	PessoaJuridica findByCodigo(Long codigo);

	List<PessoaJuridica> findByNomeFantasiaContainsIgnoreCaseOrNomeContainsIgnoreCaseOrDocumentoContainsIgnoreCaseOrderByNomeAsc(String nome, String nome1, String nome2);

	List<PessoaJuridica> findByNomeContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrNomeFantasiaContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrDocumentoContainsIgnoreCaseAndClientePrincipalCodigoIsNullOrderByNomeAsc(String nome, String nome1, String nome2);
}

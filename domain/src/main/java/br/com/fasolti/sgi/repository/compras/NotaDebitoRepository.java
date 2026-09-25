package br.com.fasolti.sgi.repository.compras;

import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaDebito;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface NotaDebitoRepository extends QueryDslCrudRepository<NotaDebito> {

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.situacao = ?2 where nd.codigo = ?1")
	void updateSituacao(Long codigo, NotaDebito.SituacaoNotaDebito CANCELADA);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.responsavelNumeracao = ?2 where nd.codigo = ?1")
	void updateResponsavelNumeracao(Long codigo, Pessoa pessoa);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataResponsavelNumeracao = ?2 where nd.codigo = ?1")
	void updateDataResponsavelNumeracao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataEmissao = ?2 where nd.codigo = ?1")
	void updateDataEmissao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataVencimento = ?2 where nd.codigo = ?1")
	void updateDataVencimento(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.numero = ?2 where nd.codigo = ?1")
	void updateNumero(Long codigo, String s);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.responsavelCancelarNumeracao = ?2 where nd.codigo = ?1")
	void updateResponsavelCancelarNumeracao(Long codigo, Pessoa pessoa);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataResponsavelCancelarNumeracao = ?2 where nd.codigo = ?1")
	void updateDataResponsavelCancelamentoNumeracao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.situacao = ?2 where nd.codigo = ?1")
	void updateStatus(Long codigo, NotaDebito.SituacaoNotaDebito canceladaFaturamento);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.responsavelCancelamento = ?2 where nd.codigo = ?1")
	void updateResponsavelCancelamento(Long codigo, PessoaFisica pessoaFisica);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataResponsavelCancelamento = ?2 where nd.codigo = ?1")
	void updateDataResponsavelCancelamento(Long codigo, Date dataCancelamento);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.responsavelReverterCancelamento = ?2 where nd.codigo = ?1")
	void updateResponsavelReverterCancelamento(Long codigo, PessoaFisica pessoaFisica);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataResponsavelReverterCancelamento = ?2 where nd.codigo = ?1")
	void updateDataResponsavelReverterCancelamento(Long codigo, Date dataCancelamento);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.atualizador = ?2 where nd.codigo = ?1")
	void updateAtualizador(Long codigo, String username);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.dataAtualizacao = ?2 where nd.codigo = ?1")
	void updateDataAtualizacao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.empresa = ?2 where nd.codigo = ?1")
	void updateEmpresa(Long codigo, Unidade unidade);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.protocoloEntrega = ?2 where nd.codigo = ?1")
	void updateProtocoloEntrega(Long codigo, String protocoloEntrega);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.servicoTipo = ?2 where nd.codigo = ?1")
	void updateServicoTipo(Long codigo, ServicoTipo servicoTipo);

	@Modifying
	@Transactional()
	@Query("update NotaDebito nd set nd.descricao = ?2 where nd.codigo = ?1")
    void updateDescricao(Long codigo, String descricao);

}

package br.com.fasolti.sgi.repository.notaSaida;

import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface NotaSaidaParcelaRepository extends QueryDslCrudRepository<NotaSaidaParcela>, JpaRepository<NotaSaidaParcela, Long> {

	@Query("select parcela.situacao from NotaSaidaParcela parcela where parcela.notaSaida.codigo = ?1 order by parcela.codigo asc")
	List<NotaSaidaParcela.SituacaoParcela> findSituacaoNotaSaidaParcelaByCodigoNotaSaida(Long codigo);

	List<NotaSaidaParcela> findByNotaSaidaCodigo(Long codigo);

	@Query("select parcela.notaSaida from NotaSaidaParcela parcela where parcela.codigo = ?1")
	NotaSaida findNotaSaidaByCodigoNotaSaidaParcela(Long codigo);

	@Query("select parcela.notaSaida.codigo from NotaSaidaParcela parcela where parcela.codigo = ?1")
	Long findCodigoNotaSaidaByCodigoNotaSaidaParcela(Long codigo);

	@Query(
		"select parcela from NotaSaidaParcela parcela where parcela.notaSaida.codigo = ?1" +
			" and parcela.ordem = ?2 order by parcela.codigo asc")
	NotaSaidaParcela findNotaSaidaParcelaByCodigoNotaSaidaAndOrdem(Long codigo, Integer ordem);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET situacao = ?1 WHERE notaSaida.codigo = (SELECT lote.nota.codigo FROM LoteInterfaceNota lote INNER JOIN lote.nota WHERE lote.codigo = ?2 )")
	void updateStatusByLoteInterfaceNota(NotaSaidaParcela.SituacaoParcela situacao, Long codigoLoteInterfaceNota);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET situacao = ?2 WHERE codigo = ?1")
	void updateStatus(Long codigoNotaSaidaParcela, NotaSaidaParcela.SituacaoParcela situacao);


	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataVencimento = ?1 WHERE codigo = ?2")
	void updateDataVencimento(Date dataVencimento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataPagamento = ?1 WHERE codigo = ?2")
	void updateDataPagamento(Date dataPagamento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET contaCorrentePrevista = ?1 WHERE codigo = ?2")
	void updateContaCorrentePrevista(ContaCorrente contaCorrentePrevista, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET contaCorrenteRecebimento = ?1 WHERE codigo = ?2")
	void updateContaCorrenteRecebimento(ContaCorrente contaCorrenteRecebimento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET tipoDocumentoPrevisto = ?1 WHERE codigo = ?2")
	void updateTipoDocumentoPrevisto(Pagamento.TipoDocumentoPagamento tipoDocumentoPrevisto, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET tipoDocumentoRecebimento = ?1 WHERE codigo = ?2")
	void updateTipoDocumentoRecebimento(Pagamento.TipoDocumentoPagamento tipoDocumentoRecebimento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET motivoDataVencimento = ?1 WHERE codigo = ?2")
	void updateMotivoDataVencimento(String motivoDataVencimento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET motivoDataPagamento = ?1 WHERE codigo = ?2")
	void updateMotivoDataPagamento(String motivoDataPagamento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET motivoDataAgendamento = ?1 WHERE codigo = ?2")
	void updateMotivoDataAgendamento(String motivoDataAgendamento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET responsavelRecebimento = ?1 WHERE codigo = ?2")
	void updateResponsavelRecebimento(PessoaFisica pessoa, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET responsavelCancRecebimento = ?1 WHERE codigo = ?2")
	void updateResponsavelCancRecebimento(PessoaFisica pessoa, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataResponsavelCancelamento = ?1 WHERE codigo = ?2")
	void updateDataCancelamento(Date date, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET responsavelCancelamento = ?1 WHERE codigo = ?2")
	void updateResponsavelCancelamento(PessoaFisica pessoa, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataResponsavelRecebimento = ?1 WHERE codigo = ?2")
	void updateDataResponsavelRecebimento(Date date, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataResponsavelCancRecebimento = ?1 WHERE codigo = ?2")
	void updateDataResponsavelCancRecebimento(Date date, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataAgendamento = ?1 WHERE codigo = ?2")
	void updateDataAgendamento(Date dataAgendamento, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorPermuta = ?1 WHERE codigo = ?2")
	void updateValorPermuta(BigDecimal valorPermuta, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorDesconto = ?1 WHERE codigo = ?2")
	void updateValorDesconto(BigDecimal valorDesconto, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorMulta = ?1 WHERE codigo = ?2")
	void updateValorMulta(BigDecimal valorMulta, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorJuros = ?1 WHERE codigo = ?2")
	void updateValorJuros(BigDecimal valorJuros, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET situacao = ?1 WHERE codigo = ?2")
	void updateSituacao(NotaSaidaParcela.SituacaoParcela finalizada, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorBruto = ?2 WHERE codigo = ?1")
	void updateValorBruto(Long codigo, BigDecimal valor);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorTributo = ?2 WHERE codigo = ?1")
	void updateValorTributo(Long codigo, BigDecimal valor);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorLiquido = ?2 WHERE codigo = ?1")
	void updateValorLiquido(Long codigo, BigDecimal valorLiquido);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET ordem = ?2 WHERE codigo = ?1")
	void updateValorOrdem(Long codigo, Integer ordem);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataAtualizacao = ?2 WHERE codigo = ?1")
	void updateDataAtualizacao(Long codigo, Date date);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET atualizador = ?2 WHERE codigo = ?1")
	void updateAtualizador(Long codigo, String username);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataContabilizacao = ?1 WHERE codigo = ?2")
	void updateDataContabilizacao(Date dataContabilizacao, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET valorDevolucao = ?2 WHERE codigo = ?1")
	void updateValorDevolucao(Long codigo, BigDecimal valorDevolucao);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET cobrancaCronica = ?1 WHERE codigo = ?2")
	void updateCobrancaCronica(Boolean cobrancaCronica, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET responsavelMarcaCobrancaCronica = ?1 WHERE codigo = ?2")
	void updateResponsavelMarcaCobrancaCronica(PessoaFisica pessoa, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataResponsavelMarcaCobrancaCronica = ?1 WHERE codigo = ?2")
	void updateDataResponsavelMarcaCobrancaCronica(Date date, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET responsavelDesmarcaCobrancaCronica = ?1 WHERE codigo = ?2")
	void updateResponsavelDesmarcaCobrancaCronica(PessoaFisica pessoa, Long id);

	@Modifying
	@Transactional()
	@Query("UPDATE NotaSaidaParcela SET dataResponsavelDesmarcaCobrancaCronica = ?1 WHERE codigo = ?2")
	void updateDataResponsavelDesmarcaCobrancaCronica(Date date, Long id);


}
